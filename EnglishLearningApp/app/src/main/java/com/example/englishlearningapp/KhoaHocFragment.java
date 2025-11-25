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

import com.example.englishlearningapp.Model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KhoaHocFragment extends Fragment {

    private RecyclerView recyclerView;
    // 1. Khai báo biến cho nút "Kiểm tra"
    private View btn_TrangKiemTra;
    private ChuDeAdapter adapter;

    // Fragment sử dụng onCreateView thay vì onCreate
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 1. Thay thế setContentView bằng inflater.inflate
        // Vẫn dùng lại file layout cũ: activity_course
        View view = inflater.inflate(R.layout.activity_khoahoc, container, false);

        // 2. Ánh xạ View (Phải có biến "view." đứng trước findViewById)
        recyclerView = view.findViewById(R.id.rcv_chitiet);
        btn_TrangKiemTra = view.findViewById(R.id.btn_kiemtra);

        // 3. Logic khởi tạo dữ liệu giữ nguyên
        List<ChuDeModel> topics = createTopicData();

        // 4. Thiết lập Adapter
        // Lưu ý: Trong Fragment, dùng getContext() thay cho "this"
        adapter = new ChuDeAdapter(getContext(), topics);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // 3. SỰ KIỆN CLICK: Chuyển sang màn hình Kiểm tra (TestFragment)
        if (btn_TrangKiemTra != null) {
            btn_TrangKiemTra.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Lệnh chuyển Fragment
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                    // Thay thế màn hình hiện tại bằng TestFragment
                    // R.id.frame_container là cái khung trong MainActivity
                    transaction.replace(R.id.frame_container, new KiemTraFragment());

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
                new ChuDePhuModel(1, "Thành viên trong gia đình", R.drawable.img_ic_family_member),
                new ChuDePhuModel(2, "Hoạt động hằng ngày", R.drawable.img_ic_family_activityday),
                new ChuDePhuModel(3, "Họ hàng", R.drawable.img_ic_family_relative)
        );
        list.add(new ChuDeModel("Gia đình", R.drawable.img_ic_family_course, familySubs));

        // 2. School Life (Các ID: 4, 5, 6)
        List<ChuDePhuModel> schoolSubs = Arrays.asList(
                new ChuDePhuModel(4, "Lớp học", R.drawable.img_ic_family_course),
                new ChuDePhuModel(5, "Giáo viên", R.drawable.img_ic_family_course),
                new ChuDePhuModel(6, "Bài tập về nhà", R.drawable.img_ic_family_course)
        );
        list.add(new ChuDeModel("Cuộc sống học đường", R.drawable.img_ic_schoollife_course, schoolSubs));

        // 3. Food & Drink (Các ID: 7, 8, 9)
        List<ChuDePhuModel> foodSubs = Arrays.asList(
                new ChuDePhuModel(7, "Trái cây", R.drawable.img_ic_fruits),
                new ChuDePhuModel(8, "Rau củ", R.drawable.img_ic_family_course),
                new ChuDePhuModel(9, "Đồ ăn nhanh", R.drawable.img_ic_family_course)
        );
        list.add(new ChuDeModel("Thức ăn & Đồ uống", R.drawable.img_ic_fooddrink_course, foodSubs));

        // 4. House & Home
        List<ChuDePhuModel> homeSubs = Arrays.asList(
                new ChuDePhuModel(10, "Phòng bếp", R.drawable.img_ic_family_course),
                new ChuDePhuModel(11, "Phòng tắm", R.drawable.img_ic_family_course),
                new ChuDePhuModel(12, "Phòng khách", R.drawable.img_ic_family_course)
        );
        list.add(new ChuDeModel("Nhà của tôi", R.drawable.img_ic_househome_course, homeSubs));

        // 5. Shopping
        List<ChuDePhuModel> shoppingSubs = Arrays.asList(
                new ChuDePhuModel(13, "Áo quần", R.drawable.img_ic_family_course),
                new ChuDePhuModel(14, "Giày", R.drawable.img_ic_family_course),
                new ChuDePhuModel(15, "Phụ kiện", R.drawable.img_ic_family_course)
        );
        list.add(new ChuDeModel("Mua sắm", R.drawable.img_ic_shopping_course, shoppingSubs));

        // 6. Animal
        List<ChuDePhuModel> animalSubs = Arrays.asList(
                new ChuDePhuModel(16, "Lớp thú", R.drawable.img_ic_family_course),
                new ChuDePhuModel(17, "Lớp chim", R.drawable.img_ic_family_course),
                new ChuDePhuModel(18, "Lớp bò sát", R.drawable.img_ic_family_course)
        );
        list.add(new ChuDeModel("Động vật", R.drawable.img_ic_animal_course, animalSubs));

        return list;
    }
}