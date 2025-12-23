package com.example.englishlearningapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.DTO.Response.BaiTap; // nếu class tên khác, đổi lại
import com.example.englishlearningapp.Model.BaiHocModel;
import com.example.englishlearningapp.Retrofit.ApiService;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private static final String TAG = "BAIHOC_ACTIVITY";

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

        // --- NHẬN DỮ LIỆU TỪ INTENT ---
        Intent intent = getIntent();
        int maBaiHoc = -1;
        String tenChuDeCon = "Bài học";

        if (intent != null) {
            maBaiHoc = intent.getIntExtra("SUB_ITEM_ID", -1);
            String tmp = intent.getStringExtra("SUB_ITEM_NAME");
            if (tmp != null) tenChuDeCon = tmp;
        }

        if (tenChuDeCon != null) tvTieuDeKhoaHoc.setText(tenChuDeCon);

        // --- KHỞI TẠO ADAPTER VỚI LIST RỖNG ---
        danhSachBaiHoc = new ArrayList<>();
        adapterBaiHoc = new BaiHocAdapter(this, danhSachBaiHoc);

        // Gán sự kiện click (giữ nguyên logic cũ)
        adapterBaiHoc.datSuKienClick(new BaiHocAdapter.SuKienClickItem() {
            @Override
            public void khiAnVaoItem(BaiHocModel baiHoc) {
                Intent intentChiTiet = new Intent(BaiHocActivity.this, ChiTietBaiTapActivity.class);
                intentChiTiet.putExtra("BAIHOC_ID", baiHoc.getId());
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

        // Nếu SUB_ITEM_ID hợp lệ -> gọi API lấy bài tập theo id bài học
        if (maBaiHoc > 0) {
            fetchBaiTapByBaiHocId(maBaiHoc);
        } else {
            // fallback: dùng dữ liệu cũ (local) nếu id không hợp lệ
            taiDuLieuTheoIdChuDe(maBaiHoc, tenChuDeCon);
            adapterBaiHoc.notifyDataSetChanged();
        }
    }

    // Gọi API lấy danh sách Bài tập của Bài học
    private void fetchBaiTapByBaiHocId(int idBaiHoc) {
        ApiService service = ApiClient.getClient(this).create(ApiService.class);
        Log.d(TAG, "Gọi API getBaiTapByBaiHocId with id=" + idBaiHoc);
        Call<List<BaiTap>> call = service.getBaiTapByBaiHocId(idBaiHoc);
        call.enqueue(new Callback<List<BaiTap>>() {
            @Override
            public void onResponse(Call<List<BaiTap>> call, Response<List<BaiTap>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BaiTap> apiList = response.body();
                    Log.d(TAG, "Số bài tập trả về: " + apiList.size());
                    List<BaiHocModel> mapped = new ArrayList<>();
                    for (BaiTap b : apiList) {
                        int mau = MAU_TU_VUNG;
                        if (b.getLoaiBaiTap() != null) {
                            String lower = b.getLoaiBaiTap().toLowerCase();
                            if (lower.contains("ngữ") || lower.contains("grammar")) mau = MAU_NGU_PHAP;
                            else if (lower.contains("nói") || lower.contains("speaking")) mau = MAU_LUYEN_NOI;
                            else if (lower.contains("nghe") || lower.contains("listening")) mau = MAU_LUYEN_NGHE;
                            else if (lower.contains("đọc") || lower.contains("reading")) mau = MAU_LUYEN_DOC;
                            else if (lower.contains("viết") || lower.contains("writing")) mau = MAU_LUYEN_VIET;
                        }

                        mapped.add(new BaiHocModel(
                                b.getId(),
                                b.getLoaiBaiTap() != null ? b.getLoaiBaiTap() : "Từ vựng",
                                b.getTenBaiTap() != null ? b.getTenBaiTap() : "Bài tập",
                                b.getCapdo() != null ? b.getCapdo() : "Cơ bản",
                                b.getThoigian() != null ? b.getThoigian() : "5 phút",
                                mau
                        ));
                    }


                    // cập nhật UI
                    runOnUiThread(() -> {
                        danhSachBaiHoc.clear();
                        danhSachBaiHoc.addAll(mapped);
                        adapterBaiHoc.notifyDataSetChanged();
                    });

                } else {
                    Log.w(TAG, "Lấy bài tập thất bại, code=" + response.code());
                    runOnUiThread(() -> {
                        Toast.makeText(BaiHocActivity.this, "Không lấy được bài tập (code " + response.code() + ")", Toast.LENGTH_SHORT).show();
                        // fallback local sample
                        taiDuLieuTheoIdChuDe(idBaiHoc, tvTieuDeKhoaHoc.getText().toString());
                        adapterBaiHoc.notifyDataSetChanged();
                    });
                }
            }

            @Override
            public void onFailure(Call<List<BaiTap>> call, Throwable t) {
                Log.e(TAG, "Lỗi khi gọi API bài tập", t);
                runOnUiThread(() -> {
                    Toast.makeText(BaiHocActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    // fallback local sample
                    taiDuLieuTheoIdChuDe(idBaiHoc, tvTieuDeKhoaHoc.getText().toString());
                    adapterBaiHoc.notifyDataSetChanged();
                });
            }
        });
    }

    // Reflection helper: cố gọi các method theo danh sách tên, trả về String hoặc null
    private String safeGetString(Object obj, String... methodNames) {
        if (obj == null) return null;
        for (String mName : methodNames) {
            try {
                Method m = obj.getClass().getMethod(mName);
                Object val = m.invoke(obj);
                if (val != null) return String.valueOf(val);
            } catch (NoSuchMethodException ignored) {
            } catch (Exception e) {
                Log.w(TAG, "safeGetString failed for " + mName, e);
            }
        }
        return null;
    }

    // --- Các hàm tạo dữ liệu cục bộ cũ (giữ nguyên để fallback) ---
    private void taiDuLieuTheoIdChuDe(int id, String tenMacDinh) {
        danhSachBaiHoc.clear();
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

    private void themBoBaiHocDayDu(String tieuDeTuVung, String tieuDeNguPhap,
                                   String tieuDeNoi, String tieuDeNghe,
                                   String tieuDeDoc, String tieuDeViet) {

        danhSachBaiHoc.add(new BaiHocModel(1, "Từ vựng", tieuDeTuVung, "Cơ bản", "00:10:00", MAU_TU_VUNG));
        danhSachBaiHoc.add(new BaiHocModel(2, "Ngữ pháp", tieuDeNguPhap, "Cơ bản", "00:12:00", MAU_NGU_PHAP));
        danhSachBaiHoc.add(new BaiHocModel(3, "Luyện nói", tieuDeNoi, "Trung cấp", "00:15:00", MAU_LUYEN_NOI));
        danhSachBaiHoc.add(new BaiHocModel(4, "Luyện nghe", tieuDeNghe, "Trung cấp", "00:15:00", MAU_LUYEN_NGHE));
        danhSachBaiHoc.add(new BaiHocModel(5, "Luyện đọc", tieuDeDoc, "Nâng cao", "00:20:00", MAU_LUYEN_DOC));
        danhSachBaiHoc.add(new BaiHocModel(6, "Luyện viết", tieuDeViet, "Trung cấp", "00:18:00", MAU_LUYEN_VIET));
    }


    private void themBaiHocChung(String tenChuDe) {

        danhSachBaiHoc.add(new BaiHocModel(1, "Từ vựng", "Từ vựng về " + tenChuDe,
                "Cơ bản", "00:10:00", MAU_TU_VUNG));

        danhSachBaiHoc.add(new BaiHocModel(2, "Ngữ pháp", "Ngữ pháp liên quan",
                "Cơ bản", "00:12:00", MAU_NGU_PHAP));

        danhSachBaiHoc.add(new BaiHocModel(3, "Luyện nói", "Luyện nói về " + tenChuDe,
                "Trung cấp", "00:15:00", MAU_LUYEN_NOI));

        danhSachBaiHoc.add(new BaiHocModel(4, "Luyện nghe", "Bài nghe chủ đề " + tenChuDe,
                "Trung cấp", "00:15:00", MAU_LUYEN_NGHE));

        danhSachBaiHoc.add(new BaiHocModel(5, "Luyện đọc", "Bài đọc hiểu",
                "Nâng cao", "00:20:00", MAU_LUYEN_DOC));

        danhSachBaiHoc.add(new BaiHocModel(6, "Luyện viết", "Bài tập viết",
                "Trung cấp", "00:18:00", MAU_LUYEN_VIET));
    }

}
