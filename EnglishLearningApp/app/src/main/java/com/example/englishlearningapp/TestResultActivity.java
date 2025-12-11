package com.example.englishlearningapp;


import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Model.QuestionResult;

import java.util.ArrayList;
import java.util.List;

public class TestResultActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvTitle;
    private TextView tvScorePercent;
    private TextView tvScore;
    private TextView tvTime;
    private TextView tvDate;
    private Button btnFilterAll;
    private Button btnFilterIncorrect;
    private Button btnFilterCorrect;
    private RecyclerView rcvQuestions;

    private QuestionResultAdapter adapter;
    private List<QuestionResult> allQuestions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_chitietbailam);
            initViews();
            setupData();
            setupRecyclerView();
            setupClickListeners();
        } catch (Exception e) {
            Log.e("TestResultActivity", "Error in onCreate: " + e.getMessage());
            e.printStackTrace();
            finish();
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

    private void setupData() {
        // Get data from Intent or ViewModel
        // For demo, using sample data
        allQuestions = new ArrayList<>();

        allQuestions.add(new QuestionResult(
                "Yesterday, I ______ to the new café downtown.",
                "went",
                "went",
                null
        ));

        allQuestions.add(new QuestionResult(
                "She ______ her keys on the bus this morning.",
                "losed",
                "lost",
                "The past tense of 'lose' is 'lost', not 'losed'."
        ));

        allQuestions.add(new QuestionResult(
                "We ______ a great movie last night.",
                "watched",
                "watched",
                null
        ));

        allQuestions.add(new QuestionResult(
                "They ______ to the party yesterday.",
                "goed",
                "went",
                "The past tense of 'go' is 'went', not 'goed'."
        ));

        // Calculate score
        int correctCount = 0;
        for (QuestionResult q : allQuestions) {
            if (q.getUserAnswer().equals(q.getCorrectAnswer())) {
                correctCount++;
            }
        }

        int totalCount = allQuestions.size();
        int percentage = (correctCount * 100) / totalCount;

        tvScorePercent.setText(percentage + "%");
        tvScore.setText(correctCount + "/" + totalCount);
        tvTime.setText("5:32"); // Get from intent
        tvDate.setText("Oct 12"); // Get from intent
    }

    private void setupRecyclerView() {
        adapter = new QuestionResultAdapter(allQuestions);
        rcvQuestions.setLayoutManager(new LinearLayoutManager(this));
        rcvQuestions.setAdapter(adapter);
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
        // Reset all buttons
        btnFilterAll.setBackgroundResource(R.drawable.bg_filter_unselected);
        btnFilterAll.setTextColor(ContextCompat.getColor(this, R.color.gray_400));

        btnFilterIncorrect.setBackgroundResource(R.drawable.bg_filter_unselected);
        btnFilterIncorrect.setTextColor(ContextCompat.getColor(this, R.color.gray_400));

        btnFilterCorrect.setBackgroundResource(R.drawable.bg_filter_unselected);
        btnFilterCorrect.setTextColor(ContextCompat.getColor(this, R.color.gray_400));

        // Set selected button
        selectedButton.setBackgroundResource(R.drawable.bg_filter_selected);
        selectedButton.setTextColor(ContextCompat.getColor(this, R.color.blue_primary));
    }
}
