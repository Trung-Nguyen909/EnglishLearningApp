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

    Button btnEdit;
    AppCompatButton btnSettings;

    public HoSoFragment() {
        // Constructor rỗng
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_hoso, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Xử lý EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main_profile_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Ánh xạ nút chỉnh sửa
        btnEdit = view.findViewById(R.id.btnEdit);

        // Xử lý sự kiện nút Chỉnh sửa -> Chuyển sang EditProfileFragment
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChinhSuaHoSoFragment editProfileFragment = new ChinhSuaHoSoFragment();

                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, editProfileFragment);

                // Thêm vào BackStack để ấn nút Back điện thoại sẽ quay lại trang Profile
                transaction.addToBackStack(null);

                transaction.commit();
            }
        });
        btnSettings = view.findViewById(R.id.btnSettings);

        btnSettings.setOnClickListener(v -> {
            showSettingsDialog();
        });
    }
    private void showSettingsDialog() {
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_caidat);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER);

        // --- 1. Ánh xạ các View ---
        ImageView icNotif = dialog.findViewById(R.id.ic_notif);
        ImageView icSound = dialog.findViewById(R.id.ic_sound);
        ImageView icDark = dialog.findViewById(R.id.ic_dark);

        ImageButton btnClose = dialog.findViewById(R.id.btn_close_settings);
        AppCompatButton btnDone = dialog.findViewById(R.id.btn_done);

        // Switches
        SwitchCompat swNotif = dialog.findViewById(R.id.sw_notif);
        SwitchCompat swSound = dialog.findViewById(R.id.sw_sound);
        SwitchCompat swDark = dialog.findViewById(R.id.sw_dark);

        // Status TextViews
        TextView tvNotifStatus = dialog.findViewById(R.id.tv_notif_status);
        TextView tvSoundStatus = dialog.findViewById(R.id.tv_sound_status);
        TextView tvDarkStatus = dialog.findViewById(R.id.tv_dark_status);

        // Language
        RelativeLayout layoutLanguage = dialog.findViewById(R.id.layout_language);
        TextView tvLangStatus = dialog.findViewById(R.id.tv_lang_status);

        // 1. Khởi tạo SharedPreferences (Bộ nhớ)
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("AppSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Mặc định là false (Sáng) nếu chưa lưu gì
        boolean isDarkMode = sharedPreferences.getBoolean("isDarkMode", false);
        // Cập nhật UI ban đầu theo trạng thái đã lưu
        swDark.setChecked(isDarkMode);
        tvDarkStatus.setText(isDarkMode ? "Hiệu ứng tối" : "Hiệu ứng sáng");
        icDark.setImageResource(isDarkMode ? R.drawable.img_ic_dark_mode : R.drawable.img_ic_light_mode);

        // --- 2. Xử lý sự kiện Switch (Bật/Tắt) ---
        // Notifications
        swNotif.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tvNotifStatus.setText(isChecked ? "Bật thông báo" : "Tắt thông báo");
            if (isChecked) {
                // Ảnh khi BẬT (Dùng ảnh của bạn)
                icNotif.setImageResource(R.drawable.img_ic_notify_on);
            } else {
                // Ảnh khi TẮT (Dùng ảnh của bạn)
                icNotif.setImageResource(R.drawable.img_ic_notify_off);
            }
        });

        // Sound
        swSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tvSoundStatus.setText(isChecked ? "Bật âm thanh" : "Tắt âm thanh");
            if (isChecked) {
                // Ảnh khi BẬT
                icSound.setImageResource(R.drawable.img_ic_sound_on);
            } else {
                // Ảnh khi TẮT
                icSound.setImageResource(R.drawable.img_ic_sound_off);
            }
        });

        // Dark Mode
        swDark.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tvDarkStatus.setText(isChecked ? "Hiệu ứng tối" : "Hiệu ứng sáng");
            if (isChecked) {
                // Ảnh khi BẬT (Chế độ tối - Mặt trăng)
                icDark.setImageResource(R.drawable.img_ic_dark_mode);
            } else {
                // Ảnh khi TẮT (Chế độ sáng - Mặt trời)
                icDark.setImageResource(R.drawable.img_ic_light_mode);
            }
            // B. Lưu trạng thái vào bộ nhớ
            editor.putBoolean("isDarkMode", isChecked);
            editor.apply(); // Lưu xuống file

            // C. Kích hoạt chế độ tối/sáng
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            // Lưu ý: Khi dòng lệnh trên chạy, Activity sẽ tự động khởi động lại (recreate)
            // để áp dụng theme mới. Dialog sẽ bị đóng lại, đây là hành vi bình thường.
        });

        // --- 3. Xử lý chọn Ngôn Ngữ ---
        layoutLanguage.setOnClickListener(v -> {
            // Tạo danh sách lựa chọn
            String[] languages = {"English", "Tiếng Việt"};

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Lựa chọn ngôn ngữ");
            builder.setItems(languages, (dialogInterface, which) -> {
                // Cập nhật text dựa trên lựa chọn
                tvLangStatus.setText(languages[which]);

                // Code logic đổi ngôn ngữ (nếu có)
            });
            builder.show();
        });

        // --- 4. Xử lý nút Đóng ---
        btnClose.setOnClickListener(v -> dialog.dismiss());
        btnDone.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        LinearLayout btnLogout = dialog.findViewById(R.id.btn_logout);

        // Xử lý sự kiện Click
        btnLogout.setOnClickListener(v -> {
            // Tạo hộp thoại xác nhận (AlertDialog)
            new AlertDialog.Builder(requireContext()) // 'this' là ProfileActivity
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất khỏi tài khoản không?")

                    // Nút "Đồng ý" (Positive Button)
                    .setPositiveButton("Đồng ý", (dialogInterface, which) -> {
                        // --- BẮT ĐẦU LOGIC ĐĂNG XUẤT ---
                        // BƯỚC 1: Xóa dữ liệu đăng nhập (Nếu có dùng SharedPreferences để lưu)
                        // Ví dụ: Xóa trạng thái "đã đăng nhập"
                        //            SharedPreferences preferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                        //            SharedPreferences.Editor editor = preferences.edit();
                        //            editor.clear(); // Xóa sạch dữ liệu
                        //            editor.apply();

                        // BƯỚC 2: Chuyển màn hình và Xóa lịch sử (Quan trọng!)
                        Intent intent = new Intent(requireContext(), ManHinhChoActivity.class);

                        // Hai cờ (Flag) này giúp xóa sạch các Activity cũ,
                        // làm cho SplashActivity trở thành màn hình duy nhất trong ngăn xếp.
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        // Đóng dialog (thực ra không cần thiết lắm vì Activity đã bị hủy, nhưng nên có cho sạch)
                        dialog.dismiss();
                    })

                    // Nút "Hủy" (Negative Button)
                    .setNegativeButton("Hủy", (dialogInterface, which) -> {
                        // Chỉ cần đóng hộp thoại xác nhận, không làm gì cả
                        dialogInterface.dismiss();
                    })
                    .show();
        });
    }
    }