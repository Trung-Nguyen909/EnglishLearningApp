package com.example.englishlearningapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.ApiClient;
import com.example.englishlearningapp.DTO.Response.LichSuBaiTapResponse;
import com.example.englishlearningapp.Adapter.LichSuLamBaiAdapter;
import com.example.englishlearningapp.Model.BaiTapModel;
import com.example.englishlearningapp.R;
import com.example.englishlearningapp.Retrofit.ApiService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LichSuLamBaiActivity extends AppCompatActivity {
    private RecyclerView recyclerViewLichSu;
    private LichSuLamBaiAdapter adapter;
    private ImageView btnBack;
    private List<BaiTapModel> baiTapList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lichsulambai);

        // Ánh xạ view
        recyclerViewLichSu = findViewById(R.id.recyclerViewLichSu);
        btnBack = findViewById(R.id.btnBack);

        // Setup RecyclerView
        recyclerViewLichSu.setLayoutManager(new LinearLayoutManager(this));
        baiTapList = new ArrayList<>();
        adapter = new LichSuLamBaiAdapter(this, baiTapList);
        recyclerViewLichSu.setAdapter(adapter);

        // Gọi API
        getHistoryFromApi();

        // --- XỬ LÝ CLICK ---
        adapter.setOnItemClickListener((baiTap, position) -> {
            Intent intent = new Intent(LichSuLamBaiActivity.this, ChiTietLichSuActivity.class);

            intent.putExtra("idLichSuBaiLam", baiTap.getId());

            // Truyền thêm thời gian làm (giây) qua màn hình kết quả
            intent.putExtra("tgianLam", baiTap.getTgianLam());

            startActivity(intent);
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void getHistoryFromApi() {
        ApiService apiService = ApiClient.getClient(this).create(ApiService.class);
        apiService.getLichSuHocTap().enqueue(new Callback<List<LichSuBaiTapResponse>>() {
            @Override
            public void onResponse(Call<List<LichSuBaiTapResponse>> call, Response<List<LichSuBaiTapResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mapDataToView(response.body());
                } else {
                    Toast.makeText(LichSuLamBaiActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<LichSuBaiTapResponse>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi: " + t.getMessage());
            }
        });
    }

    private void mapDataToView(List<LichSuBaiTapResponse> apiList) {
        baiTapList.clear();

        for (LichSuBaiTapResponse item : apiList) {
            // Xử lý icon
            int iconRes = (item.getLoaiBai() != null && "TEST".equals(item.getLoaiBai()))
                    ? R.drawable.ic_books : R.drawable.ic_sach;

            // Xử lý điểm & màu sắc
            boolean isPassed = item.getDiemSo() >= 5.0;
            int colorRes = isPassed ? R.color.colorSuccess : R.color.colorError;
            String statusText = String.format(Locale.US, "%.1f điểm - %s", item.getDiemSo(), item.getTrangThai());

            // Xử lý ngày
            String dateStr = formatDate(item.getTgianNopBai());

            baiTapList.add(new BaiTapModel(
                    item.getId(),
                    iconRes,
                    item.getTenBai(),
                    dateStr,
                    statusText,
                    colorRes,
                    !isPassed,
                    item.getTgianLam()
                    ));
        }
        adapter.notifyDataSetChanged();
    }

    private String formatDate(String jsonDate) {
        if (jsonDate == null) return "";
        try {
            SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date date = sourceFormat.parse(jsonDate);
            SimpleDateFormat destFormat = new SimpleDateFormat("dd 'tháng' MM, yyyy", new Locale("vi", "VN"));
            return destFormat.format(date);
        } catch (ParseException e) {
            return jsonDate;
        }
    }
}