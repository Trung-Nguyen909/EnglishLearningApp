package com.example.englishlearningapp.Activity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Adapter.CauHoiAdapter;
import com.example.englishlearningapp.DTO.Response.CauHoiResponse;
import com.example.englishlearningapp.ApiClient;
import com.example.englishlearningapp.R;
import com.example.englishlearningapp.Retrofit.ApiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaiTapNgheActivity extends AppCompatActivity implements CauHoiAdapter.LangNgheSuKienChonDapAn {

    // --- BIẾN GIAO DIỆN ---
    private Button btnHoanThanh;
    private ProgressBar thanhTienTrinh;
    private TextView tvDemSoCauHoi;
    private TextView tvPhanTramTienTrinh;
    private RecyclerView rcvDanhSachCauHoiNghe;
    private ImageView nutQuayLai;

    // --- BIẾN AUDIO ---
    private ImageButton nutPhatNhac;
    private SeekBar thanhTuaNhac;
    private TextView tvThoiLuong;
    private MediaPlayer mayPhatNhac;
    private Handler boXuLyAmThanh = new Handler();
    private boolean dangPhat = false;
    private boolean daChuanBiNhac = false;

    // --- BIẾN DỮ LIỆU ---
    private List<CauHoiResponse> danhSachCauHoi = new ArrayList<>();
    private CauHoiAdapter adapter;

    // 1. DÙNG MAP ĐỂ LƯU ĐÁP ÁN (Giống bài Đọc)
    private Map<Integer, String> danhSachDapAnNguoiDung = new HashMap<>();

    private String capDoHienTai = "Basic";
    private int idBaiTapHienTai = -1;

    // 2. BIẾN TÍNH GIỜ (Giống bài Đọc)
    private int soGiayLamBai = 0;
    private Handler boDemGio = new Handler();
    private Runnable tacVuDemGio = new Runnable() {
        @Override
        public void run() {
            soGiayLamBai++;
            boDemGio.postDelayed(this, 1000);
        }
    };

    // Link nhạc gốc nếu cần (để trống nếu server trả link full)
    private static final String BASE_AUDIO_URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_tap_nghe);

        // Nhận dữ liệu
        if (getIntent() != null) {
            idBaiTapHienTai = getIntent().getIntExtra("ID_BAI_TAP", -1);
            capDoHienTai = getIntent().getStringExtra("MUC_DO");
        }

        anhXaView();
        setupRecyclerView();

        if (idBaiTapHienTai != -1) {
            goiApiLayCauHoi(idBaiTapHienTai);
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy bài tập!", Toast.LENGTH_SHORT).show();
        }

        caiDatSuKien();

        // 3. BẮT ĐẦU ĐẾM GIỜ
        batDauDemGio();
    }

    private void batDauDemGio() {
        soGiayLamBai = 0;
        boDemGio.postDelayed(tacVuDemGio, 1000);
    }

    private void anhXaView() {
        btnHoanThanh = findViewById(R.id.btn_hoan_thanh);
        thanhTienTrinh = findViewById(R.id.thanh_tien_trinh);
        tvDemSoCauHoi = findViewById(R.id.tv_dem_so_cau_hoi);
        tvPhanTramTienTrinh = findViewById(R.id.tv_phan_tram_tien_trinh);
        rcvDanhSachCauHoiNghe = findViewById(R.id.rcv_danh_sach_cau_hoi_nghe);
        nutQuayLai = findViewById(R.id.nut_quay_lai);

        nutPhatNhac = findViewById(R.id.nut_phat_nhac);
        thanhTuaNhac = findViewById(R.id.thanh_tua_nhac);
        tvThoiLuong = findViewById(R.id.tv_thoi_luong);

//        nutPhatNhac.setEnabled(false);
//        nutPhatNhac.setAlpha(0.5f);
        nutPhatNhac.setEnabled(true);
        nutPhatNhac.setAlpha(1.0f);
    }

    private void setupRecyclerView() {
        adapter = new CauHoiAdapter(this, danhSachCauHoi);
        rcvDanhSachCauHoiNghe.setLayoutManager(new LinearLayoutManager(this));
        rcvDanhSachCauHoiNghe.setAdapter(adapter);
    }

    private void caiDatSuKien() {
        nutQuayLai.setOnClickListener(v -> finish());

        // Nút Hoàn thành -> Chuyển sang tính điểm
        btnHoanThanh.setOnClickListener(v -> chuyenSangTrangKetQua());

        // Sự kiện Audio
        nutPhatNhac.setOnClickListener(v -> {
            if (!daChuanBiNhac) return;
            if (dangPhat) tamDungNhac();
            else phatNhac();
        });

        thanhTuaNhac.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mayPhatNhac != null && daChuanBiNhac) {
                    mayPhatNhac.seekTo(progress);
                    tvThoiLuong.setText(dinhDangThoiGian(progress));
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void goiApiLayCauHoi(int baiTapId) {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        apiService.getCauHoiByBaiTapId(baiTapId).enqueue(new Callback<List<CauHoiResponse>>() {
            @Override
            public void onResponse(Call<List<CauHoiResponse>> call, Response<List<CauHoiResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    danhSachCauHoi.clear();
                    List<CauHoiResponse> listTuServer = response.body();

                    for (CauHoiResponse ch : listTuServer) {
                        ch.xuLyDuLieu();
                        danhSachCauHoi.add(ch);
                    }
                    adapter.notifyDataSetChanged();
                    thanhTienTrinh.setMax(danhSachCauHoi.size());
                    capNhatTrangThaiTienTrinh();

                    // --- LOGIC MỚI: CHƠI NHẠC TỪ FILE LOCAL (OFFLINE) ---
                    if (!danhSachCauHoi.isEmpty()) {
                        // Lấy tên file từ DB (Ví dụ: "dog.mp3")
                        String tenFileTuServer = danhSachCauHoi.get(0).getAudioUrl();

                        // Chuyển thành ID trong res/raw
                        int resId = layIdNhacTuTenFile(tenFileTuServer);

                        // Phát nhạc
                        chuanBiNhacOffline(resId);
                    }

                } else {
                    Toast.makeText(BaiTapNgheActivity.this, "Không có câu hỏi nào!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CauHoiResponse>> call, Throwable t) {
                Toast.makeText(BaiTapNgheActivity.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int layIdNhacTuTenFile(String tenFile) {
        if (tenFile == null || tenFile.isEmpty()) return 0;

        // 1. Xóa đuôi .mp3 nếu có (Ví dụ: "dog.mp3" -> "dog")
        String tenSach = tenFile.toLowerCase().replace(".mp3", "").trim();

        // 2. Tìm ID trong thư mục raw
        int resId = getResources().getIdentifier(tenSach, "raw", getPackageName());

        // 3. Nếu không tìm thấy file đúng tên, dùng file mẫu (audio_sample) để không bị lỗi
        if (resId == 0) {
            resId = getResources().getIdentifier("audio_sample", "raw", getPackageName());
        }

        return resId;
    }

    // --- LOGIC TÍNH ĐIỂM & CHUYỂN TRANG (ĐÃ SỬA GIỐNG BÀI ĐỌC) ---
    private void chuyenSangTrangKetQua() {
        // Dừng đếm giờ và nhạc
        boDemGio.removeCallbacks(tacVuDemGio);
        tamDungNhac();

        int soCauDung = 0;
        for (CauHoiResponse cauHoi : danhSachCauHoi) {
            int id = cauHoi.getId();

            // Lấy đáp án đúng (Text: "Father", "Yes"...)
            String dapAnDungHeThong = cauHoi.getDapAnDung();

            // Lấy đáp án người dùng chọn từ Map (Text: "Father")
            String dapAnNguoiDungChon = danhSachDapAnNguoiDung.get(id);

            // So sánh
            if (dapAnNguoiDungChon != null && dapAnDungHeThong != null) {
                if (dapAnNguoiDungChon.trim().equalsIgnoreCase(dapAnDungHeThong.trim())) {
                    soCauDung++;
                }
            }
        }

        Intent intent = new Intent(BaiTapNgheActivity.this, KetQuaActivity.class);

        intent.putExtra(KetQuaActivity.EXTRA_CORRECT_ANSWERS, soCauDung);
        intent.putExtra(KetQuaActivity.EXTRA_TOTAL_QUESTIONS, danhSachCauHoi.size());

        // Truyền thời gian đã làm
        intent.putExtra(KetQuaActivity.EXTRA_TIME_SPENT, soGiayLamBai);

        // Truyền thông tin để làm lại
        intent.putExtra("ID_BAI_TAP", idBaiTapHienTai);
        intent.putExtra(KetQuaActivity.EXTRA_TOPIC, "Nghe");
        intent.putExtra(KetQuaActivity.EXTRA_LEVEL, capDoHienTai);

        startActivity(intent);
        finish();
    }

    // --- CẬP NHẬT MAP ĐÁP ÁN (GIỐNG BÀI ĐỌC) ---
    @Override
    public void khiDapAnDuocChon(int maCauHoi, String dapAnDuocChon) {
        if (dapAnDuocChon != null) {
            danhSachDapAnNguoiDung.put(maCauHoi, dapAnDuocChon);
        } else {
            danhSachDapAnNguoiDung.remove(maCauHoi);
        }
        capNhatTrangThaiTienTrinh();
    }

    private void capNhatTrangThaiTienTrinh() {
        int soCauDaTraLoi = danhSachDapAnNguoiDung.size();
        int tongSoCau = danhSachCauHoi.size();
        int phanTram = (tongSoCau > 0) ? (int) (((float) soCauDaTraLoi / tongSoCau) * 100) : 0;

        tvDemSoCauHoi.setText("Câu " + soCauDaTraLoi + "/" + tongSoCau);
        tvPhanTramTienTrinh.setText(phanTram + "%");
        thanhTienTrinh.setProgress(soCauDaTraLoi);

        if (soCauDaTraLoi == tongSoCau && tongSoCau > 0) {
            btnHoanThanh.setEnabled(true);
            btnHoanThanh.setVisibility(View.VISIBLE);
            btnHoanThanh.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.royalBlue));
        } else {
            btnHoanThanh.setEnabled(false);
            btnHoanThanh.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.darker_gray));
        }
    }

    // --- CÁC HÀM AUDIO PLAYER (KHÔNG ĐỔI) ---
    private void chuanBiBaiHat(String url) {
        try {
            if (mayPhatNhac != null) mayPhatNhac.release();
            mayPhatNhac = new MediaPlayer();
            mayPhatNhac.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            );
            mayPhatNhac.setDataSource(url);
            mayPhatNhac.prepareAsync();
            mayPhatNhac.setOnPreparedListener(mp -> {
                daChuanBiNhac = true;
                nutPhatNhac.setEnabled(true);
                nutPhatNhac.setAlpha(1.0f);
                thanhTuaNhac.setMax(mayPhatNhac.getDuration());
                tvThoiLuong.setText(dinhDangThoiGian(mayPhatNhac.getDuration()));
            });
            setupMediaPlayerListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chuanBiNhacOffline(int resId) {
        if (resId == 0) {
            Toast.makeText(this, "Không tìm thấy file nhạc!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (mayPhatNhac != null) mayPhatNhac.release();

            // Tạo MediaPlayer từ Resource ID
            mayPhatNhac = MediaPlayer.create(this, resId);

            if (mayPhatNhac != null) {
                daChuanBiNhac = true;

                // Cập nhật giao diện
                thanhTuaNhac.setMax(mayPhatNhac.getDuration());
                tvThoiLuong.setText(dinhDangThoiGian(mayPhatNhac.getDuration()));

                // Mở khóa nút bấm
                nutPhatNhac.setEnabled(true);
                nutPhatNhac.setAlpha(1.0f);

                setupMediaPlayerListeners();
            } else {
                Toast.makeText(this, "Lỗi khởi tạo nhạc!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupMediaPlayerListeners() {
        if (mayPhatNhac == null) return;
        mayPhatNhac.setOnCompletionListener(mp -> {
            tamDungNhac();
            mayPhatNhac.seekTo(0);
            thanhTuaNhac.setProgress(0);
        });
    }

    private void phatNhac() {
        if (mayPhatNhac != null && daChuanBiNhac) {
            mayPhatNhac.start();
            dangPhat = true;
            nutPhatNhac.setImageResource(android.R.drawable.ic_media_pause);
            capNhatThanhTua();
        }
    }

    private void tamDungNhac() {
        if (mayPhatNhac != null) {
            mayPhatNhac.pause();
            dangPhat = false;
            nutPhatNhac.setImageResource(android.R.drawable.ic_media_play);
            boXuLyAmThanh.removeCallbacks(tacVuCapNhatThanhTua);
        }
    }

    private Runnable tacVuCapNhatThanhTua = new Runnable() {
        @Override
        public void run() {
            if (mayPhatNhac != null && dangPhat) {
                int pos = mayPhatNhac.getCurrentPosition();
                thanhTuaNhac.setProgress(pos);
                tvThoiLuong.setText(dinhDangThoiGian(pos));
                boXuLyAmThanh.postDelayed(this, 100);
            }
        }
    };

    private void capNhatThanhTua() {
        boXuLyAmThanh.postDelayed(tacVuCapNhatThanhTua, 0);
    }

    private String dinhDangThoiGian(int miliGiay) {
        int giay = (miliGiay / 1000) % 60;
        int phut = (miliGiay / (1000 * 60)) % 60;
        return String.format("%02d:%02d", phut, giay);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (boDemGio != null) boDemGio.removeCallbacks(tacVuDemGio);
        if (mayPhatNhac != null) {
            boXuLyAmThanh.removeCallbacks(tacVuCapNhatThanhTua);
            mayPhatNhac.release();
            mayPhatNhac = null;
        }
    }
}