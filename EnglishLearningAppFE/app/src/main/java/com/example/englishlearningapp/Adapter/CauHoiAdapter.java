package com.example.englishlearningapp.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.DTO.Response.CauHoiResponse; // Import đúng file bạn gửi
import com.example.englishlearningapp.R;

import java.util.ArrayList;
import java.util.List;

public class CauHoiAdapter extends RecyclerView.Adapter<CauHoiAdapter.ViewHolder> {

    private final List<CauHoiResponse> danhSachCauHoi;
    private final LangNgheSuKienChonDapAn suKienLangNghe;
    private Context context;

    // Interface để gửi sự kiện ra Activity khi người dùng chọn đáp án
    public interface LangNgheSuKienChonDapAn {
        void khiDapAnDuocChon(int maCauHoi, String dapAnDuocChon);
    }

    public CauHoiAdapter(Context context, List<CauHoiResponse> danhSachCauHoi) {
        this.context = context;
        this.danhSachCauHoi = danhSachCauHoi;

        // Kiểm tra xem Activity có implement interface không
        if (context instanceof LangNgheSuKienChonDapAn) {
            this.suKienLangNghe = (LangNgheSuKienChonDapAn) context;
        } else {
            this.suKienLangNghe = null;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cauhoi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CauHoiResponse cauHoi = danhSachCauHoi.get(position);
        holder.ganDuLieu(cauHoi);
    }

    @Override
    public int getItemCount() {
        if (danhSachCauHoi == null) return 0;
        return danhSachCauHoi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvHuongDan;
        private final TextView tvNoiDungCauHoi;
        private final LinearLayout khungChuaLuaChon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHuongDan = itemView.findViewById(R.id.tv_huong_dan);
            tvNoiDungCauHoi = itemView.findViewById(R.id.tv_noi_dung_cau_hoi);
            khungChuaLuaChon = itemView.findViewById(R.id.khung_chua_lua_chon);
        }

        public void ganDuLieu(CauHoiResponse cauHoi) {
            // 1. Hiển thị hướng dẫn
            String loaiCH = cauHoi.getLoaiCauHoi();
            if (loaiCH != null) {
                switch (loaiCH) {
                    case "Listening":
                        tvHuongDan.setText("Nghe và chọn đáp án đúng:");
                        break;
                    case "Speaking":
                        tvHuongDan.setText("Luyện nói theo mẫu câu:");
                        break;
                    case "FillBlank":
                        tvHuongDan.setText("Điền vào chỗ trống:");
                        break;
                    default:
                        tvHuongDan.setText("Câu hỏi số " + (getAdapterPosition() + 1));
                        break;
                }
            }

            // 2. Hiển thị nội dung câu hỏi
            tvNoiDungCauHoi.setText(cauHoi.getNoiDung());

            // 3. Xử lý các lựa chọn
            // Vì Class CauHoiResponse lưu đáp án rời rạc (A, B, C, D), ta cần gom lại để hiển thị
            List<String> listDapAn = new ArrayList<>();
            if (cauHoi.getDapAnA() != null && !cauHoi.getDapAnA().isEmpty()) listDapAn.add(cauHoi.getDapAnA());
            if (cauHoi.getDapAnB() != null && !cauHoi.getDapAnB().isEmpty()) listDapAn.add(cauHoi.getDapAnB());
            if (cauHoi.getDapAnC() != null && !cauHoi.getDapAnC().isEmpty()) listDapAn.add(cauHoi.getDapAnC());
            if (cauHoi.getDapAnD() != null && !cauHoi.getDapAnD().isEmpty()) listDapAn.add(cauHoi.getDapAnD());

            // Xóa view cũ để tránh trùng lặp khi cuộn
            khungChuaLuaChon.removeAllViews();

            // Duyệt qua từng đáp án để tạo View
            for (String luaChon : listDapAn) {
                View viewLuaChon = LayoutInflater.from(context).inflate(R.layout.item_luachon_cauhoi, khungChuaLuaChon, false);

                TextView tvNoiDungLuaChon = viewLuaChon.findViewById(R.id.thongtin_luachon);
                RadioButton radioNutChon = viewLuaChon.findViewById(R.id.nut_lua_chon);

                tvNoiDungLuaChon.setText(luaChon);

                // Kiểm tra xem đáp án này có phải là đáp án người dùng đã chọn không
                boolean isSelected = luaChon.equals(cauHoi.getDapAnDaChon());
                radioNutChon.setChecked(isSelected);

                // Đổi màu nền nếu được chọn (Tùy chọn thẩm mỹ)
                if (isSelected) {
                    // Bạn cần tạo drawable/bg_selected.xml hoặc dùng màu cứng
                    viewLuaChon.setBackgroundColor(Color.parseColor("#E0F7FA")); // Màu xanh nhạt
                } else {
                    viewLuaChon.setBackgroundColor(Color.TRANSPARENT);
                }

                // Bắt sự kiện Click vào dòng đáp án
                viewLuaChon.setOnClickListener(v -> {
                    // Cập nhật đáp án đã chọn vào Model
                    cauHoi.setDapAnDaChon(luaChon);

                    // Gửi sự kiện ra ngoài Activity (để tính điểm, cập nhật progress bar)
                    if (suKienLangNghe != null) {
                        suKienLangNghe.khiDapAnDuocChon(cauHoi.getId(), luaChon);
                    }

                    // Refresh lại item này để cập nhật giao diện (Radio button)
                    notifyItemChanged(getAdapterPosition());
                });

                // Vô hiệu hóa click trực tiếp vào RadioButton để tránh xung đột sự kiện
                radioNutChon.setClickable(false);

                // Thêm view vào khung
                khungChuaLuaChon.addView(viewLuaChon);
            }
        }
    }
}