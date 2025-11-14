package com.example.englishlearningapp; // Thay thế bằng package của bạn

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CourseActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TopicAdapter adapter;
    private List<Topic> topicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course); // Layout chứa RecyclerView

        recyclerView = findViewById(R.id.recycler_view_topics);

        // 1. Chuẩn bị dữ liệu mẫu
        topicList = createTopicData();

        // 2. Thiết lập Adapter
        adapter = new TopicAdapter(this, topicList);
        recyclerView.setAdapter(adapter);

        // 3. Thiết lập Layout Manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // Hàm tạo dữ liệu mẫu
    private List<Topic> createTopicData() {
        List<Topic> list = new ArrayList<>();

        // Ghi chú: Thay thế R.drawable.xxx bằng tài nguyên icon thực tế
        list.add(new Topic("Family", R.drawable.img_ic_family_course));
        list.add(new Topic("School Life", R.drawable.img_ic_schoollife_course));
        list.add(new Topic("Food & Drink", R.drawable.img_ic_fooddrink_course));
        list.add(new Topic("House & Home", R.drawable.img_ic_househome_course));
        list.add(new Topic("Shopping", R.drawable.img_ic_shopping_course));
        list.add(new Topic("Animal", R.drawable.img_ic_animal_course));

        return list;
    }
}