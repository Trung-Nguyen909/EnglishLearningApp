package com.example.englishlearningapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// Import Model (Nhớ thay bằng tên Model thực tế của bạn nếu khác)
import com.example.englishlearningapp.Model.CauHoiNoiModel;

import java.util.List;
public class BaiTapNoiAdapter extends RecyclerView.Adapter<BaiTapNoiAdapter.BaiTapNoiViewHolder> {

    private Context boiCanh;
    private List<CauHoiNoiModel> danhSachCauHoi;
    private LangNgheSuKienItem nguoiLangNghe;

    public interface LangNgheSuKienItem {
        void khiAnGhiAm(int viTri);
        void khiAnNghe(String noiDung);
    }

    public BaiTapNoiAdapter(Context boiCanh, List<CauHoiNoiModel> danhSachCauHoi, LangNgheSuKienItem nguoiLangNghe) {
        this.boiCanh = boiCanh;
        this.danhSachCauHoi = danhSachCauHoi;
        this.nguoiLangNghe = nguoiLangNghe;
    }

    @NonNull
    @Override
    public BaiTapNoiViewHolder onCreateViewHolder(@NonNull ViewGroup nhomCha, int kieuView) {
        View giaoDien = LayoutInflater.from(boiCanh).inflate(R.layout.item_speaking, nhomCha, false);
        return new BaiTapNoiViewHolder(giaoDien);
    }

    @Override
    // 4. Đổi tham số đầu vào thành BaiTapNoiViewHolder
    public void onBindViewHolder(@NonNull BaiTapNoiViewHolder nguoiGiu, int viTri) {
        CauHoiNoiModel cauHoi = danhSachCauHoi.get(viTri);

        nguoiGiu.tvSoThuTuCau.setText("Câu " + (viTri + 1));
        nguoiGiu.tvCauMau.setText(cauHoi.getCauMau());


        String dapAn = cauHoi.getDapAnNguoiDung();

        if (dapAn != null && !dapAn.isEmpty()) {
            nguoiGiu.tvKetQuaNguoiDung.setVisibility(View.VISIBLE);
            nguoiGiu.tvKetQuaNguoiDung.setText(dapAn);
            nguoiGiu.anhTrangThai.setVisibility(View.VISIBLE);

            if (cauHoi.isChinhXac()) {
                nguoiGiu.tvKetQuaNguoiDung.setTextColor(Color.parseColor("#4CAF50"));
                nguoiGiu.anhTrangThai.setImageResource(R.drawable.ic_check_circle);
            } else {
                nguoiGiu.tvKetQuaNguoiDung.setTextColor(Color.parseColor("#F44336"));
                nguoiGiu.anhTrangThai.setImageResource(R.drawable.ic_error);
            }
        } else {
            nguoiGiu.tvKetQuaNguoiDung.setVisibility(View.GONE);
            nguoiGiu.anhTrangThai.setVisibility(View.GONE);
        }

        nguoiGiu.nutGhiAm.setOnClickListener(v -> {
            if (nguoiLangNghe != null) nguoiLangNghe.khiAnGhiAm(viTri);
        });

        nguoiGiu.nutNghe.setOnClickListener(v -> {
            if (nguoiLangNghe != null) nguoiLangNghe.khiAnNghe(cauHoi.getCauMau());
        });
    }

    @Override
    public int getItemCount() {
        return danhSachCauHoi.size();
    }
    public static class BaiTapNoiViewHolder extends RecyclerView.ViewHolder {
        TextView tvSoThuTuCau, tvCauMau, tvKetQuaNguoiDung, tvPhienAm;
        ImageView nutGhiAm, nutNghe, anhTrangThai;

        public BaiTapNoiViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSoThuTuCau = itemView.findViewById(R.id.tv_so_thu_tu_cau);
            tvCauMau = itemView.findViewById(R.id.tv_cau_mau);
            tvKetQuaNguoiDung = itemView.findViewById(R.id.tv_ket_qua_nguoi_dung);
            tvPhienAm = itemView.findViewById(R.id.tv_phien_am);

            nutGhiAm = itemView.findViewById(R.id.nut_ghi_am);
            nutNghe = itemView.findViewById(R.id.nut_nghe);
            anhTrangThai = itemView.findViewById(R.id.anh_trang_thai);
        }
    }
}