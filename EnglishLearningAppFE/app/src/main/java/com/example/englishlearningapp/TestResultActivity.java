package com.example.englishlearningapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.DTO.Response.ChiTietBaitapResponse;
import com.example.englishlearningapp.Model.QuestionResult;
import com.example.englishlearningapp.Retrofit.ApiService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestResultActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvTitle, tvScorePercent, tvScore, tvTime, tvDate;
    private Button btnFilterAll, btnFilterIncorrect, btnFilterCorrect;
    private RecyclerView rcvQuestions;

    private QuestionResultAdapter adapter;
    private List<QuestionResult> allQuestions;

    private int idLichSuBaiLam;
    private int tgianLamGiay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_chitietbailam);

            // 1. Nhận dữ liệu từ Intent
            idLichSuBaiLam = getIntent().getIntExtra("idLichSuBaiLam", -1);
            tgianLamGiay = getIntent().getIntExtra("tgianLam", 0);

            initViews();
            setupRecyclerView();
            setupClickListeners();

            // 2. Gọi API nếu có ID hợp lệ
            if (idLichSuBaiLam != -1) {
                getTestDetailFromApi(idLichSuBaiLam);
            } else {
                Toast.makeText(this, "Không tìm thấy bài làm", Toast.LENGTH_SHORT).show();
            }

            // Hiển thị thời gian và ngày (Ngày hiện tại hoặc truyền từ intent nếu muốn)
            setupHeaderInfo();

        } catch (Exception e) {
            Log.e("TestResultActivity", "Error in onCreate: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        tvTitle = findViewById(R.id.tv_title);
        tvScorePercent = findViewById(R.id.tv_score_percent);
        tvScore = findViewById(R.id.tv_score);
        tvTime = findViewById(R.id.tv_time);
        tvDate = findViewById(R.id.tv_date);
        btnFilterAll = findViewById(R.id.btn_filter_all);
        btnFilterIncorrect = findViewById(R.id.btn_filter_incorrect);
        btnFilterCorrect = findViewById(R.id.btn_filter_correct);
        rcvQuestions = findViewById(R.id.rcv_questions);
    }

    private void setupHeaderInfo() {
        // Format thời gian làm bài (Giây -> mm:ss)
        int phut = tgianLamGiay / 60;
        int giay = tgianLamGiay % 60;
        tvTime.setText(String.format(Locale.US, "%02d:%02d", phut, giay));

        // Format ngày hiện tại (hoặc bạn có thể truyền ngày từ Intent cũng được)
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM", Locale.getDefault());
        tvDate.setText(sdf.format(new Date()));
    }

    private void setupRecyclerView() {
        allQuestions = new ArrayList<>();
        adapter = new QuestionResultAdapter(allQuestions);
        rcvQuestions.setLayoutManager(new LinearLayoutManager(this));
        rcvQuestions.setAdapter(adapter);
    }

    private void getTestDetailFromApi(int id) {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        apiService.getChiTietBaiLam(id).enqueue(new Callback<List<ChiTietBaitapResponse>>() {
            @Override
            public void onResponse(Call<List<ChiTietBaitapResponse>> call, Response<List<ChiTietBaitapResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    processApiData(response.body());
                } else {
                    Toast.makeText(TestResultActivity.this, "Lỗi tải chi tiết", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChiTietBaitapResponse>> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
                Toast.makeText(TestResultActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm xử lý dữ liệu API và convert sang Model hiển thị
    private void processApiData(List<ChiTietBaitapResponse> apiList) {
        allQuestions.clear();
        int correctCount = 0;
        Gson gson = new Gson();

        for (ChiTietBaitapResponse item : apiList) {
            String questionText = item.getNoiDung();
            String explanation = item.getGiaiThich();

            // Xử lý JSON đáp án
            // item.getDapAnDung() trả về: {"A": "Sister", "B": "Mother", ..., "Correct": "B"}
            String correctAnswerText = "";
            String userAnswerText = "";

            try {
                JsonObject jsonOptions = gson.fromJson(item.getDapAnDung(), JsonObject.class);

                // 1. Lấy đáp án đúng (Key -> Text)
                if (jsonOptions.has("Correct")) {
                    String correctKey = jsonOptions.get("Correct").getAsString(); // "B"
                    if (jsonOptions.has(correctKey)) {
                        correctAnswerText = jsonOptions.get(correctKey).getAsString(); // "Mother"
                    }
                }

                // 2. Lấy đáp án người dùng chọn (Key -> Text)
                String userKey = item.getDapAnNguoiDung(); // Ví dụ: "B" hoặc null
                if (userKey != null && jsonOptions.has(userKey)) {
                    userAnswerText = jsonOptions.get(userKey).getAsString(); // "Mother"
                } else {
                    userAnswerText = "Không trả lời"; // Hoặc xử lý khi null
                }

            } catch (Exception e) {
                Log.e("JSON_PARSE", "Lỗi parse json đáp án: " + e.getMessage());
                correctAnswerText = "Lỗi hiển thị";
                userAnswerText = item.getDapAnNguoiDung();
            }

            // Thêm vào list hiển thị
            allQuestions.add(new QuestionResult(
                    questionText,
                    userAnswerText,
                    correctAnswerText,
                    explanation
            ));

            if (item.isDungSai()) {
                correctCount++;
            }
        }

        // Cập nhật UI Adapter
        adapter.notifyDataSetChanged();

        // Cập nhật UI Điểm số
        updateScoreUI(correctCount, apiList.size());
    }

    private void updateScoreUI(int correct, int total) {
        tvScore.setText(correct + "/" + total);

        int percent = (total > 0) ? (correct * 100 / total) : 0;
        tvScorePercent.setText(percent + "%");
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnFilterAll.setOnClickListener(v -> {
            selectFilter(btnFilterAll);
            adapter.filterAll(allQuestions);
        });

        btnFilterIncorrect.setOnClickListener(v -> {
            selectFilter(btnFilterIncorrect);
            adapter.filterIncorrect(allQuestions);
        });

        btnFilterCorrect.setOnClickListener(v -> {
            selectFilter(btnFilterCorrect);
            adapter.filterCorrect(allQuestions);
        });
    }

    private void selectFilter(Button selectedButton) {
        // Reset buttons style
        resetButtonStyle(btnFilterAll);
        resetButtonStyle(btnFilterIncorrect);
        resetButtonStyle(btnFilterCorrect);

        // Set selected style
        selectedButton.setBackgroundResource(R.drawable.bg_filter_selected);
        selectedButton.setTextColor(ContextCompat.getColor(this, R.color.blue_primary)); // Đảm bảo màu này có trong colors.xml hoặc dùng mã Hex #2563EB
    }

    private void resetButtonStyle(Button btn) {
        btn.setBackgroundResource(R.drawable.bg_filter_unselected);
        btn.setTextColor(ContextCompat.getColor(this, R.color.gray_400)); // Đảm bảo màu #9CA3AF
    }
}