package com.example.englishlearningapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.englishlearningapp.Model.KyNangModel;
import java.util.List;

public class KyNangAdapter extends RecyclerView.Adapter<KyNangAdapter.KyNangViewHolder> {

    private Context boiCanh; // context
    private List<KyNangModel> danhSachKyNang; // mList

    // 1. Khai báo Interface để lắng nghe sự kiện (Tiếng Việt)
    private LangNgheSuKienClick nguoiLangNghe; // mListener

    public interface LangNgheSuKienClick {
        void khiClickVaoItem(KyNangModel kyNang);
    }

    // 2. Hàm để Fragment gọi set sự kiện
    public void setLangNgheSuKienClick(LangNgheSuKienClick nguoiLangNghe) {
        this.nguoiLangNghe = nguoiLangNghe;
    }

    public KyNangAdapter(Context boiCanh, List<KyNangModel> danhSachKyNang) {
        this.boiCanh = boiCanh;
        this.danhSachKyNang = danhSachKyNang;
    }

    @NonNull
    @Override
    public KyNangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kynang, parent, false);
        return new KyNangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KyNangViewHolder holder, int position) {
        KyNangModel kyNang = danhSachKyNang.get(position);
        if (kyNang == null) return;

        // Sử dụng Getter tiếng Việt từ Model KyNang
        holder.tvTenKyNang.setText(kyNang.getTenKyNang());
        holder.imgAnhDaiDien.setImageResource(kyNang.getMaHinhAnh());

        // 3. BẮT SỰ KIỆN CLICK VÀO ITEM
        holder.itemView.setOnClickListener(v -> {
            if (nguoiLangNghe != null) {
                nguoiLangNghe.khiClickVaoItem(kyNang); // Gửi item được chọn ra ngoài
            }
        });
    }

    @Override
    public int getItemCount() {
        if (danhSachKyNang != null) return danhSachKyNang.size();
        return 0;
    }

    // Đổi tên TestViewHolder -> KyNangViewHolder
    public class KyNangViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgAnhDaiDien;
        private TextView tvTenKyNang;

        public KyNangViewHolder(@NonNull View itemView) {
            super(itemView);
            // Lưu ý: Bạn cần đảm bảo ID trong file item_kynang.xml khớp với tên ở đây
            // Ví dụ: android:id="@+id/img_icon"
            imgAnhDaiDien = itemView.findViewById(R.id.img_icon);

            // Ví dụ: android:id="@+id/tv_title"
            tvTenKyNang = itemView.findViewById(R.id.tv_title);
        }
    }
}