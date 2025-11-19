package com.example.englishlearningapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Model.Topic;

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
                new SubItem("Thành viên", R.drawable.img_ic_family_member),
                new SubItem("Hoạt động hằng ngày", R.drawable.img_ic_family_activityday),
                new SubItem("Họ hàng", R.drawable.img_ic_family_relative)
        );
        list.add(new Topic("Gia dình", R.drawable.img_ic_family_course, familySubs));

        // 2. School Life
        List<SubItem> schoolSubs = List.of(
                new SubItem("Lớp học", R.drawable.img_ic_family_course),
                new SubItem("Giáo viên", R.drawable.img_ic_family_course),
                new SubItem("Bài tập về nhà", R.drawable.img_ic_family_course)
        );
        list.add(new Topic("Cuộc sống học đường", R.drawable.img_ic_schoollife_course, schoolSubs));

        // 3. Food & Drink
        List<SubItem> foodSubs = List.of(
                new SubItem("Nước ép", R.drawable.img_ic_fruits),
                new SubItem("Rau củ", R.drawable.img_ic_family_course),
                new SubItem("Đồ ăn nhanh", R.drawable.img_ic_family_course)
        );
        list.add(new Topic("Thức ăn & Đồ uống", R.drawable.img_ic_fooddrink_course, foodSubs));

        // 4. House & Home
        List<SubItem> homeSubs = List.of(
                new SubItem("Phòng bếp", R.drawable.img_ic_family_course),
                new SubItem("Phòng ngủ", R.drawable.img_ic_family_course),
                new SubItem("Phòng tắm", R.drawable.img_ic_family_course)
        );
        list.add(new Topic("Nhà", R.drawable.img_ic_househome_course, homeSubs));

        // 5. Shopping
        List<SubItem> shoppingSubs = List.of(
                new SubItem("Áo quần", R.drawable.img_ic_family_course),
                new SubItem("Giày", R.drawable.img_ic_family_course),
                new SubItem("Phụ kiện", R.drawable.img_ic_family_course)
        );
        list.add(new Topic("Mua sắm", R.drawable.img_ic_shopping_course, shoppingSubs));

        // 6. Animal
        List<SubItem> animalSubs = List.of(
                new SubItem("Chó", R.drawable.img_ic_family_course),
                new SubItem("Mèo", R.drawable.img_ic_family_course),
                new SubItem("Chim", R.drawable.img_ic_family_course)
        );
        list.add(new Topic("Động vật", R.drawable.img_ic_animal_course, animalSubs));

        return list;
    }
}
