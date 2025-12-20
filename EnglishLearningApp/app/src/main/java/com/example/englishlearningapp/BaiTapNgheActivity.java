package com.example.englishlearningapp;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
// IMPORT DTO MỚI
import com.example.englishlearningapp.DTO.Response.CauHoiResponse;
import com.example.englishlearningapp.ApiClient;
import com.example.englishlearningapp.Retrofit.ApiService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaiTapNgheActivity extends AppCompatActivity implements CauHoiAdapter.LangNgheSuKienChonDapAn {

    // --- BIẾN UI ---
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
    private boolean daChuanBiNhac = false; // Biến kiểm tra nhạc đã load xong chưa

    // --- BIẾN DỮ LIỆU ---
    private List<CauHoiResponse> danhSachCauHoi = new ArrayList<>(); // Dùng CauHoiResponse
    private CauHoiAdapter adapter;
    private Set<Integer> tapHopIdCauHoiDaTraLoi = new HashSet<>();

    private String capDoHienTai = "Basic";
    private int idBaiTapHienTai = -1;

    // BASE URL chứa file nhạc (Nếu server trả về link full thì để rỗng)
    // Ví dụ: http://192.168.1.5:8080/audio/
    private static final String BASE_AUDIO_URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_tap_nghe);

        // 1. Nhận dữ liệu Level & ID Bài tập
        if (getIntent() != null) {
            idBaiTapHienTai = getIntent().getIntExtra("ID_BAI_TAP", -1);
            capDoHienTai = getIntent().getStringExtra("MUC_DO");
        }

        // 2. Ánh xạ View
        anhXaView();

        // 3. Cài đặt RecyclerView
        setupRecyclerView();

        // 4. Gọi API lấy dữ liệu
        if (idBaiTapHienTai != -1) {
            goiApiLayCauHoi(idBaiTapHienTai);
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy bài tập!", Toast.LENGTH_SHORT).show();
        }

        // 5. Cài đặt sự kiện (Nút bấm, Seekbar...)
        caiDatSuKien();
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

        // Mặc định disable nút nhạc đến khi load xong
        nutPhatNhac.setEnabled(false);
        nutPhatNhac.setAlpha(0.5f);
    }

    private void setupRecyclerView() {
        adapter = new CauHoiAdapter(this, danhSachCauHoi);
        rcvDanhSachCauHoiNghe.setLayoutManager(new LinearLayoutManager(this));
        rcvDanhSachCauHoiNghe.setAdapter(adapter);
    }

    // --- GỌI API LẤY CÂU HỎI ---
    private void goiApiLayCauHoi(int baiTapId) {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        apiService.getCauHoiByBaiTapId(baiTapId).enqueue(new Callback<List<CauHoiResponse>>() {
            @Override
            public void onResponse(Call<List<CauHoiResponse>> call, Response<List<CauHoiResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    danhSachCauHoi.clear();
                    List<CauHoiResponse> listTuServer = response.body();

                    for (CauHoiResponse ch : listTuServer) {
                        ch.xuLyDuLieu(); // Parse JSON đáp án
                        danhSachCauHoi.add(ch);
                    }
                    adapter.notifyDataSetChanged();

                    // Cập nhật tiến trình
                    thanhTienTrinh.setMax(danhSachCauHoi.size());
                    capNhatTrangThaiTienTrinh();

                    // --- XỬ LÝ AUDIO ---
                    // Lấy link nhạc từ câu hỏi đầu tiên (hoặc logic tùy bạn)
                    if (!danhSachCauHoi.isEmpty()) {
                        String audioUrl = danhSachCauHoi.get(0).getAudioUrl();
                        if (audioUrl != null && !audioUrl.isEmpty()) {
                            // Nếu URL chưa có http, ghép với BASE_AUDIO_URL
                            if (!audioUrl.startsWith("http")) {
                                audioUrl = BASE_AUDIO_URL + audioUrl;
                            }
                            chuanBiBaiHat(audioUrl);
                        } else {
                            // Nếu không có link nhạc -> Load nhạc mẫu offline để test không bị lỗi
                            chuanBiNhacOffline();
                        }
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

    // --- LOGIC PHÁT NHẠC ONLINE (STREAMING) ---
    private void chuanBiBaiHat(String url) {
        try {
            if (mayPhatNhac != null) {
                mayPhatNhac.release();
            }
            mayPhatNhac = new MediaPlayer();
            mayPhatNhac.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            );
            mayPhatNhac.setDataSource(url);
            mayPhatNhac.prepareAsync(); // Load bất đồng bộ để không đơ ứng dụng

            mayPhatNhac.setOnPreparedListener(mp -> {
                // Nhạc đã load xong
                daChuanBiNhac = true;
                nutPhatNhac.setEnabled(true);
                nutPhatNhac.setAlpha(1.0f);
                thanhTuaNhac.setMax(mayPhatNhac.getDuration());
                tvThoiLuong.setText(dinhDangThoiGian(mayPhatNhac.getDuration()));
                Toast.makeText(BaiTapNgheActivity.this, "Đã tải audio xong!", Toast.LENGTH_SHORT).show();
            });

            setupMediaPlayerListeners();

        } catch (Exception e) {
            e.printStackTrace();
//            Toast.makeText(this, "Lỗi tải file nhạc: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Fallback: Nếu không có link online thì load file mẫu trong máy
    private void chuanBiNhacOffline() {
        try {
            mayPhatNhac = MediaPlayer.create(this, R.raw.audio_sample); // Đảm bảo bạn có file này trong res/raw
            if(mayPhatNhac != null) {
                daChuanBiNhac = true;
                nutPhatNhac.setEnabled(true);
                nutPhatNhac.setAlpha(1.0f);
                thanhTuaNhac.setMax(mayPhatNhac.getDuration());
                tvThoiLuong.setText(dinhDangThoiGian(mayPhatNhac.getDuration()));
                setupMediaPlayerListeners();
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    private void caiDatSuKien() {
        nutQuayLai.setOnClickListener(v -> finish());

        btnHoanThanh.setOnClickListener(v -> chuyenSangTrangKetQua());

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
                int viTriHienTai = mayPhatNhac.getCurrentPosition();
                thanhTuaNhac.setProgress(viTriHienTai);
                tvThoiLuong.setText(dinhDangThoiGian(viTriHienTai));
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

    // --- LOGIC TIẾN ĐỘ ---
    @Override
    public void khiDapAnDuocChon(int maCauHoi, String dapAnDuocChon) {
        if (dapAnDuocChon != null) {
            tapHopIdCauHoiDaTraLoi.add(maCauHoi);
        } else {
            tapHopIdCauHoiDaTraLoi.remove(maCauHoi);
        }
        capNhatTrangThaiTienTrinh();
    }

    private void capNhatTrangThaiTienTrinh() {
        int soCauDaTraLoi = tapHopIdCauHoiDaTraLoi.size();
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

    private void chuyenSangTrangKetQua() {
        Intent intent = new Intent(BaiTapNgheActivity.this, KetQuaActivity.class);

        intent.putExtra(KetQuaActivity.EXTRA_CORRECT_ANSWERS, tapHopIdCauHoiDaTraLoi.size());
        intent.putExtra(KetQuaActivity.EXTRA_TOTAL_QUESTIONS, danhSachCauHoi.size());

        // --- DÒNG QUAN TRỌNG CÒN THIẾU ---
        intent.putExtra("ID_BAI_TAP", idBaiTapHienTai);

        intent.putExtra(KetQuaActivity.EXTRA_TOPIC, "Nghe");
        intent.putExtra(KetQuaActivity.EXTRA_LEVEL, capDoHienTai);

        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mayPhatNhac != null) {
            boXuLyAmThanh.removeCallbacks(tacVuCapNhatThanhTua);
            mayPhatNhac.release();
            mayPhatNhac = null;
        }
    }
}