package com.example.englishlearningapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.ApiClient;
import com.example.englishlearningapp.DTO.Response.TuVungResponse;
import com.example.englishlearningapp.R;
import com.example.englishlearningapp.Retrofit.ApiService;
import com.example.englishlearningapp.Adapter.TuVungAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TuVungActivity extends AppCompatActivity {

    public static final String EXTRA_BAIHOC_ID = "BAIHOC_ID";
    // Thêm hằng số để nhận và truyền tên bài học
    public static final String EXTRA_TEN_BAI_HOC = "TEN_BAI_HOC";

    private RecyclerView rv;
    private ProgressBar loading;
    private TuVungAdapter adapter;
    private Button btnContinue;
    private ApiService apiService;

    // View Header mới
    private ImageView btnBack;
    private TextView tvHeaderTitle;

    // Nếu API trả filename audio, set base url cho audio
    private final String AUDIO_BASE = "https://your.cdn.or.server/audio/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu_vung);

        // 1. Ánh xạ các View
        rv = findViewById(R.id.rvTuVung);
        loading = findViewById(R.id.progress_loading);
        btnContinue = findViewById(R.id.btnContinueGrammar);

        // Ánh xạ Header mới (Thay thế Toolbar cũ)
        btnBack = findViewById(R.id.btn_back);
        tvHeaderTitle = findViewById(R.id.tv_header_title);

        // 2. Setup RecyclerView
        rv.setLayoutManager(new LinearLayoutManager(this));

        // 3. Khởi tạo API
        apiService = ApiClient.getClient(this).create(ApiService.class);

        // 4. Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        int idBaihoc = intent.getIntExtra(EXTRA_BAIHOC_ID, -1);
        String tenBaiHoc = intent.getStringExtra(EXTRA_TEN_BAI_HOC);

        // Validation ID
        if (idBaihoc == -1) {
            Toast.makeText(this, "ID bài học không hợp lệ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 5. Xử lý giao diện Header
        // Xử lý nút Back
        btnBack.setOnClickListener(v -> finish());

        // (Tùy chọn) Nếu bạn muốn hiển thị tên bài học thay vì chữ "Từ Vựng" cố định:
        // if (tenBaiHoc != null && !tenBaiHoc.isEmpty()) {
        //     tvHeaderTitle.setText(tenBaiHoc);
        // }

        // 6. Tải dữ liệu
        loadTuVung(idBaihoc);

        // 7. Xử lý nút Tiếp tục (Chuyển sang Ngữ Pháp)
        btnContinue.setOnClickListener(v -> {
            Intent i = new Intent(TuVungActivity.this, NguPhapActivity.class);
            i.putExtra("BAIHOC_ID", idBaihoc);
            // Quan trọng: Truyền tiếp tên bài học sang màn Ngữ pháp để hiển thị Header bên đó
            if (tenBaiHoc != null) {
                i.putExtra("TEN_BAI_HOC", tenBaiHoc);
            }
            startActivity(i);
        });
    }

    private void loadTuVung(int idBaihoc) {
        loading.setVisibility(View.VISIBLE);
        apiService.getTuVungByBaihocID(idBaihoc).enqueue(new Callback<List<TuVungResponse>>() {
            @Override
            public void onResponse(Call<List<TuVungResponse>> call, Response<List<TuVungResponse>> response) {
                loading.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<TuVungResponse> list = response.body();
                    adapter = new TuVungAdapter(TuVungActivity.this, list, AUDIO_BASE);
                    rv.setAdapter(adapter);
                } else {
                    Toast.makeText(TuVungActivity.this, "Không tải được từ vựng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TuVungResponse>> call, Throwable t) {
                loading.setVisibility(View.GONE);
                Toast.makeText(TuVungActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) adapter.cleanup();
    }
}