package com.example.englishlearningapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private final List<Topic> topicList;
    private final Context context;

    public TopicAdapter(Context context, List<Topic> topicList) {
        this.context = context;
        this.topicList = topicList;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sử dụng layout item_category_row_course.xml của bạn
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_row_course, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        Topic topic = topicList.get(position);

        // 1. Gán dữ liệu
        holder.topicName.setText(topic.getName());
        holder.topicIcon.setImageResource(topic.getIconResId());

        // 2. LOGIC ĐỔI MÀU NỀN XEN KẼ
        if (position % 2 == 0) {
            // Vị trí chẵn (0, 2, 4, ...) -> Màu #DFF5FF
            holder.itemLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.even_item_color));
        } else {
            // Vị trí lẻ (1, 3, 5, ...) -> Màu trắng (#FFFFFF)
            holder.itemLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.odd_item_color));
        }
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    // ViewHolder class
    public static class TopicViewHolder extends RecyclerView.ViewHolder {
        // Hãy đảm bảo các ID này khớp với item_category_row_course.xml của bạn
        final LinearLayout itemLayout;
        final TextView topicName;
        final ImageView topicIcon;
        // final ImageView dropdownIcon; // Nếu cần

        public TopicViewHolder(View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.linear_layout_container);
            topicName = itemView.findViewById(R.id.text_category_name);
            topicIcon = itemView.findViewById(R.id.icon_category);
            // dropdownIcon = itemView.findViewById(R.id.icon_dropdown);
        }
    }
}