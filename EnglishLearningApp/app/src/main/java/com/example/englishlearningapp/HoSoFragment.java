package com.example.englishlearningapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class HoSoFragment extends Fragment {
    Button btnChinhSua;
    AppCompatButton btnCaiDat;

    public HoSoFragment() {
    }

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

        btnChinhSua = view.findViewById(R.id.btn_chinh_sua);

        // Xử lý sự kiện nút Chỉnh sửa
        btnChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChinhSuaHoSoFragment fragmentChinhSuaHoSo = new ChinhSuaHoSoFragment();

                FragmentTransaction giaoDichFragment = getParentFragmentManager().beginTransaction();
                giaoDichFragment.replace(R.id.frame_container, fragmentChinhSuaHoSo);

                giaoDichFragment.addToBackStack(null);

                giaoDichFragment.commit();
            }
        });

        btnCaiDat = view.findViewById(R.id.btn_cai_dat);

        btnCaiDat.setOnClickListener(v -> {
            hienThiHopThoaiCaiDat();
        });
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

        // 1. Khởi tạo SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("CaiDatUngDung", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        boolean cheDoToi = sharedPreferences.getBoolean("cheDoToi", false);

        swCheDoToi.setChecked(cheDoToi);
        tvTrangThaiCheDoToi.setText(cheDoToi ? "Hiệu ứng tối" : "Hiệu ứng sáng");
        iconCheDoToi.setImageResource(cheDoToi ? R.drawable.img_ic_dark_mode : R.drawable.img_ic_light_mode);

        // --- 2. Xử lý sự kiện Switch (Bật/Tắt) ---
        // Thông báo
        swThongBao.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tvTrangThaiThongBao.setText(isChecked ? "Bật thông báo" : "Tắt thông báo");
            if (isChecked) {
                iconThongBao.setImageResource(R.drawable.img_ic_notify_on);
            } else {
                iconThongBao.setImageResource(R.drawable.img_ic_notify_off);
            }
        });

        // Âm thanh
        swAmThanh.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tvTrangThaiAmThanh.setText(isChecked ? "Bật âm thanh" : "Tắt âm thanh");
            if (isChecked) {
                iconAmThanh.setImageResource(R.drawable.img_ic_sound_on);
            } else {
                iconAmThanh.setImageResource(R.drawable.img_ic_sound_off);
            }
        });

        // Chế độ tối
        swCheDoToi.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tvTrangThaiCheDoToi.setText(isChecked ? "Hiệu ứng tối" : "Hiệu ứng sáng");
            if (isChecked) {
                iconCheDoToi.setImageResource(R.drawable.img_ic_dark_mode);
            } else {
                iconCheDoToi.setImageResource(R.drawable.img_ic_light_mode);
            }
            // Lưu trạng thái vào bộ nhớ (key tiếng Việt)
            editor.putBoolean("cheDoToi", isChecked);
            editor.apply();

            // Kích hoạt chế độ tối/sáng
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        // --- 3. Xử lý chọn Ngôn Ngữ ---
        layoutNgonNgu.setOnClickListener(v -> {
            String[] ngonNgu = {"English", "Tiếng Việt"};

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Lựa chọn ngôn ngữ");
            builder.setItems(ngonNgu, (dialogInterface, which) -> {
                tvTrangThaiNgonNgu.setText(ngonNgu[which]);
            });
            builder.show();
        });

        // --- 4. Xử lý nút Đóng ---
        btnDongCaiDat.setOnClickListener(v -> hopThoai.dismiss());
        btnHoanThanh.setOnClickListener(v -> hopThoai.dismiss());

        hopThoai.show();

        // Đổi tên biến: btnLogout -> btnDangXuat
        LinearLayout btnDangXuat = hopThoai.findViewById(R.id.btn_logout);

        // Xử lý sự kiện Đăng xuất
        btnDangXuat.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất khỏi tài khoản không?")
                    .setPositiveButton("Đồng ý", (dialogInterface, which) -> {
                        // Logic đăng xuất: Chuyển về màn hình chờ (ManHinhChoActivity)
                        Intent intent = new Intent(requireContext(), ManHinhChoActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        hopThoai.dismiss();
                    })
                    .setNegativeButton("Hủy", (dialogInterface, which) -> {
                        dialogInterface.dismiss();
                    })
                    .show();
        });
    }
}