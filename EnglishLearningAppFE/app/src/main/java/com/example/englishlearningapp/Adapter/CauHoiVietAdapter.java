package com.example.englishlearningapp.Adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.DTO.Response.CauHoiVietResponse;
import com.example.englishlearningapp.R;

import java.util.List;

public class CauHoiVietAdapter extends RecyclerView.Adapter<CauHoiVietAdapter.BaiTapVietViewHolder> {

    private List<CauHoiVietResponse> danhSachCauHoi;
    private LangNgheSuKienViet nguoiLangNghe;

    // 1. Định nghĩa Interface ngay trong Adapter (Giống BaiTapNoiAdapter)
    public interface LangNgheSuKienViet {
        void khiDapAnDuocChon(int maCauHoi, String dapAn);
    }

    public CauHoiVietAdapter(List<CauHoiVietResponse> danhSach, LangNgheSuKienViet nguoiLangNghe) {
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
        CauHoiVietResponse cauHoi = danhSachCauHoi.get(position);

        // 1. Set hướng dẫn và nội dung câu hỏi
        holder.tvHuongDan.setText("Writing Task");
        holder.tvNoiDung.setText(cauHoi.getDeBai()); // Lấy đề bài từ Model mới

        // 2. Xử lý TextWatcher để tránh lỗi khi cuộn
        if (holder.boTheoDoiVanBan != null) {
            holder.etCauTraLoi.removeTextChangedListener(holder.boTheoDoiVanBan);
        }

        // Hiển thị lại đáp án cũ
        holder.etCauTraLoi.setText(cauHoi.getDapAnNguoiDung());

        // Cập nhật số từ ban đầu
        capNhatDemTu(holder, cauHoi.getDapAnNguoiDung());

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
                cauHoi.setDapAnNguoiDung(vanBan);

                // Cập nhật số từ
                capNhatDemTu(holder, vanBan);

                // Gửi sự kiện về Activity (Trim khi gửi đi để check rỗng)
                if (nguoiLangNghe != null) {
                    if (!vanBan.trim().isEmpty()) {
                        nguoiLangNghe.khiDapAnDuocChon(cauHoi.getId(), vanBan.trim());
                    } else {
                        nguoiLangNghe.khiDapAnDuocChon(cauHoi.getId(), null);
                    }
                }
            }
        };
        holder.etCauTraLoi.addTextChangedListener(holder.boTheoDoiVanBan);
    }

    private void capNhatDemTu(BaiTapVietViewHolder holder, String text) {
        if (text == null || text.trim().isEmpty()) {
            holder.tvDemTu.setText("Số từ: 0");
        } else {
            int soTu = text.trim().split("\\s+").length;
            holder.tvDemTu.setText("Số từ: " + soTu);
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