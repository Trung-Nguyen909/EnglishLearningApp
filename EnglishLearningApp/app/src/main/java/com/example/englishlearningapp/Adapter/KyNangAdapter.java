package com.example.englishlearningapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.englishlearningapp.DTO.Response.KyNangResponse;
import com.example.englishlearningapp.R;

import java.util.List;

public class KyNangAdapter extends RecyclerView.Adapter<KyNangAdapter.KyNangViewHolder> {

    private Context boiCanh;
    private List<KyNangResponse> danhSachKyNang;
    private LangNgheSuKienClick nguoiLangNghe;

    public interface LangNgheSuKienClick {
        void khiClickVaoItem(KyNangResponse kyNang);
    }

    // 2. Hàm để Fragment gọi set sự kiện
    public void setLangNgheSuKienClick(LangNgheSuKienClick nguoiLangNghe) {
        this.nguoiLangNghe = nguoiLangNghe;
    }

    public KyNangAdapter(Context boiCanh, List<KyNangResponse> danhSachKyNang) {
        this.boiCanh = boiCanh;
        this.danhSachKyNang = danhSachKyNang;
    }

    @NonNull
    @Override
    public KyNangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ky_nang, parent, false);
        return new KyNangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KyNangViewHolder holder, int position) {
        KyNangResponse kyNang = danhSachKyNang.get(position);
        if (kyNang == null) return;

        // Gán tên từ API
        holder.tvTenKyNang.setText(kyNang.getTenKyNang());

        // --- LOGIC MỚI: TỰ CHỌN ẢNH DỰA TRÊN TÊN ---
        String ten = kyNang.getTenKyNang().toLowerCase();
        int resId = R.drawable.ic_loi; // Ảnh mặc định

        if (ten.contains("nghe")) {
            resId = R.drawable.ic_nghe;
        } else if (ten.contains("nói")) {
            resId = R.drawable.ic_noi;
        } else if (ten.contains("đọc")) {
            resId = R.drawable.ic_doc;
        } else if (ten.contains("viết")) {
            resId = R.drawable.ic_viet;
        }

        holder.imgAnhDaiDien.setImageResource(resId);
        // ---------------------------------------------

        // Sự kiện click (Giữ nguyên)
        holder.itemView.setOnClickListener(v -> {
            if (nguoiLangNghe != null) nguoiLangNghe.khiClickVaoItem(kyNang);
        });
    }

    @Override
    public int getItemCount() {
        if (danhSachKyNang != null) return danhSachKyNang.size();
        return 0;
    }
    public class KyNangViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgAnhDaiDien;
        private TextView tvTenKyNang;

        public KyNangViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAnhDaiDien = itemView.findViewById(R.id.img_icon);
            tvTenKyNang = itemView.findViewById(R.id.tv_tieude);
        }
    }
}