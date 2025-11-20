package com.example.englishlearningapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WritingTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_writing_test);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.writing), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText etAnswer = findViewById(R.id.et_answer);
        TextView tvWordCount = findViewById(R.id.tv_word_count);
        Button btnNext = findViewById(R.id.btn_next);

        btnNext.setEnabled(false);

        etAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString().trim();
                int words = text.isEmpty() ? 0 : text.split("\\s+").length;

                // Cập nhật UI
                tvWordCount.setText("Word count: " + words + " / 50-100 words");

                if (words >= 50) {
                    // Trạng thái Active: Màu xanh
                    btnNext.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4169E1")));
                    btnNext.setTextColor(Color.WHITE);
                    btnNext.setEnabled(true);
                } else {
                    // Trạng thái Inactive: Màu xám
                    btnNext.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E0E0")));
                    btnNext.setTextColor(Color.parseColor("#888888"));
                    btnNext.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}