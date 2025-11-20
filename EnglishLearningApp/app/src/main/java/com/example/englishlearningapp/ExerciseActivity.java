package com.example.englishlearningapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Model.Lesson;

import java.util.ArrayList;
import java.util.List;

public class ExerciseActivity extends AppCompatActivity {

    private RecyclerView lessonsRecyclerView;
    private LessonAdapter lessonAdapter;
    private List<Lesson> lessonList;

    // Định nghĩa các màu sắc (đưa vào colors.xml)
    private static final int COLOR_VOCABULARY = R.color.orange_500; // #FF9800
    private static final int COLOR_GRAMMAR = R.color.green_500;    // #4CAF50
    private static final int COLOR_SPEAKING = R.color.purple_500;  // #9C27B0
    private static final int COLOR_LISTENING = R.color.red_500;    // #F44336
    private static final int COLOR_READING = R.color.yellow_500;   // #FFEB3B
    private static final int COLOR_WRITING = R.color.blue_500;     // #2196F3


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        lessonsRecyclerView = findViewById(R.id.lessons_recycler_view);

        // Cấu hình RecyclerView
        lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo dữ liệu
        prepareLessonData();

        // Thiết lập Adapter
        lessonAdapter = new LessonAdapter(this, lessonList);
        lessonsRecyclerView.setAdapter(lessonAdapter);
    }

    private void prepareLessonData() {
        lessonList = new ArrayList<>();

        // Dữ liệu từ ảnh (sử dụng màu đã định nghĩa ở trên)
        lessonList.add(new Lesson("Vocabulary", "Family Members", "Người mới", "10 phút", COLOR_VOCABULARY));
        lessonList.add(new Lesson("Grammar", "Possessive's", "Người mới", "12 min", COLOR_GRAMMAR));
        lessonList.add(new Lesson("Speaking", "Talking about Family", "Intermediate", "15 min", COLOR_SPEAKING));
        lessonList.add(new Lesson("Listening", "Family Daily Conversations", "Intermediate", "15 min", COLOR_LISTENING));
        lessonList.add(new Lesson("Reading", "Family Traditions", "Advanced", "20 min", COLOR_READING));
        lessonList.add(new Lesson("Writing", "Describe Your Family", "Intermediate", "18 min", COLOR_WRITING));
    }
}