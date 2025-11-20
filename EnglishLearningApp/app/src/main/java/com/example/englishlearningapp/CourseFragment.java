package com.example.englishlearningapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment; // Thay AppCompatActivity bằng Fragment
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Model.ChuDePhu;
import com.example.englishlearningapp.Model.ChuDe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CourseFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChuDeAdapter adapter;

    // Fragment sử dụng onCreateView thay vì onCreate
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 1. Thay thế setContentView bằng inflater.inflate
        // Vẫn dùng lại file layout cũ: activity_course
        View view = inflater.inflate(R.layout.activity_course, container, false);

        // 2. Ánh xạ View (Phải có biến "view." đứng trước findViewById)
        recyclerView = view.findViewById(R.id.recycler_view_topics);

        // 3. Logic khởi tạo dữ liệu giữ nguyên
        List<ChuDe> topics = createTopicData();

        // 4. Thiết lập Adapter
        // Lưu ý: Trong Fragment, dùng getContext() thay cho "this"
        adapter = new ChuDeAdapter(getContext(), topics);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    // --- Hàm tạo dữ liệu GIỮ NGUYÊN 100% như cũ ---
    private List<ChuDe> createTopicData() {
        List<ChuDe> list = new ArrayList<>();

        // 1. Family (Các ID: 1, 2, 3)
        List<ChuDePhu> familySubs = Arrays.asList(
                new ChuDePhu(1, "Family members", R.drawable.img_ic_family_member),
                new ChuDePhu(2, "Daily activities", R.drawable.img_ic_family_activityday),
                new ChuDePhu(3, "Relationship", R.drawable.img_ic_family_relative)
        );
        list.add(new ChuDe("Family", R.drawable.img_ic_family_course, familySubs));

        // 2. School Life (Các ID: 4, 5, 6)
        List<ChuDePhu> schoolSubs = Arrays.asList(
                new ChuDePhu(4, "Class", R.drawable.img_ic_family_course),
                new ChuDePhu(5, "Teacher", R.drawable.img_ic_family_course),
                new ChuDePhu(6, "Homework", R.drawable.img_ic_family_course)
        );
        list.add(new ChuDe("School Life", R.drawable.img_ic_schoollife_course, schoolSubs));

        // 3. Food & Drink (Các ID: 7, 8, 9)
        List<ChuDePhu> foodSubs = Arrays.asList(
                new ChuDePhu(7, "Fruits", R.drawable.img_ic_fruits),
                new ChuDePhu(8, "Vegetables", R.drawable.img_ic_family_course),
                new ChuDePhu(9, "Drinks", R.drawable.img_ic_family_course)
        );
        list.add(new ChuDe("Food & Drink", R.drawable.img_ic_fooddrink_course, foodSubs));

        // 4. House & Home
        List<ChuDePhu> homeSubs = Arrays.asList(
                new ChuDePhu(10, "Kitchen", R.drawable.img_ic_family_course),
                new ChuDePhu(11, "Bedroom", R.drawable.img_ic_family_course),
                new ChuDePhu(12, "Living Room", R.drawable.img_ic_family_course)
        );
        list.add(new ChuDe("House & Home", R.drawable.img_ic_househome_course, homeSubs));

        // 5. Shopping
        List<ChuDePhu> shoppingSubs = Arrays.asList(
                new ChuDePhu(13, "Clothes", R.drawable.img_ic_family_course),
                new ChuDePhu(14, "Shoes", R.drawable.img_ic_family_course),
                new ChuDePhu(15, "Accessories", R.drawable.img_ic_family_course)
        );
        list.add(new ChuDe("Shopping", R.drawable.img_ic_shopping_course, shoppingSubs));

        // 6. Animal
        List<ChuDePhu> animalSubs = Arrays.asList(
                new ChuDePhu(16, "Dog", R.drawable.img_ic_family_course),
                new ChuDePhu(17, "Cat", R.drawable.img_ic_family_course),
                new ChuDePhu(18, "Bird", R.drawable.img_ic_family_course)
        );
        list.add(new ChuDe("Animal", R.drawable.img_ic_animal_course, animalSubs));

        return list;
    }
}