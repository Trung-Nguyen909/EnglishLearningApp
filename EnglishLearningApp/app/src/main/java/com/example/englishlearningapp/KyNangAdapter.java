package com.example.englishlearningapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.englishlearningapp.Model.KyNang;
import java.util.List;

public class KyNangAdapter extends RecyclerView.Adapter<KyNangAdapter.TestViewHolder> {
    private Context context;
    private List<KyNang> mList;

    public KyNangAdapter(Context context, List<KyNang> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public KyNangAdapter.TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kynang, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KyNangAdapter.TestViewHolder holder, int position) {
        KyNang item = mList.get(position);
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
