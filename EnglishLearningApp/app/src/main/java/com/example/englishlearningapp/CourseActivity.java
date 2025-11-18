package com.example.englishlearningapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
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

        // 1. Family
        List<SubItem> familySubs = List.of(
                new SubItem("Father", R.drawable.img_ic_family_course),
                new SubItem("Mother", R.drawable.img_ic_family_course),
                new SubItem("Child", R.drawable.img_ic_family_course)
        );
        list.add(new Topic("Family", R.drawable.img_ic_family_course, familySubs));

        // 2. School Life
        List<SubItem> schoolSubs = List.of(
                new SubItem("Class", R.drawable.img_ic_family_course),
                new SubItem("Teacher", R.drawable.img_ic_family_course),
                new SubItem("Homework", R.drawable.img_ic_family_course)
        );
        list.add(new Topic("School Life", R.drawable.img_ic_schoollife_course, schoolSubs));

        // 3. Food & Drink
        List<SubItem> foodSubs = List.of(
                new SubItem("Fruits", R.drawable.img_ic_fruits),
                new SubItem("Vegetables", R.drawable.img_ic_family_course),
                new SubItem("Drinks", R.drawable.img_ic_family_course)
        );
        list.add(new Topic("Food & Drink", R.drawable.img_ic_fooddrink_course, foodSubs));

        // 4. House & Home
        List<SubItem> homeSubs = List.of(
                new SubItem("Kitchen", R.drawable.img_ic_family_course),
                new SubItem("Bedroom", R.drawable.img_ic_family_course),
                new SubItem("Living Room", R.drawable.img_ic_family_course)
        );
        list.add(new Topic("House & Home", R.drawable.img_ic_househome_course, homeSubs));

        // 5. Shopping
        List<SubItem> shoppingSubs = List.of(
                new SubItem("Clothes", R.drawable.img_ic_family_course),
                new SubItem("Shoes", R.drawable.img_ic_family_course),
                new SubItem("Accessories", R.drawable.img_ic_family_course)
        );
        list.add(new Topic("Shopping", R.drawable.img_ic_shopping_course, shoppingSubs));

        // 6. Animal
        List<SubItem> animalSubs = List.of(
                new SubItem("Dog", R.drawable.img_ic_family_course),
                new SubItem("Cat", R.drawable.img_ic_family_course),
                new SubItem("Bird", R.drawable.img_ic_family_course)
        );
        list.add(new Topic("Animal", R.drawable.img_ic_animal_course, animalSubs));

        return list;
    }
}
