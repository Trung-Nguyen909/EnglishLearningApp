package com.example.englishlearningapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Model.ItemNgay;

import java.util.List;

public class NgayAdapter extends RecyclerView.Adapter<NgayAdapter.NgayViewHolder> {

    private List<ItemNgay> mList;

    public NgayAdapter(List<ItemNgay> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public NgayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ngay, parent, false);
        return new NgayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NgayViewHolder holder, int position) {
        ItemNgay date = mList.get(position);

        holder.tvDay.setText(date.getDay());

        if(date.getStatus().equals("blue")){
            holder.tvDay.setBackgroundResource(R.drawable.bg_ngaydahoc);
            holder.tvDay.setTextColor(Color.WHITE);
        } else if (date.getStatus().equals("red")) {
            holder.tvDay.setBackgroundResource(R.drawable.bg_ngaynghi);
            holder.tvDay.setTextColor(Color.WHITE);
        } else if(date.getStatus().equals("gray")) {
            holder.tvDay.setBackgroundResource(R.drawable.bg_ngaychuahoc);
            holder.tvDay.setTextColor(Color.WHITE);
        } else if (date.getStatus().equals("grayMonth")) {
            holder.tvDay.setBackground(null);
            holder.tvDay.setTextColor(Color.GRAY);
        } else {
            // Ngày bình thường
            holder.tvDay.setBackground(null);
            holder.tvDay.setTextColor(Color.parseColor("#2563EB"));
        }

        // Nếu là ô trống thì ẩn text đi
        if(date.getDay().isEmpty()){
            holder.tvDay.setBackground(null);
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class NgayViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay;
        public NgayViewHolder(View view) {
            super(view);
            tvDay = view.findViewById((R.id.tv_day));
        }
    }
}
