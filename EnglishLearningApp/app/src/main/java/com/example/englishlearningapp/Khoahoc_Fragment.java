package com.example.englishlearningapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment; // Thay AppCompatActivity bằng Fragment
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Model.SubItem;
import com.example.englishlearningapp.Model.Topic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Khoahoc_Fragment extends Fragment {

    private RecyclerView recyclerView;
    private TopicAdapter adapter;
    // 1. Khai báo biến cho nút "Kiểm tra"
    private View btnTestTab;

    // Fragment sử dụng onCreateView thay vì onCreate
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 1. Thay thế setContentView bằng inflater.inflate
        // Vẫn dùng lại file layout cũ: activity_course
        View view = inflater.inflate(R.layout.activity_khoahoc, container, false);

        // 2. Ánh xạ View (Phải có biến "view." đứng trước findViewById)
        recyclerView = view.findViewById(R.id.recycler_view_detai);
        btnTestTab = view.findViewById(R.id.btn_kiemtra);

        // 3. Logic khởi tạo dữ liệu giữ nguyên
        List<Topic> topics = createTopicData();

        // 4. Thiết lập Adapter
        // Lưu ý: Trong Fragment, dùng getContext() thay cho "this"
        adapter = new TopicAdapter(getContext(), topics);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // 3. SỰ KIỆN CLICK: Chuyển sang màn hình Kiểm tra (TestFragment)
        if (btnTestTab != null) {
            btnTestTab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Lệnh chuyển Fragment
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                    // Thay thế màn hình hiện tại bằng TestFragment
                    // R.id.frame_container là cái khung trong MainActivity
                    transaction.replace(R.id.frame_container, new Kiemtra_Fragment());

                    // Cho phép bấm nút Back để quay lại màn hình Khóa học
                    transaction.addToBackStack(null);

                    // Thực hiện
                    transaction.commit();
                }
            });
        }

        return view;
    }

    // --- Hàm tạo dữ liệu GIỮ NGUYÊN 100% như cũ ---
    private List<Topic> createTopicData() {
        List<Topic> list = new ArrayList<>();

        // 1. Family (Các ID: 1, 2, 3)
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