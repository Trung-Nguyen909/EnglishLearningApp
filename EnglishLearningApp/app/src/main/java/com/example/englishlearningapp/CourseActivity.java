package com.example.englishlearningapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Model.SubItem;
import com.example.englishlearningapp.Model.Topic;

import java.util.ArrayList;
import java.util.Arrays; // Dùng Arrays.asList cho an toàn với mọi phiên bản Android
import java.util.List;

public class CourseActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TopicAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        recyclerView = findViewById(R.id.recycler_view_topics);

        List<Topic> topics = createTopicData();
        adapter = new TopicAdapter(this, topics);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<Topic> createTopicData() {
        List<Topic> list = new ArrayList<>();

        // 1. Family (Các ID: 1, 2, 3)
        // Lưu ý: Mình dùng Arrays.asList() thay cho List.of() để chạy được trên cả Android cũ
        List<SubItem> familySubs = Arrays.asList(
                new SubItem(1, "Family members", R.drawable.img_ic_family_member),
                new SubItem(2, "Daily activities", R.drawable.img_ic_family_activityday),
                new SubItem(3, "Relationship", R.drawable.img_ic_family_relative)
        );
        list.add(new Topic("Family", R.drawable.img_ic_family_course, familySubs));

        // 2. School Life (Các ID: 4, 5, 6)
        List<SubItem> schoolSubs = Arrays.asList(
                new SubItem(4, "Class", R.drawable.img_ic_family_course),
                new SubItem(5, "Teacher", R.drawable.img_ic_family_course),
                new SubItem(6, "Homework", R.drawable.img_ic_family_course)
        );
        list.add(new Topic("School Life", R.drawable.img_ic_schoollife_course, schoolSubs));

        // 3. Food & Drink (Các ID: 7, 8, 9)
        List<SubItem> foodSubs = Arrays.asList(
                new SubItem(7, "Fruits", R.drawable.img_ic_fruits),
                new SubItem(8, "Vegetables", R.drawable.img_ic_family_course),
                new SubItem(9, "Drinks", R.drawable.img_ic_family_course)
        );
        list.add(new Topic("Food & Drink", R.drawable.img_ic_fooddrink_course, foodSubs));

        // 4. House & Home
        List<SubItem> homeSubs = Arrays.asList(
                new SubItem(10, "Kitchen", R.drawable.img_ic_family_course),
                new SubItem(11, "Bedroom", R.drawable.img_ic_family_course),
                new SubItem(12, "Living Room", R.drawable.img_ic_family_course)
        );
        list.add(new Topic("House & Home", R.drawable.img_ic_househome_course, homeSubs));

        // 5. Shopping
        List<SubItem> shoppingSubs = Arrays.asList(
                new SubItem(13, "Clothes", R.drawable.img_ic_family_course),
                new SubItem(14, "Shoes", R.drawable.img_ic_family_course),
                new SubItem(15, "Accessories", R.drawable.img_ic_family_course)
        );
        list.add(new Topic("Shopping", R.drawable.img_ic_shopping_course, shoppingSubs));

        // 6. Animal
        List<SubItem> animalSubs = Arrays.asList(
                new SubItem(16, "Dog", R.drawable.img_ic_family_course),
                new SubItem(17, "Cat", R.drawable.img_ic_family_course),
                new SubItem(18, "Bird", R.drawable.img_ic_family_course)
        );
        list.add(new Topic("Animal", R.drawable.img_ic_animal_course, animalSubs));

        return list;
    }
}