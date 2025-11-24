package com.example.englishlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Model.CauHoiModel;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BaiTapDocActivity extends AppCompatActivity implements CauHoiAdapter.LangNgheSuKienChonDapAn {

    private List<CauHoiModel> danhSachCauHoi;
    private Button btnHoanThanh;
    private ProgressBar thanhTienTrinh;
    private TextView tvDemSoCauHoi;
    private TextView tvPhanTramTienTrinh;
    private RecyclerView rcvDanhSachCauHoi;
    private ImageView nutQuayLai;

    // Logic theo dõi
    private Set<Integer> tapHopIdCauHoiDaTraLoi = new HashSet<>();

    private String capDoHienTai = "Basic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_tap_doc);

        // 1. NHẬN LEVEL TỪ MÀN HÌNH TRƯỚC
        if (getIntent() != null) {
            String levelNhanDuoc = getIntent().getStringExtra("SELECTED_LEVEL");
            if (levelNhanDuoc != null) {
                capDoHienTai = levelNhanDuoc;
            }
        }

        // 2. Khởi tạo dữ liệu
        danhSachCauHoi = taoCauHoi();

        // 3. Ánh xạ Views
        rcvDanhSachCauHoi = findViewById(R.id.rcv_danh_sach_cau_hoi);
        nutQuayLai = findViewById(R.id.nut_quay_lai);

        btnHoanThanh = findViewById(R.id.btn_hoan_thanh);
        thanhTienTrinh = findViewById(R.id.thanh_tien_trinh);
        tvDemSoCauHoi = findViewById(R.id.tv_dem_so_cau_hoi);
        tvPhanTramTienTrinh = findViewById(R.id.tv_phan_tram_tien_trinh);

        // 4. Thiết lập RecyclerView
        CauHoiAdapter adapter = new CauHoiAdapter(danhSachCauHoi, this);
        rcvDanhSachCauHoi.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );
        rcvDanhSachCauHoi.setAdapter(adapter);

        // 5. Thiết lập trạng thái ban đầu
        thanhTienTrinh.setMax(danhSachCauHoi.size());
        capNhatTrangThaiTienTrinh();

        // Xử lý sự kiện Back
        nutQuayLai.setOnClickListener(v -> finish());

        // Xử lý nút hoàn thành
        btnHoanThanh.setOnClickListener(v -> {
            chuyenSangTrangKetQua();
        });
    }

    private void chuyenSangTrangKetQua() {
        Intent intent = new Intent(BaiTapDocActivity.this, KetQuaActivity.class);

        // Truyền số câu đã làm
        intent.putExtra(KetQuaActivity.EXTRA_CORRECT_ANSWERS, tapHopIdCauHoiDaTraLoi.size());
        intent.putExtra(KetQuaActivity.EXTRA_TOTAL_QUESTIONS, danhSachCauHoi.size());
        intent.putExtra(KetQuaActivity.EXTRA_TIME_SPENT, 0); // Thời gian tùy chọn

        // Gửi Topic và Level để biết đường quay lại
        intent.putExtra(KetQuaActivity.EXTRA_TOPIC, "Reading");
        intent.putExtra(KetQuaActivity.EXTRA_LEVEL, capDoHienTai);

        startActivity(intent);
        finish();
    }

    // Override hàm từ Interface LangNgheSuKienChonDapAn
    @Override
    public void khiDapAnDuocChon(int maCauHoi, String dapAnDuocChon) {
        // Cập nhật Set theo dõi
        if (dapAnDuocChon != null) {
            tapHopIdCauHoiDaTraLoi.add(maCauHoi);
        } else {
            tapHopIdCauHoiDaTraLoi.remove(maCauHoi);
        }

        capNhatTrangThaiTienTrinh();
    }

    /**
     * Cập nhật ProgressBar, các TextView và trạng thái nút Hoàn thành.
     */
    private void capNhatTrangThaiTienTrinh() {
        int soCauDaTraLoi = tapHopIdCauHoiDaTraLoi.size();
        int tongSoCau = danhSachCauHoi.size();

        // Tính phần trăm
        int phanTram = (tongSoCau > 0) ? (int) (((float) soCauDaTraLoi / (float) tongSoCau) * 100) : 0;

        // 1. Cập nhật TextViews và ProgressBar
        tvDemSoCauHoi.setText("Câu " + soCauDaTraLoi + "/" + tongSoCau);
        tvPhanTramTienTrinh.setText(phanTram + "%");
        thanhTienTrinh.setProgress(soCauDaTraLoi);

        // 2. Điều khiển nút Hoàn thành
        if (soCauDaTraLoi == tongSoCau) {
            btnHoanThanh.setEnabled(true);
            btnHoanThanh.setVisibility(View.VISIBLE);
            btnHoanThanh.setBackgroundTintList(getColorStateList(R.color.royalBlue));
        } else {
            btnHoanThanh.setEnabled(false);
            btnHoanThanh.setVisibility(View.VISIBLE);
            btnHoanThanh.setBackgroundTintList(getColorStateList(android.R.color.darker_gray));
        }
    }

    private List<CauHoiModel> taoCauHoi() {
        return Arrays.asList(
                new CauHoiModel(1, "Choose the correct word to complete the sentence:", "She ____ to the store yesterday.", Arrays.asList("go", "goes", "went", "going"), "went"),
                new CauHoiModel(2, "Vocabulary: Choose the word with the opposite meaning.", "The word 'difficult' is the opposite of ____.", Arrays.asList("hard", "easy", "long", "bad"), "easy"),
                new CauHoiModel(3, "Grammar: Present Continuous tense", "She ____ dinner right now.", Arrays.asList("cooks", "is cooking", "cook", "was cooking"), "is cooking"),
                new CauHoiModel(4, "Communication: What is the polite way to ask for help?", "You say: ____", Arrays.asList("Help me!", "Give me that!", "Could you help me, please?", "You must help me!"), "Could you help me, please?"),
                new CauHoiModel(5, "Vocabulary: Choose the correct meaning.", "The word 'improve' means ____.", Arrays.asList("làm tệ hơn", "cải thiện", "phá hỏng", "bắt đầu"), "cải thiện"),
                new CauHoiModel(6, "Grammar: Choose the correct past tense form.", "They ____ a movie last night.", Arrays.asList("watch", "watched", "watching", "watches"), "watched"),
                new CauHoiModel(7, "Pronunciation: Which word has a different vowel sound?", "Choose the odd one out.", Arrays.asList("beat", "seat", "great", "heat"), "great"),
                new CauHoiModel(8, "Vocabulary: Choose the correct preposition.", "I am interested ____ learning English.", Arrays.asList("on", "in", "with", "to"), "in"),
                new CauHoiModel(9, "Reading: Choose the best answer.", "‘I usually study English in the evening.’ What does ‘usually’ mean?", Arrays.asList("luôn luôn", "thỉnh thoảng", "thường xuyên", "hiếm khi"), "thường xuyên"),
                new CauHoiModel(10, "Grammar: Articles", "She bought ____ umbrella yesterday.", Arrays.asList("a", "an", "the", "no article"), "an")
        );
    }
}