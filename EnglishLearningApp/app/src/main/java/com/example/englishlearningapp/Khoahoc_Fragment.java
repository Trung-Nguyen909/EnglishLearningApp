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

import com.example.englishlearningapp.Model.ChuDePhuModel;
import com.example.englishlearningapp.Model.ChuDeModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Khoahoc_Fragment extends Fragment {

    private RecyclerView recyclerView;
    // 1. Khai báo biến cho nút "Kiểm tra"
    private View btnTestTab;
    private ChuDeAdapter adapter;

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
        List<ChuDeModel> topics = createTopicData();

        // 4. Thiết lập Adapter
        // Lưu ý: Trong Fragment, dùng getContext() thay cho "this"
        adapter = new ChuDeAdapter(getContext(), topics);

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
    private List<ChuDeModel> createTopicData() {
        List<ChuDeModel> list = new ArrayList<>();

        // 1. Family (Các ID: 1, 2, 3)
        List<ChuDePhuModel> familySubs = Arrays.asList(
                new ChuDePhuModel(1, "Family members", R.drawable.img_ic_family_member),
                new ChuDePhuModel(2, "Daily activities", R.drawable.img_ic_family_activityday),
                new ChuDePhuModel(3, "Relationship", R.drawable.img_ic_family_relative)
        );
        list.add(new ChuDeModel("Family", R.drawable.img_ic_family_course, familySubs));

        // 2. School Life (Các ID: 4, 5, 6)
        List<ChuDePhuModel> schoolSubs = Arrays.asList(
                new ChuDePhuModel(4, "Class", R.drawable.img_ic_family_course),
                new ChuDePhuModel(5, "Teacher", R.drawable.img_ic_family_course),
                new ChuDePhuModel(6, "Homework", R.drawable.img_ic_family_course)
        );
        list.add(new ChuDeModel("School Life", R.drawable.img_ic_schoollife_course, schoolSubs));

        // 3. Food & Drink (Các ID: 7, 8, 9)
        List<ChuDePhuModel> foodSubs = Arrays.asList(
                new ChuDePhuModel(7, "Fruits", R.drawable.img_ic_fruits),
                new ChuDePhuModel(8, "Vegetables", R.drawable.img_ic_family_course),
                new ChuDePhuModel(9, "Drinks", R.drawable.img_ic_family_course)
        );
        list.add(new ChuDeModel("Food & Drink", R.drawable.img_ic_fooddrink_course, foodSubs));

        // 4. House & Home
        List<ChuDePhuModel> homeSubs = Arrays.asList(
                new ChuDePhuModel(10, "Kitchen", R.drawable.img_ic_family_course),
                new ChuDePhuModel(11, "Bedroom", R.drawable.img_ic_family_course),
                new ChuDePhuModel(12, "Living Room", R.drawable.img_ic_family_course)
        );
        list.add(new ChuDeModel("House & Home", R.drawable.img_ic_househome_course, homeSubs));

        // 5. Shopping
        List<ChuDePhuModel> shoppingSubs = Arrays.asList(
                new ChuDePhuModel(13, "Clothes", R.drawable.img_ic_family_course),
                new ChuDePhuModel(14, "Shoes", R.drawable.img_ic_family_course),
                new ChuDePhuModel(15, "Accessories", R.drawable.img_ic_family_course)
        );
        list.add(new ChuDeModel("Shopping", R.drawable.img_ic_shopping_course, shoppingSubs));

        // 6. Animal
        List<ChuDePhuModel> animalSubs = Arrays.asList(
                new ChuDePhuModel(16, "Dog", R.drawable.img_ic_family_course),
                new ChuDePhuModel(17, "Cat", R.drawable.img_ic_family_course),
                new ChuDePhuModel(18, "Bird", R.drawable.img_ic_family_course)
        );
        list.add(new ChuDeModel("Animal", R.drawable.img_ic_animal_course, animalSubs));

        return list;
    }
}