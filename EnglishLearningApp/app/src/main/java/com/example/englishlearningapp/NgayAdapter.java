package com.example.englishlearningapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Model.Ngay;

import java.util.List;

public class NgayAdapter extends RecyclerView.Adapter<NgayAdapter.NgayViewHolder> {

    private List<Ngay> danhSachNgay; // mList -> danhSachNgay

    public NgayAdapter(List<Ngay> danhSachNgay) {
        this.danhSachNgay = danhSachNgay;
    }

    @NonNull
    @Override
    public NgayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ngay, parent, false);
        return new NgayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NgayViewHolder holder, int position) {
        Ngay ngay = danhSachNgay.get(position);

        holder.tvNgay.setText(ngay.getNgay());

        String trangThai = ngay.getTrangThai();

        // Kiểm tra null để tránh lỗi app bị văng nếu status chưa có dữ liệu
        if (trangThai == null) {
            trangThai = "";
        }

        if(trangThai.equals("blue")){
            holder.tvNgay.setBackgroundResource(R.drawable.bg_ngaydahoc);
            holder.tvNgay.setTextColor(Color.WHITE);
        } else if (trangThai.equals("red")) {
            holder.tvNgay.setBackgroundResource(R.drawable.bg_ngaynghi);
            holder.tvNgay.setTextColor(Color.WHITE);
        } else if(trangThai.equals("gray")) {
            holder.tvNgay.setBackgroundResource(R.drawable.bg_ngaychuahoc);
            holder.tvNgay.setTextColor(Color.WHITE);
        } else if (trangThai.equals("grayMonth")) {
            holder.tvNgay.setBackground(null);
            holder.tvNgay.setTextColor(Color.GRAY);
        } else {
            // Ngày bình thường
            holder.tvNgay.setBackground(null);
            holder.tvNgay.setTextColor(Color.parseColor("#2563EB"));
        }

        // Nếu là ô trống thì ẩn text đi
        if(ngay.getNgay().isEmpty()){
            holder.tvNgay.setBackground(null);
        }
    }

    @Override
    public int getItemCount() {
        return danhSachNgay.size();
    }

    public class NgayViewHolder extends RecyclerView.ViewHolder {
        TextView tvNgay;

        public NgayViewHolder(View view) {
            super(view);
            tvNgay = view.findViewById(R.id.tv_day);
        }
    }
}