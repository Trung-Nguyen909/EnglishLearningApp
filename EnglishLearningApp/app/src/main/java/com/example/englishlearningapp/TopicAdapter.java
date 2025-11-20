package com.example.englishlearningapp;

import android.content.Context;
import android.content.Intent; // <--- THÊM DÒNG NÀY
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.englishlearningapp.Model.SubItem;
import com.example.englishlearningapp.Model.Topic;

import java.util.ArrayList;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TOPIC = 0;
    private static final int TYPE_SUB_ITEM = 1;

    private final Context context;
    private final List<Object> displayList = new ArrayList<>();
    private final List<Topic> topicOriginalList;

    public TopicAdapter(Context context, List<Topic> topicList) {
        this.context = context;
        this.topicOriginalList = topicList;
        displayList.addAll(topicList);
    }

    @Override
    public int getItemViewType(int position) {
        if (displayList.get(position) instanceof Topic) return TYPE_TOPIC;
        else return TYPE_SUB_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_TOPIC) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_category_row_course, parent, false);
            return new TopicViewHolder(view);
        } else {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_sub_category_row, parent, false);
            return new SubItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_TOPIC) {
            // --- XỬ LÝ TOPIC (CHA) ---
            Topic topic = (Topic) displayList.get(position);
            TopicViewHolder topicHolder = (TopicViewHolder) holder;

            topicHolder.topicName.setText(topic.getName());
            topicHolder.topicIcon.setImageResource(topic.getIconResId());

            int topicIndex = topicOriginalList.indexOf(topic);
            int bgColor = (topicIndex % 2 == 0) ?
                    ContextCompat.getColor(context, R.color.even_item_color) :
                    ContextCompat.getColor(context, R.color.odd_item_color);
            topicHolder.itemLayout.setBackgroundColor(bgColor);

            topicHolder.dropdownIcon.setOnClickListener(v -> {
                int index = displayList.indexOf(topic);
                if (!topic.isExpanded()) {
                    topic.setExpanded(true);
                    displayList.addAll(index + 1, topic.getSubItems());
                    notifyItemRangeInserted(index + 1, topic.getSubItems().size());
                } else {
                    topic.setExpanded(false);
                    int subItemCount = topic.getSubItems().size();
                    for (int i = 0; i < subItemCount; i++) displayList.remove(index + 1);
                    notifyItemRangeRemoved(index + 1, subItemCount);
                }
            });

        } else {
            // --- XỬ LÝ SUB-ITEM (CON - BÀI HỌC) ---
            SubItem subItem = (SubItem) displayList.get(position);
            SubItemViewHolder subHolder = (SubItemViewHolder) holder;
            subHolder.subName.setText(subItem.getName());
            subHolder.subIcon.setImageResource(subItem.getIconResId());

            subHolder.itemLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.sub_item_color));

            // >>>>>> BẮT SỰ KIỆN CLICK VÀO BÀI HỌC TẠI ĐÂY <<<<<<
            subHolder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, BaihocActivity.class);

                // Gửi dữ liệu sang màn hình Exercise
                // LƯU Ý: Bạn cần cập nhật Model SubItem thêm phương thức getId() như mình đã nhắc ở bước trước
                intent.putExtra("SUB_ITEM_ID", subItem.getId());
                intent.putExtra("SUB_ITEM_NAME", subItem.getName());

                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return displayList.size();
    }

    // Các Class ViewHolder giữ nguyên
    public static class TopicViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout itemLayout;
        final TextView topicName;
        final ImageView topicIcon;
        final ImageView dropdownIcon;

        public TopicViewHolder(View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.linear_layout_container);
            topicName = itemView.findViewById(R.id.text_category_name);
            topicIcon = itemView.findViewById(R.id.icon_category);
            dropdownIcon = itemView.findViewById(R.id.icon_dropdown);
        }
    }

    public static class SubItemViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout itemLayout;
        final TextView subName;
        final ImageView subIcon;

        public SubItemViewHolder(View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.linear_layout_container);
            subName = itemView.findViewById(R.id.text_category_name);
            subIcon = itemView.findViewById(R.id.icon_category);
        }
    }
}