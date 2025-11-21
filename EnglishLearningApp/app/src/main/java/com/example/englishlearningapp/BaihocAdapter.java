package com.example.englishlearningapp;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Model.Lesson;

import java.util.List;

public class BaihocAdapter extends RecyclerView.Adapter<BaihocAdapter.LessonViewHolder> {

    // Đổi tên biến sang tiếng Việt
    private final List<Lesson> danhSachBaiHoc;
    private final Context boiCanh;

    // 1. Khai báo biến chứa sự kiện click
    private SuKienClickItem suKienClick;

    // 2. Tạo Interface (Giao diện) để lắng nghe sự kiện click
    public interface SuKienClickItem {
        void khiAnVaoItem(Lesson baiHoc);
    }

    // 3. Hàm để gán sự kiện click từ bên ngoài vào
    public void datSuKienClick(SuKienClickItem suKien) {
        this.suKienClick = suKien;
    }

    // Constructor (Hàm khởi tạo)
    public BaihocAdapter(Context context, List<Lesson> danhSach) {
        this.boiCanh = context;
        this.danhSachBaiHoc = danhSach;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(boiCanh).inflate(R.layout.item_exercise, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson baiHoc = danhSachBaiHoc.get(position);

        // Gán dữ liệu (Đã đổi tên biến holder sang tiếng Việt ở dưới)
        holder.nhanLoai.setText(baiHoc.getType());
        holder.tieuDe.setText(baiHoc.getTitle());
        holder.capDo.setText(baiHoc.getLevel());
        holder.thoiGian.setText(baiHoc.getTime());

        // Thay đổi màu nền của nhãn
        GradientDrawable nenNhan = (GradientDrawable) holder.nhanLoai.getBackground();
        nenNhan.setColor(boiCanh.getColor(baiHoc.getTypeColor()));

        // 4. XỬ LÝ SỰ KIỆN CLICK (QUAN TRỌNG)
        holder.itemView.setOnClickListener(v -> {
            if (suKienClick != null) {
                suKienClick.khiAnVaoItem(baiHoc); // Gửi bài học được chọn ra ngoài
            }
        });
    }

    @Override
    public int getItemCount() {
        return danhSachBaiHoc.size();
    }

    // ViewHolder - Đổi tên biến thành tiếng Việt
    public static class LessonViewHolder extends RecyclerView.ViewHolder {
        TextView nhanLoai; // typeLabel cũ
        TextView tieuDe;   // title cũ
        TextView capDo;    // level cũ
        TextView thoiGian; // time cũ

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ View (ID giữ nguyên theo file XML của bạn)
            nhanLoai = itemView.findViewById(R.id.lesson_type_label);
            tieuDe = itemView.findViewById(R.id.lesson_title);
            capDo = itemView.findViewById(R.id.lesson_level);
            thoiGian = itemView.findViewById(R.id.lesson_time);
        }
    }
}