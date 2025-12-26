package com.example.englishlearningapp.Fragment;

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

import com.example.englishlearningapp.Adapter.ChuDeAdapter;
import com.example.englishlearningapp.ApiClient;
import com.example.englishlearningapp.DTO.Response.BaiHocResponse;
import com.example.englishlearningapp.DTO.Response.KhoaHocResponse;
import com.example.englishlearningapp.Model.ChuDeModel;
import com.example.englishlearningapp.R;
import com.example.englishlearningapp.Retrofit.ApiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KhoaHocFragment extends Fragment {

    private RecyclerView recyclerView;
    private View btn_TrangKiemTra;
    private ChuDeAdapter adapter;
    private final List<ChuDeModel> topics = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_khoahoc, container, false);

        recyclerView = view.findViewById(R.id.rcv_chitiet);
        btn_TrangKiemTra = view.findViewById(R.id.btn_kiemtra);

        // set adapter với list rỗng trước, sau đó fetch data
        adapter = new ChuDeAdapter(requireContext(), topics);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        // SỰ KIỆN CLICK: Chuyển sang màn hình Kiểm tra
        if (btn_TrangKiemTra != null) {
            btn_TrangKiemTra.setOnClickListener(v -> {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, new KiemTraFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            });
        }

        // gọi API để load khóa học
        loadKhoaHocFromApi();

        return view;
    }

    private int getDrawableId(String drawableName) {
        if (drawableName == null || drawableName.trim().isEmpty()) {
            return R.drawable.img_ic_animal_course; // icon mặc định
        }

        int resId = requireContext()
                .getResources()
                .getIdentifier(drawableName, "drawable", requireContext().getPackageName());

        return resId != 0 ? resId : R.drawable.img_ic_animal_course;
    }

    private void loadKhoaHocFromApi() {
        ApiService service = ApiClient.getClient(requireContext()).create(ApiService.class);

        Log.d("KHOAHOC_API", "Bắt đầu gọi API KhoaHoc...");
        Call<List<KhoaHocResponse>> call = service.getAllKhoaHoc();

        call.enqueue(new Callback<List<KhoaHocResponse>>() {
            @Override
            public void onResponse(Call<List<KhoaHocResponse>> call, Response<List<KhoaHocResponse>> response) {
                Log.d("KHOAHOC_API", "HTTP CODE: " + response.code());

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(requireContext(),
                            "Lỗi tải khóa học: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                List<KhoaHocResponse> khoaHocs = response.body();
                if (khoaHocs.isEmpty()) {
                    adapter.setData(new ArrayList<>());
                    return;
                }

                // 1) tạo temp list ChuDeModel (chưa có bài học)
                List<ChuDeModel> tempList = new ArrayList<>();

                for (KhoaHocResponse k : khoaHocs) {
                    int iconRes = getDrawableId(k.getIconUrl()); //  lấy từ DB -> drawable local

                    tempList.add(new ChuDeModel(
                            (k.getTenKhoaHoc() != null && !k.getTenKhoaHoc().trim().isEmpty())
                                    ? k.getTenKhoaHoc()
                                    : "Khóa học",
                            iconRes,
                            new ArrayList<>()
                    ));
                }

                // 2) map idKhoaHoc -> ChuDeModel để set bài học vào đúng chỗ
                Map<Integer, ChuDeModel> mapById = new HashMap<>();
                for (int i = 0; i < khoaHocs.size(); i++) {
                    mapById.put(khoaHocs.get(i).getId(), tempList.get(i));
                }

                // 3) gọi API bài học theo từng khóa học, đợi xong hết rồi mới update adapter
                AtomicInteger remaining = new AtomicInteger(khoaHocs.size());

                for (KhoaHocResponse k : khoaHocs) {
                    final int idKhoa = k.getId();

                    Call<List<BaiHocResponse>> callBaiHoc =
                            service.getBaiHocByKhoaHoc(idKhoa);

                    callBaiHoc.enqueue(new Callback<List<BaiHocResponse>>() {
                        @Override
                        public void onResponse(
                                Call<List<BaiHocResponse>> call,
                                Response<List<BaiHocResponse>> resp
                        ) {
                            if (resp.isSuccessful() && resp.body() != null) {
                                ChuDeModel cd = mapById.get(idKhoa);
                                if (cd != null) {
                                    cd.setDanhSachMucCon(resp.body());
                                }
                            } else {
                                Log.w("KHOAHOC_API",
                                        "Không lấy được bài học cho khoahoc " + idKhoa + ", code=" + resp.code());
                            }

                            finishIfDone(remaining, tempList);
                        }

                        @Override
                        public void onFailure(
                                Call<List<BaiHocResponse>> call,
                                Throwable t
                        ) {
                            Log.e("KHOAHOC_API", "Lỗi khi lấy bài học cho khoahoc " + idKhoa, t);
                            finishIfDone(remaining, tempList);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<KhoaHocResponse>> call, Throwable t) {
                Toast.makeText(requireContext(),
                        "Không thể kết nối: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void finishIfDone(AtomicInteger remaining, List<ChuDeModel> tempList) {
        if (remaining.decrementAndGet() == 0) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> adapter.setData(tempList));
            } else {
                adapter.setData(tempList);
            }
        }
    }
}
