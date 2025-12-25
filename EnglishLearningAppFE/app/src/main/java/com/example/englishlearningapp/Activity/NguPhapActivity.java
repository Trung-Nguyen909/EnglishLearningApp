package com.example.englishlearningapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Adapter.NguPhapDetailAdapter;
import com.example.englishlearningapp.Model.NguPhap;
import com.example.englishlearningapp.ApiClient;
import com.example.englishlearningapp.R;
import com.example.englishlearningapp.Retrofit.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NguPhapActivity extends AppCompatActivity {

    private TextView tvHeaderTitle;
    private ImageView btnBack;
    private Button btnExercise;
    private RecyclerView rcvNguPhap;
    private NguPhapDetailAdapter adapter;

    // Biến lưu tên bài học
    private String tenBaiHoc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngu_phap);

        // 1. Ánh xạ View
        tvHeaderTitle = findViewById(R.id.tv_header_title);
        btnBack = findViewById(R.id.btn_back);
        btnExercise = findViewById(R.id.btn_start_exercise);
        rcvNguPhap = findViewById(R.id.rcv_ngu_phap_list);

        // 2. Setup RecyclerView
        rcvNguPhap.setLayoutManager(new LinearLayoutManager(this));

        // 3. Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        int idBaiHoc = intent.getIntExtra("BAIHOC_ID", -1);
        tenBaiHoc = intent.getStringExtra("TEN_BAI_HOC");

        // 4. Hiển thị tên bài học lên Header
        if (tenBaiHoc != null && !tenBaiHoc.isEmpty()) {
            tvHeaderTitle.setText(tenBaiHoc);
        } else {
            tvHeaderTitle.setText("Ngữ pháp");
        }

        // 5. Sự kiện Click
        btnBack.setOnClickListener(v -> finish());

        btnExercise.setOnClickListener(v -> {
            Intent i = new Intent(this, BaiHocActivity.class);
            i.putExtra("BAIHOC_ID", idBaiHoc);
            // Truyền tiếp tên bài học sang màn bài tập nếu cần
            i.putExtra("TEN_BAI_HOC", tenBaiHoc);
            Log.d("tenbaihoc", "onCreate: " + tenBaiHoc);
            startActivity(i);
        });

        // 6. Gọi API
        if (idBaiHoc != -1) {
            goiApiLayNguPhap(idBaiHoc);
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID bài học", Toast.LENGTH_SHORT).show();
        }
    }

    private void goiApiLayNguPhap(int idBaiHoc) {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        apiService.getListNguPhap(idBaiHoc).enqueue(new Callback<List<NguPhap>>() {
            @Override
            public void onResponse(Call<List<NguPhap>> call, Response<List<NguPhap>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NguPhap> danhSach = response.body();

                    // --- SỬA LẠI DÒNG NÀY ---
                    // Truyền thêm biến 'tenBaiHoc' vào Constructor để khớp với Adapter bạn đã sửa
                    adapter = new NguPhapDetailAdapter(NguPhapActivity.this, danhSach, tenBaiHoc);

                    rcvNguPhap.setAdapter(adapter);
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
}