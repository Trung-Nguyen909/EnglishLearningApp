package com.example.englishlearningapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.englishlearningapp.Model.BaiTapModel;
import java.util.ArrayList;
import java.util.List;

public class LichSuLamBaiActivity extends AppCompatActivity {
    private RecyclerView recyclerViewLichSu;
    private LichSuLamBaiAdapter adapter;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lichsulambai);

        // Initialize views
        recyclerViewLichSu = findViewById(R.id.recyclerViewLichSu);
        btnBack = findViewById(R.id.btnBack);

        // Set up RecyclerView
        recyclerViewLichSu.setLayoutManager(new LinearLayoutManager(this));

        // Create sample data
        List<BaiTapModel> baiTapList = createSampleData();

        // Set adapter
        adapter = new LichSuLamBaiAdapter(this, baiTapList);
        recyclerViewLichSu.setAdapter(adapter);

        // Handle back button
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private List<BaiTapModel> createSampleData() {
        List<BaiTapModel> list = new ArrayList<>();

        // Item 1: Vocabulary - 100% Correct
        list.add(new BaiTapModel(
            R.drawable.ic_thanhviengiadinh,
            "Từ Vựng: Các Thành Viên Gia Đình",
            "20 tháng 5, 2024",
            "100% Đúng",
            R.color.colorSuccess,
            false
        ));

        // Item 2: Grammar Test - 92% Passed
        list.add(new BaiTapModel(
            R.drawable.ic_books,
            "Bài Kiểm Tra Ngữ Pháp: Thì Quá Khứ",
            "18 tháng 5, 2024",
            "92% Vượt Qua",
            R.color.colorSuccess,
            false
        ));

        // Item 3: Listening - 85% Correct
        list.add(new BaiTapModel(
            R.drawable.ic_nghe,
            "Nghe Hiểu: Tại Sân Bay",
            "17 tháng 5, 2024",
            "85% Đúng",
            R.color.colorSuccess,
            false
        ));

        // Item 4: Speaking - Try Again
        list.add(new BaiTapModel(
            R.drawable.ic_noi,
            "Nói: Đặt Hàng Thức Ăn",
            "15 tháng 5, 2024",
            "Hãy Thử Lại",
            R.color.colorError,
            true
        ));

        // Item 5: Reading - Completed
        list.add(new BaiTapModel(
            R.drawable.ic_sach,
            "Đọc: Một Câu Chuyện Ngắn",
            "14 tháng 5, 2024",
            "Hoàn Thành",
            R.color.colorCompleted,
            false
        ));

        return list;
    }
}

