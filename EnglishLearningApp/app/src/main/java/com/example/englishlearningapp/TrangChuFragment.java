package com.example.englishlearningapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Retrofit.ApiService;
import com.example.englishlearningapp.TrangChu.EventDecorator;
import com.example.englishlearningapp.Model.KyNangModel;
import com.example.englishlearningapp.DTO.Response.NhatKyHoatDong; // Import Model
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
// Nhớ import class tạo Retrofit của bạn (ví dụ: APIClient hoặc RetrofitClient)
// import com.example.englishlearningapp.API.APIClient;

public class TrangChuFragment extends Fragment {

    private RecyclerView rcvKiemTraNhanh;
    private KyNangAdapter adapterKyNang;
    private AppCompatButton btnTiepTucHoc;
    private MaterialCalendarView calendarView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_trang_chu, container, false);

        // 1. Ánh xạ View
        rcvKiemTraNhanh = view.findViewById(R.id.rcv_kiem_tra_nhanh);
        btnTiepTucHoc = view.findViewById(R.id.btn_tiep_tuc_hoc);
        calendarView = view.findViewById(R.id.calendarView);

        // 2. Setup Calendar cơ bản
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
        // Set ngày hiện tại để lịch focus vào tháng hiện tại
        calendarView.setCurrentDate(CalendarDay.today());

        // 3. GỌI API LẤY LỊCH (Thay thế cho hàm fake data cũ)
        // Giả sử UserId = 1 (Bạn cần lấy ID thật từ SharedPreferences hoặc lúc Login)
        int userId = 1;
        callApiLayLichHoatDong(userId);

        // --- CODE CŨ GIỮ NGUYÊN (XỬ LÝ NÚT VÀ QUICK TEST) ---
        if (btnTiepTucHoc != null) {
            btnTiepTucHoc.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), BaiHocActivity.class);
                intent.putExtra("SUB_ITEM_ID", 1);
                intent.putExtra("SUB_ITEM_NAME", "Thành viên trong gia đình");
                startActivity(intent);
            });
        }

        List<KyNangModel> danhSachKyNang = new ArrayList<>();
        danhSachKyNang.add(new KyNangModel("Nghe", R.drawable.ic_nghe));
        danhSachKyNang.add(new KyNangModel("Nói", R.drawable.ic_noi));
        danhSachKyNang.add(new KyNangModel("Đọc", R.drawable.ic_doc));
        danhSachKyNang.add(new KyNangModel("Viết", R.drawable.ic_viet));

        adapterKyNang = new KyNangAdapter(getContext(), danhSachKyNang);
        adapterKyNang.setLangNgheSuKienClick(new KyNangAdapter.LangNgheSuKienClick() {
            @Override
            public void khiClickVaoItem(KyNangModel kyNang) {
                // Lấy tên kỹ năng (Listening, Reading, Speaking, Writing)
                String tenKyNang = kyNang.getTenKyNang();

                // 1. Tạo Fragment
                KiemTraFragment kiemtraFragment = new KiemTraFragment();

                // 2. Đóng gói dữ liệu để gửi sang Fragment kia
                Bundle goiDuLieu = new Bundle();
                goiDuLieu.putString("TEN_CHU_DE", tenKyNang);
                kiemtraFragment.setArguments(goiDuLieu);

                // --- THÊM DÒNG NÀY ĐỂ BÁO HIỆU ---
                goiDuLieu.putBoolean("TU_TRANG_CHU", true);

                // 3. Thực hiện chuyển đổi Fragment
                if (getParentFragmentManager() != null) {
                    getParentFragmentManager().beginTransaction()
                            .replace(R.id.frame_container, kiemtraFragment) // Thay thế layout hiện tại
                            .addToBackStack(null) // Cho phép ấn Back để quay lại
                            .commit();
                }
            }
        });

        GridLayoutManager gridManagerKyNang = new GridLayoutManager(getContext(), 4);
        rcvKiemTraNhanh.setLayoutManager(gridManagerKyNang);
        rcvKiemTraNhanh.setAdapter(adapterKyNang);

        return view;
    }

    private void callApiLayLichHoatDong(int userId) {
        SharedPreferences prefs = getActivity().getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE);
        String token = prefs.getString("TOKEN", null);

        if (token == null) {
            return;
        }

        ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        Call<List<NhatKyHoatDong>> call = apiService.getUserActivityLog();

        call.enqueue(new Callback<List<NhatKyHoatDong>>() {
            @Override
            public void onResponse(Call<List<NhatKyHoatDong>> call, Response<List<NhatKyHoatDong>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Xử lý hiển thị lịch như cũ
                    xuLyHienThiLich(response.body());
                } else {
                    // Token hết hạn hoặc lỗi
                    if (response.code() == 401 || response.code() == 403) {
                        Toast.makeText(getContext(), "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
                        // Có thể intent về trang Login tại đây
                    }
                }
            }

            @Override
            public void onFailure(Call<List<NhatKyHoatDong>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi: " + t.getMessage());
            }
        });
    }

    // --- HÀM XỬ LÝ LOGIC TÔ MÀU (Đã viết ở bước trước) ---
    private void xuLyHienThiLich(List<NhatKyHoatDong> listNhatKyTuApi) {
        if (getActivity() == null) return;

        // 1. Đưa list ngày API vào HashSet
        HashSet<String> tapHopNgayDaHoc = new HashSet<>();
        for (NhatKyHoatDong item : listNhatKyTuApi) {
            tapHopNgayDaHoc.add(item.getNgayHoatDong()); // Giả sử format "2025-12-07"
        }

        List<CalendarDay> listXanh = new ArrayList<>();
        List<CalendarDay> listDo = new ArrayList<>();
        List<CalendarDay> listXam = new ArrayList<>();

        CalendarDay homNay = CalendarDay.today();
        int namHienTai = homNay.getYear();
        int thangHienTai = homNay.getMonth();
        int ngayHienTai = homNay.getDay();

        // 2. Duyệt từ ngày 1 đến ngày hôm nay
        for (int i = 1; i <= ngayHienTai; i++) {
            // Tạo chuỗi check "yyyy-MM-dd"
            String ngayCheck = namHienTai + "-" + String.format("%02d", thangHienTai) + "-" + String.format("%02d", i);
            CalendarDay checkDay = CalendarDay.from(namHienTai, thangHienTai, i);

            if (tapHopNgayDaHoc.contains(ngayCheck)) {
                listXanh.add(checkDay); // Có đi học
            } else {
                if (i < ngayHienTai) { // Chỉ tô đỏ ngày quá khứ
                    listDo.add(checkDay); // Vắng
                }else if (i == ngayHienTai) {
                    // Hôm nay mà chưa có trong API -> Chưa học -> XÁM
                    listXam.add(checkDay);
                }
            }
        }

        // 3. Cập nhật UI
        calendarView.removeDecorators();

        // Dùng file drawable _small mới tạo
        calendarView.addDecorator(new EventDecorator(R.drawable.bg_ngaydahoc, listXanh, getActivity()));
        calendarView.addDecorator(new EventDecorator(R.drawable.bg_ngaynghi, listDo, getActivity()));
        calendarView.addDecorator(new EventDecorator(R.drawable.bg_ngaychuahoc, listXam, getActivity()));
        calendarView.invalidateDecorators(); // Refresh lại view cho chắc
    }
}