package com.example.englishlearningapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Adapter.KyNangAdapter;
import com.example.englishlearningapp.ApiClient;
import com.example.englishlearningapp.Activity.BaiHocActivity;
import com.example.englishlearningapp.DTO.Response.BaiHocGanNhatResponse;
import com.example.englishlearningapp.DTO.UserDetail;
import com.example.englishlearningapp.R;
import com.example.englishlearningapp.Retrofit.ApiService;
import com.example.englishlearningapp.TrangChu.EventDecorator;
import com.example.englishlearningapp.DTO.Response.KyNangResponse;
import com.example.englishlearningapp.DTO.Response.NhatKyHoatDong; // Import Model
import com.google.gson.Gson;
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
    private TextView tvKhoaHocGanNhat, tvBaiHocGanNhat, tvPhanTramGanNhat;
    private com.google.android.material.progressindicator.CircularProgressIndicator progressIndicator;

    // Biến lưu thông tin để dùng cho nút bấm
    private String tenBaiHocCurrent = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_trang_chu, container, false);

        // 1. Ánh xạ View
        rcvKiemTraNhanh = view.findViewById(R.id.rcv_kiem_tra_nhanh);
        btnTiepTucHoc = view.findViewById(R.id.btn_tiep_tuc_hoc);
        calendarView = view.findViewById(R.id.calendarView);
        // Ánh xạ các View hiển thị tiến độ (Thêm ID vào XML trước nhé)
        tvKhoaHocGanNhat = view.findViewById(R.id.tv_khoa_hoc_gan_nhat);
        tvBaiHocGanNhat = view.findViewById(R.id.tv_bai_hoc_gan_nhat);
        tvPhanTramGanNhat = view.findViewById(R.id.tv_phan_tram);
        progressIndicator = view.findViewById(R.id.progress_indicator);

        // 2. Setup Calendar cơ bản
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
        // Set ngày hiện tại để lịch focus vào tháng hiện tại
        calendarView.setCurrentDate(CalendarDay.today());


        // 3. GỌI API LẤY LỊCH (Thay thế cho hàm fake data cũ)
        // Giả sử UserId = 1 (Bạn cần lấy ID thật từ SharedPreferences hoặc lúc Login)
        SharedPreferences prefs = getActivity().getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        UserDetail user = gson.fromJson(
                prefs.getString("USER", null),
                UserDetail.class
        );
        int userId = user.getId();

        taiBaiHocGanNhat();// Mặc định là 1 nếu chưa lưu
        callApiLayLichHoatDong(userId);

        // 4. Xử lý sự kiện nút "Tiếp tục học"
        if (btnTiepTucHoc != null) {
            btnTiepTucHoc.setOnClickListener(v -> {
                if (userId != -1) {
                    Intent intent = new Intent(getContext(), BaiHocActivity.class);
                    intent.putExtra("BAIHOC_ID", userId);
                    intent.putExtra("TEN_BAI_HOC", tenBaiHocCurrent);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Chưa có bài học nào!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        goiApiLayDanhSachKyNang();
        return view;


//        // --- CODE CŨ GIỮ NGUYÊN (XỬ LÝ NÚT VÀ QUICK TEST) ---
//        if (btnTiepTucHoc != null) {
//            btnTiepTucHoc.setOnClickListener(v -> {
//                Intent intent = new Intent(getContext(), BaiHocActivity.class);
//                intent.putExtra("SUB_ITEM_ID", 1);
//                intent.putExtra("SUB_ITEM_NAME", "Thành viên trong gia đình");
//                startActivity(intent);
//            });
//        }
//
//        goiApiLayDanhSachKyNang();
//        return view;
    }

    // --- HÀM GỌI API MỚI ---
    private void goiApiLayDanhSachKyNang() {
        ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);

        apiService.getAllKyNang().enqueue(new Callback<List<KyNangResponse>>() {
            @Override
            public void onResponse(Call<List<KyNangResponse>> call, Response<List<KyNangResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<KyNangResponse> listTuApi = response.body();

                    // Khởi tạo Adapter với dữ liệu từ API
                    adapterKyNang = new KyNangAdapter(getContext(), listTuApi);

                    // Setup sự kiện click (Code cũ của bạn chuyển vào đây)
                    adapterKyNang.setLangNgheSuKienClick(new KyNangAdapter.LangNgheSuKienClick() {
                        @Override
                        public void khiClickVaoItem(KyNangResponse kyNang) {
                            String tenKyNang = kyNang.getTenKyNang();

                            // Chuyển Fragment (Logic cũ của bạn)
                            KiemTraFragment kiemtraFragment = new KiemTraFragment();
                            Bundle goiDuLieu = new Bundle();
                            goiDuLieu.putString("TEN_CHU_DE", tenKyNang);
                            goiDuLieu.putInt("ID_KYNANG", kyNang.getId()); // Truyền thêm ID nếu cần
                            goiDuLieu.putBoolean("TU_TRANG_CHU", true);
                            kiemtraFragment.setArguments(goiDuLieu);

                            if (getParentFragmentManager() != null) {
                                getParentFragmentManager().beginTransaction()
                                        .replace(R.id.frame_container, kiemtraFragment)
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }
                    });

                    // Gán vào RecyclerView
                    GridLayoutManager gridManager = new GridLayoutManager(getContext(), 4);
                    rcvKiemTraNhanh.setLayoutManager(gridManager);
                    rcvKiemTraNhanh.setAdapter(adapterKyNang);
                }
            }

            @Override
            public void onFailure(Call<List<KyNangResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải kỹ năng", Toast.LENGTH_SHORT).show();
            }
        });
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
    private void taiBaiHocGanNhat() {
        ApiService apiService = ApiClient.getClient(getContext()).create(ApiService.class);
        apiService.getBaiHocGanNhat().enqueue(new Callback<BaiHocGanNhatResponse>() {
            @Override
            public void onResponse(Call<BaiHocGanNhatResponse> call, Response<BaiHocGanNhatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BaiHocGanNhatResponse data = response.body();

                    // Cập nhật giao diện
                    if(tvKhoaHocGanNhat != null) tvKhoaHocGanNhat.setText(data.getTenKhoaHoc());
                    if(tvBaiHocGanNhat != null) tvBaiHocGanNhat.setText(data.getTenBaiHoc());

                    int phanTram = (int) data.getPhanTram();
                    if(tvPhanTramGanNhat != null) tvPhanTramGanNhat.setText(phanTram + "%");
                    if(progressIndicator != null) progressIndicator.setProgress(phanTram);

                    tenBaiHocCurrent = data.getTenBaiHoc();
                } else {
                    // Nếu không có dữ liệu (User mới chưa học gì)
                    if(tvBaiHocGanNhat != null) tvBaiHocGanNhat.setText("Chưa có bài học");
                }
            }

            @Override
            public void onFailure(Call<BaiHocGanNhatResponse> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi tải bài học gần nhất: " + t.getMessage());
            }
        });
    }
}