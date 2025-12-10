package com.example.englishlearningapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.englishlearningapp.Model.BaiTapModel;
import java.util.List;

public class LichSuLamBaiAdapter extends RecyclerView.Adapter<LichSuLamBaiAdapter.ViewHolder> {
    private List<BaiTapModel> baiTapList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(BaiTapModel baiTap, int position);
    }

    public LichSuLamBaiAdapter(Context context, List<BaiTapModel> baiTapList) {
        this.context = context;
        this.baiTapList = baiTapList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lich_su_lam_bai, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiTapModel baiTap = baiTapList.get(position);

        // Set icon
        holder.itemIcon.setImageResource(baiTap.getIconResId());

        // Set title
        holder.itemTitle.setText(baiTap.getTitle());

        // Set date
        holder.itemDate.setText(baiTap.getDate());

        // Set status text and color
        holder.itemStatus.setText(baiTap.getStatusText());
        holder.itemStatus.setTextColor(context.getResources().getColor(baiTap.getStatusColorRes(), null));

        // Set status icon
        if (baiTap.isError()) {
            holder.statusIcon.setImageResource(R.drawable.ic_loi);
        } else {
            holder.statusIcon.setImageResource(R.drawable.ic_tick);
        }

        // Change card style for error items
        if (baiTap.isError()) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white, null));
        } else {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white, null));
        }

        // Set click listener
        holder.cardView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(baiTap, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return baiTapList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemIcon, statusIcon;
        TextView itemTitle, itemDate, itemStatus;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemIcon = itemView.findViewById(R.id.itemIcon);
            statusIcon = itemView.findViewById(R.id.statusIcon);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemDate = itemView.findViewById(R.id.itemDate);
            itemStatus = itemView.findViewById(R.id.itemStatus);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}

