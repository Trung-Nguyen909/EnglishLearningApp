package com.example.englishlearningapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.DTO.Response.KhoaHocResponse;
import com.example.englishlearningapp.Fragment.KiemTraFragment;
import com.example.englishlearningapp.Model.ChuDeModel;
import com.example.englishlearningapp.Model.ChuDePhuModel;
import com.example.englishlearningapp.Retrofit.ApiService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KhoaHocFragment extends Fragment {

    private RecyclerView recyclerView;
    private View btn_TrangKiemTra;
    private ChuDeAdapter adapter;
    private List<ChuDeModel> topics = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_khoahoc, container, false);
        recyclerView = view.findViewById(R.id.rcv_chitiet);
        btn_TrangKiemTra = view.findViewById(R.id.btn_kiemtra);

        // set adapter với list rỗng trước, sau đó fetch data
        adapter = new ChuDeAdapter(getContext(), topics);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // SỰ KIỆN CLICK: Chuyển sang màn hình Kiểm tra
        if (btn_TrangKiemTra != null) {
            btn_TrangKiemTra.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_container, new KiemTraFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        }

        // gọi API để load khóa học
        loadKhoaHocFromApi();

        return view;
    }

    private void loadKhoaHocFromApi() {
        ApiService service = ApiClient.getClient(requireContext()).create(ApiService.class);
        Log.d("KHOAHOC_API", "Bắt đầu gọi API KhoaHoc...");
        Call<List<KhoaHocResponse>> call = service.getAllKhoaHoc();
        call.enqueue(new Callback<List<KhoaHocResponse>>() {
            @Override
            public void onResponse(Call<List<KhoaHocResponse>> call, Response<List<KhoaHocResponse>> response) {
                Log.d("KHOAHOC_API", "HTTP CODE: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    List<KhoaHocResponse> khoaHocs = response.body();
                    if (khoaHocs.isEmpty()) {
                        adapter.setData(new ArrayList<>());
                        return;
                    }

                    // tạo temp list ChuDeModel (chưa có bài học)
                    List<ChuDeModel> tempList = new ArrayList<>();
                    for (KhoaHocResponse k : khoaHocs) {
                        // khởi tạo với danhSachMucCon = empty list (sẽ set sau khi gọi API bài học)
                        tempList.add(new ChuDeModel(
                                k.getTenKhoaHoc() != null ? k.getTenKhoaHoc() : "Khóa học",
                                R.drawable.img_ic_animal_course,
                                new ArrayList<>()
                        ));
                    }

                    // map id khoaHoc -> ChuDeModel để dễ set bài học sau
                    final java.util.Map<Integer, ChuDeModel> mapById = new java.util.HashMap<>();
                    for (int i = 0; i < khoaHocs.size(); i++) {
                        mapById.put(khoaHocs.get(i).getId(), tempList.get(i));
                    }

                    // đếm số API bài học còn chờ
                    final java.util.concurrent.atomic.AtomicInteger remaining = new java.util.concurrent.atomic.AtomicInteger(khoaHocs.size());

                    for (KhoaHocResponse k : khoaHocs) {
                        int idKhoa = k.getId();
                        Call<List<com.example.englishlearningapp.DTO.Response.BaiHocResponse>> callBaiHoc =
                                service.getBaiHocByKhoaHoc(idKhoa);
                        callBaiHoc.enqueue(new Callback<List<com.example.englishlearningapp.DTO.Response.BaiHocResponse>>() {
                            @Override
                            public void onResponse(Call<List<com.example.englishlearningapp.DTO.Response.BaiHocResponse>> call, Response<List<com.example.englishlearningapp.DTO.Response.BaiHocResponse>> resp) {
                                if (resp.isSuccessful() && resp.body() != null) {
                                    // set danh sách bài học vào ChuDeModel tương ứng
                                    ChuDeModel cd = mapById.get(idKhoa);
                                    if (cd != null) {
                                        cd.setDanhSachMucCon(resp.body());
                                    }
                                } else {
                                    Log.w("KHOAHOC_API", "Không lấy được bài học cho khoahoc " + idKhoa + ", code=" + resp.code());
                                }

                                // khi tất cả request bài học hoàn thành -> cập nhật adapter
                                if (remaining.decrementAndGet() == 0) {
                                    // cập nhật adapter trên UI thread
                                    if (getActivity() != null) {
                                        getActivity().runOnUiThread(() -> adapter.setData(tempList));
                                    } else {
                                        adapter.setData(tempList);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<List<com.example.englishlearningapp.DTO.Response.BaiHocResponse>> call, Throwable t) {
                                Log.e("KHOAHOC_API", "Lỗi khi lấy bài học cho khoahoc " + idKhoa, t);
                                if (remaining.decrementAndGet() == 0) {
                                    if (getActivity() != null) {
                                        getActivity().runOnUiThread(() -> adapter.setData(tempList));
                                    } else {
                                        adapter.setData(tempList);
                                    }
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi tải khóa học: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<KhoaHocResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Không thể kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
