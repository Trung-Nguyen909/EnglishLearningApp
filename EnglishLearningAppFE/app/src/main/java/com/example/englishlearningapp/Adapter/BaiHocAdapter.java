package com.example.englishlearningapp.Adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Model.BaiHocModel;
import com.example.englishlearningapp.R;

import java.util.List;

public class BaiHocAdapter extends RecyclerView.Adapter<BaiHocAdapter.LessonViewHolder> {
    private final List<BaiHocModel> danhSachBaiHoc;
    private final Context boiCanh;

    // 1. Khai báo biến chứa sự kiện click
    private SuKienClickItem suKienClick;

    // 2. Tạo Interface (Giao diện) để lắng nghe sự kiện click
    public interface SuKienClickItem {
        void khiAnVaoItem(BaiHocModel baiHoc);
    }
    private String formatThoiGian(String thoigian) {
        if (thoigian == null || thoigian.isEmpty()) return "";
        try {
            // Chia chuỗi "HH:MM:SS"
            String[] parts = thoigian.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            // int seconds = Integer.parseInt(parts[2]); // có thể bỏ qua
            int totalMinutes = hours * 60 + minutes;
            return totalMinutes + " phút";
        } catch (Exception e) {
            e.printStackTrace();
            return thoigian; // fallback nếu lỗi
        }
    }


    // 3. Hàm để gán sự kiện click từ bên ngoài vào
    public void datSuKienClick(SuKienClickItem suKien) {
        this.suKienClick = suKien;
    }

    // Constructor (Hàm khởi tạo)
    public BaiHocAdapter(Context context, List<BaiHocModel> danhSach) {
        this.boiCanh = context;
        this.danhSachBaiHoc = danhSach;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(boiCanh).inflate(R.layout.item_bai_hoc, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        BaiHocModel baiHoc = danhSachBaiHoc.get(position);

        holder.nhanLoai.setText(baiHoc.getLoaiBaiHoc());
        holder.tieuDe.setText(baiHoc.getTieuDe());
        holder.capDo.setText(baiHoc.getCapDo());

        // Chuyển đổi thời gian từ "00:10:00" -> "10 phút"
        holder.thoiGian.setText(formatThoiGian(baiHoc.getThoiGian()));

        GradientDrawable nenNhan = (GradientDrawable) holder.nhanLoai.getBackground();
        nenNhan.setColor(boiCanh.getColor(baiHoc.getMauSacLoai()));

        holder.itemView.setOnClickListener(v -> {
            if (suKienClick != null) {
                suKienClick.khiAnVaoItem(baiHoc);
            }
        });
    }


    @Override
    public int getItemCount() {
        return danhSachBaiHoc.size();
    }
    public static class LessonViewHolder extends RecyclerView.ViewHolder {
        TextView nhanLoai;
        TextView tieuDe;
        TextView capDo;
        TextView thoiGian;
        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ View
            nhanLoai = itemView.findViewById(R.id.tv_loai_bai_hoc);
            tieuDe = itemView.findViewById(R.id.tv_tieu_de_bai_hoc);
            capDo = itemView.findViewById(R.id.tv_cap_do);
            thoiGian = itemView.findViewById(R.id.tv_thoi_gian);
        }
    }
}