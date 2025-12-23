package com.example.englishlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.DTO.Response.TuVungResponse;
import com.example.englishlearningapp.Retrofit.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LessonVocabularyActivity extends AppCompatActivity {

    public static final String EXTRA_BAIHOC_ID = "ID_BAIHOC";
    private RecyclerView rv;
    private ProgressBar loading;
    private TuVungAdapter adapter;
    private Button btnContinue;
    private ApiService apiService;

    // nếu API trả filename audio, set base url cho audio
    private final String AUDIO_BASE = "https://your.cdn.or.server/audio/"; // <- chỉnh

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_vocabulary);

        rv = findViewById(R.id.rvTuVung);
        loading = findViewById(R.id.progress_loading);
        btnContinue = findViewById(R.id.btnContinueGrammar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

// dùng icon của bạn
        toolbar.setNavigationIcon(R.drawable.ic_muitentrang);

// xử lý khi bấm mũi tên
        toolbar.setNavigationOnClickListener(v -> finish());

// (tùy chọn) nếu vẫn muốn hiển thị title màu trắng từ ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        rv.setLayoutManager(new LinearLayoutManager(this));
        apiService = ApiClient.getClient(this).create(ApiService.class);

        int idBaihoc = getIntent().getIntExtra(EXTRA_BAIHOC_ID, -1);
        if (idBaihoc == -1) {
            Toast.makeText(this, "ID bài học không hợp lệ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadTuVung(idBaihoc);

//        btnContinue.setOnClickListener(v -> {
//            // Chuyển sang màn Grammar (ví dụ GrammarActivity)
//            Intent i = new Intent(LessonVocabularyActivity.this, GrammarActivity.class);
//            i.putExtra(GrammarActivity.EXTRA_BAIHOC_ID, idBaihoc);
//            startActivity(i);
//        });
    }

    private void loadTuVung(int idBaihoc) {
        loading.setVisibility(View.VISIBLE);
        apiService.getTuVungByBaihocID(idBaihoc).enqueue(new Callback<List<TuVungResponse>>() {
            @Override
            public void onResponse(Call<List<TuVungResponse>> call, Response<List<TuVungResponse>> response) {
                loading.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    List<TuVungResponse> list = response.body();
                    adapter = new TuVungAdapter(LessonVocabularyActivity.this, list, AUDIO_BASE);
                    rv.setAdapter(adapter);
                } else {
                    Toast.makeText(LessonVocabularyActivity.this, "Không tải được từ vựng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TuVungResponse>> call, Throwable t) {
                loading.setVisibility(View.GONE);
                Toast.makeText(LessonVocabularyActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) adapter.cleanup();
    }
}
