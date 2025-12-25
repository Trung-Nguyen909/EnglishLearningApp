package com.example.englishlearningapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.englishlearningapp.DTO.Response.SkillDto;
import com.example.englishlearningapp.DTO.Response.UserSummaryDto;
import com.example.englishlearningapp.DTO.UserDetail;
import com.example.englishlearningapp.Retrofit.ApiService;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HoSoFragment extends Fragment {
    private static final String TAG = "HoSoFragment";

    AppCompatButton btnCaiDat;
    private TextView tvTenNguoiDung, tvNgayThamGia, tvStreak, tvTime;
    private TextView tvAchive, tvLessons;
    private ImageView imgAvatar;
    // progress bars (as Views) - find by id thanhnoi/thanhviet/thanhnghe/thanhdoc
    private ProgressBar progressNoi, progressViet, progressNghe, progressDoc;
    private TextView tvNoiScore, tvVietScore, tvNgheScore, tvDocScore;

    public HoSoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_hoso, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.layout_ho_so_chinh), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        view.findViewById(R.id.btn_chinh_sua).setOnClickListener(v -> {
            ChinhSuaHoSoFragment fragmentChinhSuaHoSo = new ChinhSuaHoSoFragment();
            FragmentTransaction giaoDichFragment = getParentFragmentManager().beginTransaction();
            giaoDichFragment.replace(R.id.frame_container, fragmentChinhSuaHoSo);
            giaoDichFragment.addToBackStack(null);
            giaoDichFragment.commit();
        });

        btnCaiDat = view.findViewById(R.id.btn_cai_dat);
        btnCaiDat.setOnClickListener(v -> hienThiHopThoaiCaiDat());

        androidx.cardview.widget.CardView theHoatDongGanDay = view.findViewById(R.id.the_hoat_dong_gan_day);
        if (theHoatDongGanDay != null) {
            theHoatDongGanDay.setOnClickListener(v -> {
                Intent intent = new Intent(requireContext(), LichSuLamBaiActivity.class);
                startActivity(intent);
            });
        }

        // bind views
        tvTenNguoiDung = view.findViewById(R.id.tv_ten_nguoi_dung);
        tvNgayThamGia = view.findViewById(R.id.tv_ngay_tham_gia);
        tvStreak = view.findViewById(R.id.tv_streak);
        tvTime = view.findViewById(R.id.tv_time);

        tvAchive = view.findViewById(R.id.tv_achive);
        tvLessons = view.findViewById(R.id.tv_lessons);
        imgAvatar = view.findViewById(R.id.img_anh_dai_dien);

        // progress bars (IDs from layout)
        progressNoi = view.findViewById(R.id.thanhnoi);
        progressViet = view.findViewById(R.id.thanhviet);
        progressNghe = view.findViewById(R.id.thanhnghe);
        progressDoc = view.findViewById(R.id.thanhdoc);

        //diem
        tvNoiScore = view.findViewById(R.id.tv_noi_score);
        tvVietScore = view.findViewById(R.id.tv_viet_score);
        tvNgheScore = view.findViewById(R.id.tv_nghe_score);
        tvDocScore = view.findViewById(R.id.tv_doc_score);


        // load user basic info from prefs (keeps previous behavior)
        SharedPreferences prefs = requireActivity().getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE);
        String userJson = prefs.getString("USER", null);

        if (userJson != null) {
            UserDetail user = new Gson().fromJson(userJson, UserDetail.class);
            tvTenNguoiDung.setText(user.getTenDangNhap());

            String lastLogin = user.getLastLogin();
            if (lastLogin != null && lastLogin.contains("T")) {
                lastLogin = lastLogin.replace("T", " ");
                if (lastLogin.length() > 19) lastLogin = lastLogin.substring(0, 19);
            }
            tvNgayThamGia.setText("Lần đăng nhập gần nhất: " + (lastLogin == null ? "-" : lastLogin));
            tvStreak.setText(String.valueOf(user.getStreak()));
            int minutes = user.getTongThoiGianHoatDong() / 60;
            tvTime.setText(String.valueOf(minutes));

            // Avatar: if backend returned full url use directly; if returned filename and contains localhost, replace for emulator
            String avatarUrl = user.getAnhDaiDien();
            if (avatarUrl != null) {
                if (!avatarUrl.startsWith("http")) {
                    // assume it's filename only --> build url using emulator host
                    avatarUrl = "http://10.0.2.2:8080/img_user/user_avatar/" + avatarUrl;
                } else if (avatarUrl.contains("localhost")) {
                    avatarUrl = avatarUrl.replace("localhost", "10.0.2.2");
                }
                Log.d(TAG, "Avatar URL initial: " + avatarUrl);
                Glide.with(this)
                        .load(avatarUrl)
                        .placeholder(R.drawable.ic_nguoidung)
                        .error(R.drawable.ic_nguoidung)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(imgAvatar);
            }
        }

        // --- CALL API to fetch summary and populate UI ---
        fetchUserSummary();
    }

    private void fetchUserSummary() {
        ApiService apiService = ApiClient.getClient(requireContext()).create(ApiService.class);
        Call<UserSummaryDto> call = apiService.GetInfoUser();
        call.enqueue(new Callback<UserSummaryDto>() {
            @Override
            public void onResponse(Call<UserSummaryDto> call, Response<UserSummaryDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateSummary(response.body());
                } else {
                    Log.e(TAG, "getMySummary not successful: code=" + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserSummaryDto> call, Throwable t) {
                Log.e(TAG, "getMySummary failure: " + t.getMessage());
            }
        });
    }

    private void populateSummary(UserSummaryDto dto) {
        if (dto == null) return;

        tvAchive.setText(String.valueOf(dto.getSoThanhTuu()));
        tvLessons.setText(String.valueOf(dto.getSoBaiHoc()));

        progressNoi.setProgress(0);
        progressViet.setProgress(0);
        progressNghe.setProgress(0);
        progressDoc.setProgress(0);

        List<SkillDto> skills = dto.getKyNang();
        if (skills != null) {
            for (SkillDto s : skills) {
                String name = s.getTenKyNang() == null ? "" : s.getTenKyNang().toLowerCase();
                int score = s.getDiem();

                int mapped = Math.min(10, Math.max(0, score));

                if (name.contains("nói") || name.contains("noi") || name.contains("speaking")) {
                    progressNoi.setProgress(mapped);
                    tvNoiScore.setText(score + "/10");
                } else if (name.contains("viết") || name.contains("viet") || name.contains("writing")) {
                    progressViet.setProgress(mapped);
                    tvVietScore.setText(score + "/10");
                } else if (name.contains("nghe") || name.contains("listening")) {
                    progressNghe.setProgress(mapped);
                    tvNgheScore.setText(score + "/10");
                } else if (name.contains("đọc") || name.contains("doc") || name.contains("reading")) {
                    progressDoc.setProgress(mapped);
                    tvDocScore.setText(score + "/10");
                } else {
                    // nếu skill khác, bạn có thể xử lý thêm (hiện log)
                    Log.d(TAG, "Unmapped skill: " + s.getTenKyNang() + " score=" + s.getDiem());
                }
            }
        }
    }

    private void hienThiHopThoaiCaiDat() {
        final Dialog hopThoai = new Dialog(requireContext());
        hopThoai.requestWindowFeature(Window.FEATURE_NO_TITLE);
        hopThoai.setContentView(R.layout.dialog_caidat);

        hopThoai.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        hopThoai.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        hopThoai.getWindow().setGravity(Gravity.CENTER);

        ImageView iconThongBao = hopThoai.findViewById(R.id.ic_notif);
        ImageView iconAmThanh = hopThoai.findViewById(R.id.ic_sound);
        ImageView iconCheDoToi = hopThoai.findViewById(R.id.ic_dark);

        ImageButton btnDongCaiDat = hopThoai.findViewById(R.id.btn_close_settings);
        AppCompatButton btnHoanThanh = hopThoai.findViewById(R.id.btn_done);

        SwitchCompat swThongBao = hopThoai.findViewById(R.id.sw_notif);
        SwitchCompat swAmThanh = hopThoai.findViewById(R.id.sw_sound);
        SwitchCompat swCheDoToi = hopThoai.findViewById(R.id.sw_dark);

        TextView tvTrangThaiThongBao = hopThoai.findViewById(R.id.tv_notif_status);
        TextView tvTrangThaiAmThanh = hopThoai.findViewById(R.id.tv_sound_status);
        TextView tvTrangThaiCheDoToi = hopThoai.findViewById(R.id.tv_dark_status);

        RelativeLayout layoutNgonNgu = hopThoai.findViewById(R.id.layout_language);
        TextView tvTrangThaiNgonNgu = hopThoai.findViewById(R.id.tv_lang_status);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("CaiDatUngDung", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        boolean cheDoToi = sharedPreferences.getBoolean("cheDoToi", false);

        swCheDoToi.setChecked(cheDoToi);
        tvTrangThaiCheDoToi.setText(cheDoToi ? "Hiệu ứng tối" : "Hiệu ứng sáng");
        iconCheDoToi.setImageResource(cheDoToi ? R.drawable.img_ic_dark_mode : R.drawable.img_ic_light_mode);

        swThongBao.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tvTrangThaiThongBao.setText(isChecked ? "Bật thông báo" : "Tắt thông báo");
            if (isChecked) iconThongBao.setImageResource(R.drawable.img_ic_notify_on);
            else iconThongBao.setImageResource(R.drawable.img_ic_notify_off);
        });

        swAmThanh.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tvTrangThaiAmThanh.setText(isChecked ? "Bật âm thanh" : "Tắt âm thanh");
            if (isChecked) iconAmThanh.setImageResource(R.drawable.img_ic_sound_on);
            else iconAmThanh.setImageResource(R.drawable.img_ic_sound_off);
        });

        swCheDoToi.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tvTrangThaiCheDoToi.setText(isChecked ? "Hiệu ứng tối" : "Hiệu ứng sáng");
            if (isChecked) iconCheDoToi.setImageResource(R.drawable.img_ic_dark_mode);
            else iconCheDoToi.setImageResource(R.drawable.img_ic_light_mode);
            editor.putBoolean("cheDoToi", isChecked);
            editor.apply();
            if (isChecked) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        });

        layoutNgonNgu.setOnClickListener(v -> {
            String[] ngonNgu = {"English", "Tiếng Việt"};
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Lựa chọn ngôn ngữ");
            builder.setItems(ngonNgu, (dialogInterface, which) -> tvTrangThaiNgonNgu.setText(ngonNgu[which]));
            builder.show();
        });

        btnDongCaiDat.setOnClickListener(v -> hopThoai.dismiss());
        btnHoanThanh.setOnClickListener(v -> hopThoai.dismiss());

        hopThoai.show();

        LinearLayout btnDangXuat = hopThoai.findViewById(R.id.btn_logout);
        btnDangXuat.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất khỏi tài khoản không?")
                    .setPositiveButton("Đồng ý", (dialogInterface, which) -> {
                        Intent intent = new Intent(requireContext(), ManHinhChoActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        hopThoai.dismiss();
                    })
                    .setNegativeButton("Hủy", (dialogInterface, which) -> dialogInterface.dismiss())
                    .show();
        });
    }
}
