package com.example.englishlearningapp;

import android.content.Intent;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Model.CauHoiModel;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BaiTapNgheActivity extends AppCompatActivity implements CauHoiAdapter.LangNgheSuKienChonDapAn {

    private List<CauHoiModel> danhSachCauHoi;
    private Button btnHoanThanh;
    private ProgressBar thanhTienTrinh;
    private TextView tvDemSoCauHoi;
    private TextView tvPhanTramTienTrinh;
    private RecyclerView rcvDanhSachCauHoiNghe;
    private ImageView nutQuayLai;

    private ImageButton nutPhatNhac;
    private SeekBar thanhTuaNhac;
    private TextView tvThoiLuong;

    // Logic xử lý Audio
    private MediaPlayer mayPhatNhac;
    private Handler boXuLyAmThanh = new Handler();
    private boolean dangPhat = false;

    // Logic theo dõi tiến độ
    private Set<Integer> tapHopIdCauHoiDaTraLoi = new HashSet<>();
    private String capDoHienTai = "Basic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_tap_nghe);

        // 1. Nhận dữ liệu Level
        if (getIntent() != null) {
            String levelNhanDuoc = getIntent().getStringExtra("SELECTED_LEVEL");
            if (levelNhanDuoc != null) {
                capDoHienTai = levelNhanDuoc;
            }
        }

        // 2. Ánh xạ View
        anhXaView();

        // 3. Khởi tạo dữ liệu
        danhSachCauHoi = taoCauHoiNghe();

        // 4. Cài đặt RecyclerView và Audio
        caiDatDanhSachCauHoi();
        caiDatTrinhPhatNhac();

        // 5. Thiết lập trạng thái ban đầu
        thanhTienTrinh.setMax(danhSachCauHoi.size());
        capNhatTrangThaiTienTrinh();

        // Xử lý sự kiện nút Quay lại
        nutQuayLai.setOnClickListener(v -> finish());

        // Xử lý sự kiện nút Hoàn thành
        btnHoanThanh.setOnClickListener(v -> {
            chuyenSangTrangKetQua();
        });
    }

    private void anhXaView() {
        // Ánh xạ các thành phần chung
        btnHoanThanh = findViewById(R.id.btn_hoan_thanh);
        thanhTienTrinh = findViewById(R.id.thanh_tien_trinh);
        tvDemSoCauHoi = findViewById(R.id.tv_dem_so_cau_hoi);
        tvPhanTramTienTrinh = findViewById(R.id.tv_phan_tram_tien_trinh);
        rcvDanhSachCauHoiNghe = findViewById(R.id.rcv_danh_sach_cau_hoi_nghe);
        nutQuayLai = findViewById(R.id.nut_quay_lai);

        // Ánh xạ các thành phần Audio
        nutPhatNhac = findViewById(R.id.nut_phat_nhac);
        thanhTuaNhac = findViewById(R.id.thanh_tua_nhac);
        tvThoiLuong = findViewById(R.id.tv_thoi_luong);
    }

    private void caiDatDanhSachCauHoi() {
        CauHoiAdapter adapter = new CauHoiAdapter(danhSachCauHoi, this);
        rcvDanhSachCauHoiNghe.setLayoutManager(new LinearLayoutManager(this));
        rcvDanhSachCauHoiNghe.setAdapter(adapter);
    }

    private void chuyenSangTrangKetQua() {
        Intent intent = new Intent(BaiTapNgheActivity.this, KetQuaActivity.class);

        // Truyền số câu đã làm
        intent.putExtra(KetQuaActivity.EXTRA_CORRECT_ANSWERS, tapHopIdCauHoiDaTraLoi.size());
        intent.putExtra(KetQuaActivity.EXTRA_TOTAL_QUESTIONS, danhSachCauHoi.size());
        intent.putExtra(KetQuaActivity.EXTRA_TIME_SPENT, 0);

        // Gửi Topic và Level để nút "Làm lại"
        intent.putExtra(KetQuaActivity.EXTRA_TOPIC, "Nghe");
        intent.putExtra(KetQuaActivity.EXTRA_LEVEL, capDoHienTai);

        startActivity(intent);
        finish();
    }

    // Phần xử lý phát nhạc
    private void caiDatTrinhPhatNhac() {
        try {
            // Đảm bảo file audio_sample.mp3 có trong res/raw
            mayPhatNhac = MediaPlayer.create(this, R.raw.audio_sample);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (mayPhatNhac != null) {
            thanhTuaNhac.setMax(mayPhatNhac.getDuration());
            tvThoiLuong.setText(dinhDangThoiGian(mayPhatNhac.getDuration()));

            nutPhatNhac.setOnClickListener(v -> {
                if (dangPhat) tamDungNhac();
                else phatNhac();
            });

            thanhTuaNhac.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        mayPhatNhac.seekTo(progress);
                        tvThoiLuong.setText(dinhDangThoiGian(progress));
                    }
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });

            mayPhatNhac.setOnCompletionListener(mp -> {
                tamDungNhac();
                mayPhatNhac.seekTo(0);
                thanhTuaNhac.setProgress(0);
                tvThoiLuong.setText(dinhDangThoiGian(mayPhatNhac.getDuration()));
            });
        }
    }

    private void phatNhac() {
        if (mayPhatNhac != null) {
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

    // Runnable để cập nhật seekbar liên tục
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

    // --- LOGIC CẬP NHẬT TIẾN ĐỘ
    @Override
    public void khiDapAnDuocChon(int maCauHoi, String dapAnDuocChon) {
        // Logic: Nếu chọn thì thêm vào Set, bỏ chọn thì xóa khỏi Set
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

        if (soCauDaTraLoi == tongSoCau) {
            btnHoanThanh.setEnabled(true);
            btnHoanThanh.setVisibility(View.VISIBLE);
            // Thay đổi màu nút khi hoàn thành
            btnHoanThanh.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.royalBlue));
        } else {
            btnHoanThanh.setEnabled(false);
            btnHoanThanh.setVisibility(View.VISIBLE);
            btnHoanThanh.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.darker_gray));
        }
    }

    private List<CauHoiModel> taoCauHoiNghe() {
        return Arrays.asList(
                new CauHoiModel(1, "Instruction: Listen and choose.", "Where are they going?", Arrays.asList("Cinema", "Museum", "Park", "School"), "Museum"),
                new CauHoiModel(2, "Instruction: Listen to detail.", "What time is it?", Arrays.asList("7:00", "7:30", "8:00", "9:00"), "7:30"),
                new CauHoiModel(3, "Instruction: Inferring.", "How does the man feel?", Arrays.asList("Happy", "Sad", "Angry", "Tired"), "Happy")
        );
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