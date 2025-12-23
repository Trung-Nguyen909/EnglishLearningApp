package com.example.englishlearningapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.englishlearningapp.Model.NguPhap; // Kiểm tra lại đường dẫn import này
import com.example.englishlearningapp.Retrofit.ApiService; // Kiểm tra lại đường dẫn import này

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NguPhapActivity extends AppCompatActivity {

    // Khai báo các View trong layout activity_ngu_phap_detail.xml
    private TextView tvHeaderTitle, tvTenNguPhap, tvMoTaNgan, tvGiaiThichChiTiet, tvCongThuc, tvMeoGhiNho;
    private LinearLayout layoutExamplesContainer;
    private ImageView btnBack;
    private Button btnexcsesi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // SỬA: Dùng layout mới activity_ngu_phap_detail
        setContentView(R.layout.activity_ngu_phap);
        btnexcsesi = findViewById(R.id.btn_start_exercise);
        anhXaView();

        // Xử lý nút Back
        btnBack.setOnClickListener(v -> finish());

        // Nhận ID bài học
        int idBaiHoc = getIntent().getIntExtra("BAIHOC_ID", -1);

        if (idBaiHoc != -1) {
            goiApiLayNguPhap(idBaiHoc);
        } else {
            Toast.makeText(this, "Không tìm thấy ID bài học", Toast.LENGTH_SHORT).show();
        }
        btnexcsesi.setOnClickListener(v -> {
            // Chuyển sang màn Grammar (ví dụ GrammarActivity)
            Intent i = new Intent(this, BaiHocActivity.class);
            i.putExtra("BAIHOC_ID", idBaiHoc);
            startActivity(i);
        });
    }

    private void anhXaView() {
        tvHeaderTitle = findViewById(R.id.tv_header_title);
        tvTenNguPhap = findViewById(R.id.tv_ten_ngu_phap);
        tvMoTaNgan = findViewById(R.id.tv_mo_ta_ngan);
        tvGiaiThichChiTiet = findViewById(R.id.tv_giai_thich_chi_tiet);
        tvCongThuc = findViewById(R.id.tv_cong_thuc);
        tvMeoGhiNho = findViewById(R.id.tv_meo_ghi_nho);
        layoutExamplesContainer = findViewById(R.id.layout_examples_container);
        btnBack = findViewById(R.id.btn_back);
    }

    private void goiApiLayNguPhap(int idBaiHoc) {
        // Gọi API
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);

        apiService.getListNguPhap(idBaiHoc).enqueue(new Callback<List<NguPhap>>() {
            @Override
            public void onResponse(Call<List<NguPhap>> call, Response<List<NguPhap>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    // Giả sử lấy bài ngữ pháp ĐẦU TIÊN trong danh sách để hiển thị chi tiết
                    // (Nếu muốn hiển thị danh sách để chọn, ta phải làm thêm trang danh sách trước)
                    NguPhap nguPhap = response.body().get(0);

                    hienThiDuLieu(nguPhap);
                } else {
                    Toast.makeText(NguPhapActivity.this, "Không có dữ liệu ngữ pháp", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NguPhap>> call, Throwable t) {
                Toast.makeText(NguPhapActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hienThiDuLieu(NguPhap np) {
        tvHeaderTitle.setText(np.getTenNguPhap());
        tvTenNguPhap.setText(np.getTenNguPhap());

        // Giả sử field 'giaiThich' trong DB chứa cả mô tả ngắn và dài, ta tạm hiển thị chung
        tvGiaiThichChiTiet.setText(np.getGiaiThich());

        // Nếu DB chưa có trường 'congThuc', tạm set cứng hoặc ẩn đi
        // tvCongThuc.setText(np.getCongThuc());

        // Xử lý hiển thị Ví dụ (Tách chuỗi nếu có nhiều ví dụ ngăn cách bởi xuống dòng)
        if (np.getViDu() != null) {
            String[] viDus = np.getViDu().split("\n"); // Tách ví dụ theo dòng

            layoutExamplesContainer.removeAllViews(); // Xóa ví dụ mẫu trong XML

            for (String vidu : viDus) {
                themDongViDu(vidu);
            }
        }
    }

    // Hàm tạo động TextView ví dụ có icon tròn xanh
    private void themDongViDu(String noiDung) {
        View viewDongViDu = getLayoutInflater().inflate(R.layout.item_vidu_dong, null);
        TextView tvNoiDung = viewDongViDu.findViewById(R.id.tv_noi_dung_vi_du);

        // Xử lý HTML nếu có (để tô màu chữ)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvNoiDung.setText(Html.fromHtml(noiDung, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvNoiDung.setText(Html.fromHtml(noiDung));
        }

        layoutExamplesContainer.addView(viewDongViDu);
    }
}