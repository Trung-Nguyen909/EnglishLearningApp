package com.example.englishlearningapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.englishlearningapp.Model.ItemBaiTap;
import java.util.List;

public class ItemBaiTapAdapter extends RecyclerView.Adapter<ItemBaiTapAdapter.TestViewHolder> {
    private Context context;
    private List<ItemBaiTap> mList;

    public ItemBaiTapAdapter(Context context, List<ItemBaiTap> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ItemBaiTapAdapter.TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_baitap, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemBaiTapAdapter.TestViewHolder holder, int position) {
        ItemBaiTap item = mList.get(position);
        if (item == null) return;

        // Gán dữ liệu vào View
        holder.tvTitle.setText(item.getTitle());
        holder.imgIcon.setImageResource(item.getIconResId());
    }

    @Override
    public int getItemCount() {
        if (mList != null) return mList.size();
        return 0;
    }

    public class TestViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgIcon;
        private TextView tvTitle;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ ID trong file item_baitap.xml
            imgIcon = itemView.findViewById(R.id.img_icon);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }
    }
}
