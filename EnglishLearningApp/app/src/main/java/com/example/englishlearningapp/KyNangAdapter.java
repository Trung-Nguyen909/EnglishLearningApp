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

    // 1. Khai báo Interface để lắng nghe sự kiện
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(KyNang kyNang);
    }

    // 2. Hàm để Fragment gọi set sự kiện
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public KyNangAdapter(Context context, List<KyNang> mList) {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public KyNangAdapter.TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Lưu ý: Bạn đang dùng layout item_kynang
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kynang, parent, false);
        return new TestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KyNangAdapter.TestViewHolder holder, int position) {
        KyNang item = mList.get(position);
        if (item == null) return;

        holder.tvTitle.setText(item.getTitle()); // Hoặc getTenKyNang() tùy model của bạn
        holder.imgIcon.setImageResource(item.getIconResId()); // Hoặc getResourceId()

        // 3. BẮT SỰ KIỆN CLICK VÀO ITEM
        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onItemClick(item); // Gửi item được chọn ra ngoài
            }
        });
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
            // Đảm bảo ID khớp với file xml item_kynang của bạn
            imgIcon = itemView.findViewById(R.id.img_icon); // Kiểm tra lại ID này trong XML
            tvTitle = itemView.findViewById(R.id.tv_title);   // Kiểm tra lại ID này trong XML
        }
    }
}