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

import com.example.englishlearningapp.Interface.LangNgheSuKien;
import com.example.englishlearningapp.Model.CauHoiModel;

import java.util.List;

public class BaiTapVietAdapter extends RecyclerView.Adapter<BaiTapVietAdapter.NguoiGiuView> {

    private List<CauHoiModel> danhSachCauHoi;
    private LangNgheSuKien nguoiLangNghe;

    public BaiTapVietAdapter(List<CauHoiModel> danhSach, LangNgheSuKien nguoiLangNghe) {
        this.danhSachCauHoi = danhSach;
        this.nguoiLangNghe = nguoiLangNghe;
    }

    @NonNull
    @Override
    public NguoiGiuView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Gọi layout item_cau_hoi_viet
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cau_hoi_viet, parent, false);
        return new NguoiGiuView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NguoiGiuView holder, int position) {
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

        // 3. Tạo bộ theo dõi mới
        holder.boTheoDoiVanBan = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String vanBan = s.toString().trim();

                // Lưu vào model
                cauHoi.setDapAnDaChon(vanBan);

                // Cập nhật số từ
                int soTu = vanBan.isEmpty() ? 0 : vanBan.split("\\s+").length;
                holder.tvDemTu.setText("Số từ: " + soTu + " / 50-100 từ");

                // Gửi sự kiện về Activity
                if (!vanBan.isEmpty()) {
                    nguoiLangNghe.khiDapAnDuocChon(cauHoi.getMaCauHoi(), vanBan);
                } else {
                    nguoiLangNghe.khiDapAnDuocChon(cauHoi.getMaCauHoi(), null);
                }
            }
        };
        holder.etCauTraLoi.addTextChangedListener(holder.boTheoDoiVanBan);
    }

    @Override
    public int getItemCount() {
        if (danhSachCauHoi != null) return danhSachCauHoi.size();
        return 0;
    }

    // Class ViewHolder đổi tên thành Tiếng Việt: NguoiGiuView
    public static class NguoiGiuView extends RecyclerView.ViewHolder {
        TextView tvHuongDan, tvNoiDung, tvDemTu;
        EditText etCauTraLoi;
        TextWatcher boTheoDoiVanBan;

        public NguoiGiuView(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ đúng ID tiếng Việt trong XML
            tvHuongDan = itemView.findViewById(R.id.tv_tieu_de_bai_tap);
            tvNoiDung = itemView.findViewById(R.id.tv_noi_dung_cau_hoi);
            tvDemTu = itemView.findViewById(R.id.tv_dem_tu);
            etCauTraLoi = itemView.findViewById(R.id.et_cau_tra_loi);
        }
    }
}