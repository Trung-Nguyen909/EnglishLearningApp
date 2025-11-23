package com.example.englishlearningapp;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Model.CauHoiModel;

import java.util.List;

public class BaiTapVietAdapter extends RecyclerView.Adapter<BaiTapVietAdapter.BaiTapVietViewHolder> {

    private List<CauHoiModel> danhSachCauHoi;
    private LangNgheSuKienViet nguoiLangNghe;

    // 1. Định nghĩa Interface ngay trong Adapter (Giống BaiTapNoiAdapter)
    public interface LangNgheSuKienViet {
        void khiDapAnDuocChon(int maCauHoi, String dapAn);
    }

    public BaiTapVietAdapter(List<CauHoiModel> danhSach, LangNgheSuKienViet nguoiLangNghe) {
        this.danhSachCauHoi = danhSach;
        this.nguoiLangNghe = nguoiLangNghe;
    }

    @NonNull
    @Override
    public BaiTapVietViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cau_hoi_viet, parent, false);
        return new BaiTapVietViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaiTapVietViewHolder holder, int position) {
        CauHoiModel cauHoi = danhSachCauHoi.get(position);

        // 1. Set hướng dẫn và nội dung câu hỏi
        holder.tvHuongDan.setText(cauHoi.getHuongDan());
        holder.tvNoiDung.setText("Câu " + (position + 1) + ": " + cauHoi.getNoiDung());

        // 2. Xử lý TextWatcher để tránh lỗi khi cuộn
        if (holder.boTheoDoiVanBan != null) {
            holder.etCauTraLoi.removeTextChangedListener(holder.boTheoDoiVanBan);
        }

        // Hiển thị lại đáp án cũ
        holder.etCauTraLoi.setText(cauHoi.getDapAnDaChon());

        // Cập nhật số từ ban đầu
        capNhatDemTu(holder, cauHoi.getDapAnDaChon());

        // 3. Tạo bộ theo dõi mới
        holder.boTheoDoiVanBan = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String vanBan = s.toString(); // Không trim ngay để giữ trải nghiệm gõ

                // Lưu vào model
                cauHoi.setDapAnDaChon(vanBan);

                // Cập nhật số từ
                capNhatDemTu(holder, vanBan);

                // Gửi sự kiện về Activity (Trim khi gửi đi để check rỗng)
                if (nguoiLangNghe != null) {
                    if (!vanBan.trim().isEmpty()) {
                        nguoiLangNghe.khiDapAnDuocChon(cauHoi.getMaCauHoi(), vanBan.trim());
                    } else {
                        nguoiLangNghe.khiDapAnDuocChon(cauHoi.getMaCauHoi(), null);
                    }
                }
            }
        };
        holder.etCauTraLoi.addTextChangedListener(holder.boTheoDoiVanBan);
    }

    private void capNhatDemTu(BaiTapVietViewHolder holder, String text) {
        if (text == null || text.trim().isEmpty()) {
            holder.tvDemTu.setText("Số từ: 0 / 50-100 từ");
        } else {
            int soTu = text.trim().split("\\s+").length;
            holder.tvDemTu.setText("Số từ: " + soTu + " / 50-100 từ");
        }
    }

    @Override
    public int getItemCount() {
        if (danhSachCauHoi != null) return danhSachCauHoi.size();
        return 0;
    }
    public static class BaiTapVietViewHolder extends RecyclerView.ViewHolder {
        TextView tvHuongDan, tvNoiDung, tvDemTu;
        EditText etCauTraLoi;
        TextWatcher boTheoDoiVanBan;

        public BaiTapVietViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHuongDan = itemView.findViewById(R.id.tv_tieu_de_bai_tap);
            tvNoiDung = itemView.findViewById(R.id.tv_noi_dung_cau_hoi);
            tvDemTu = itemView.findViewById(R.id.tv_dem_tu);
            etCauTraLoi = itemView.findViewById(R.id.edt_cau_tra_loi);
        }
    }
}