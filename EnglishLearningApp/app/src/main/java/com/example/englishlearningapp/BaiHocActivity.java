package com.example.englishlearningapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.englishlearningapp.Model.BaiHocModel;
import java.util.ArrayList;
import java.util.List;

public class BaiHocActivity extends AppCompatActivity {
    private RecyclerView rcvDanhSachBaiHoc;
    private BaiHocAdapter adapterBaiHoc;
    private List<BaiHocModel> danhSachBaiHoc;
    private TextView tvTieuDeKhoaHoc;
    private ImageView nutQuayLai;

    // Định nghĩa màu sắc
    private static final int MAU_TU_VUNG = R.color.orange_500;
    private static final int MAU_NGU_PHAP = R.color.green_500;
    private static final int MAU_LUYEN_NOI = R.color.purple_500;
    private static final int MAU_LUYEN_NGHE = R.color.red_500;
    private static final int MAU_LUYEN_DOC = R.color.yellow_500;
    private static final int MAU_LUYEN_VIET = R.color.blue_500;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bai_hoc);

        // Ánh xạ View (ID giữ nguyên theo file XML)
        rcvDanhSachBaiHoc = findViewById(R.id.rcv_danh_sach_bai_hoc);
        tvTieuDeKhoaHoc = findViewById(R.id.tv_tieu_de_khoa_hoc);
        nutQuayLai = findViewById(R.id.nut_quay_lai);

        rcvDanhSachBaiHoc.setLayoutManager(new LinearLayoutManager(this));

        // --- NHẬN DỮ LIỆU ---
        Intent intent = getIntent();
        int maChuDeCon = -1;
        String tenChuDeCon = "Bài học";

        if (intent != null) {
            maChuDeCon = intent.getIntExtra("SUB_ITEM_ID", -1);
            tenChuDeCon = intent.getStringExtra("SUB_ITEM_NAME");
        }

        if (tenChuDeCon != null) tvTieuDeKhoaHoc.setText(tenChuDeCon);

        // --- TẠO DỮ LIỆU ---
        danhSachBaiHoc = new ArrayList<>();
        taiDuLieuTheoIdChuDe(maChuDeCon, tenChuDeCon);

        // Khởi tạo Adapter
        adapterBaiHoc = new BaiHocAdapter(this, danhSachBaiHoc);

        // --- XỬ LÝ SỰ KIỆN CLICK (GIỮ NGUYÊN LOGIC CŨ) ---
        adapterBaiHoc.datSuKienClick(new BaiHocAdapter.SuKienClickItem() {
            @Override
            public void khiAnVaoItem(BaiHocModel baiHoc) {
                // Giữ nguyên logic: Chuyển sang ExerciseDetailActivity
                Intent intentChiTiet = new Intent(BaiHocActivity.this, ChiTietBaiTapActivity.class);

                // Gửi dữ liệu sang trang chi tiết
                intentChiTiet.putExtra("TITLE", baiHoc.getTieuDe());
                intentChiTiet.putExtra("TYPE", baiHoc.getLoaiBaiHoc());
                intentChiTiet.putExtra("LEVEL", baiHoc.getCapDo());
                intentChiTiet.putExtra("TIME", baiHoc.getThoiGian());

                startActivity(intentChiTiet);
            }
        });

        rcvDanhSachBaiHoc.setAdapter(adapterBaiHoc);

        // Sự kiện nút Quay lại
        nutQuayLai.setOnClickListener(v -> finish());
    }

    // --- CÁC HÀM TẠO DỮ LIỆU

    private void taiDuLieuTheoIdChuDe(int id, String tenMacDinh) {
        switch (id) {
            case 1: // Family Members
                themBoBaiHocDayDu(
                        "Các thành viên trong gia đình",
                        "Sở hữu cách (Possessive's)",
                        "Giới thiệu về gia đình bạn",
                        "Đoạn hội thoại hàng ngày",
                        "Truyền thống gia đình",
                        "Viết thư kể về gia đình"
                );
                break;

            case 2: // Daily Activities
                themBoBaiHocDayDu(
                        "Hoạt động buổi sáng",
                        "Thì hiện tại đơn",
                        "Hỏi giờ giấc",
                        "Lịch trình xe buýt",
                        "Nhật ký của Anna",
                        "Kế hoạch cuối tuần"
                );
                break;

            case 3: // Relationship
                themBoBaiHocDayDu(
                        "Mối quan hệ trong gia đình",
                        "Thì hiện tại tiếp diễn",
                        "Chào hỏi trong gia đình",
                        "Hội thoại trong gia đình",
                        "Giới thiệu về gia đình",
                        "Thành viên trong gia đình của bạn"
                );
                break;

            default:
                themBaiHocChung(tenMacDinh);
                break;
        }
    }

    private void themBoBaiHocDayDu(String tieuDeTuVung, String tieuDeNguPhap, String tieuDeNoi,
                                   String tieuDeNghe, String tieuDeDoc, String tieuDeViet) {

        danhSachBaiHoc.add(new BaiHocModel("Từ vựng", tieuDeTuVung, "Cơ bản", "10 phút", MAU_TU_VUNG));
        danhSachBaiHoc.add(new BaiHocModel("Ngữ pháp", tieuDeNguPhap, "Cơ bản", "12 phút", MAU_NGU_PHAP));
        danhSachBaiHoc.add(new BaiHocModel("Luyện nói", tieuDeNoi, "Trung cấp", "15 phút", MAU_LUYEN_NOI));
        danhSachBaiHoc.add(new BaiHocModel("Luyện nghe", tieuDeNghe, "Trung cấp", "15 phút", MAU_LUYEN_NGHE));
        danhSachBaiHoc.add(new BaiHocModel("Luyện đọc", tieuDeDoc, "Nâng cao", "20 phút", MAU_LUYEN_DOC));
        danhSachBaiHoc.add(new BaiHocModel("Luyện viết", tieuDeViet, "Trung cấp", "18 phút", MAU_LUYEN_VIET));
    }

    private void themBaiHocChung(String tenChuDe) {
        danhSachBaiHoc.add(new BaiHocModel("Từ vựng", "Từ vựng về " + tenChuDe, "Cơ bản", "10 phút", MAU_TU_VUNG));
        danhSachBaiHoc.add(new BaiHocModel("Ngữ pháp", "Ngữ pháp liên quan", "Cơ bản", "12 phút", MAU_NGU_PHAP));
        danhSachBaiHoc.add(new BaiHocModel("Luyện nói", "Luyện nói về " + tenChuDe, "Trung cấp", "15 phút", MAU_LUYEN_NOI));
        danhSachBaiHoc.add(new BaiHocModel("Luyện nghe", "Bài nghe chủ đề " + tenChuDe, "Trung cấp", "15 phút", MAU_LUYEN_NGHE));
        danhSachBaiHoc.add(new BaiHocModel("Luyện đọc", "Bài đọc hiểu", "Nâng cao", "20 phút", MAU_LUYEN_DOC));
        danhSachBaiHoc.add(new BaiHocModel("Luyện viết", "Bài tập viết", "Trung cấp", "18 phút", MAU_LUYEN_VIET));
    }
}