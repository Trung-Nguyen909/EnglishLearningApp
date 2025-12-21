package com.example.englishlearningapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.DTO.Response.BaiHocResponse;
import com.example.englishlearningapp.Model.ChuDeModel;

import java.util.ArrayList;
import java.util.List;

public class ChuDeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_CHU_DE = 0;
    private static final int TYPE_MUC_CON = 1;

    private final Context context;
    private final List<Object> danhSachHienThi = new ArrayList<>();
    private final List<ChuDeModel> danhSachChuDeGoc;

    public ChuDeAdapter(Context context, List<ChuDeModel> danhSachChuDe) {
        this.context = context;
        this.danhSachChuDeGoc = new ArrayList<>(danhSachChuDe);
        // ban đầu fill danhSachHienThi với danhSachChuDeGoc
        danhSachHienThi.addAll(danhSachChuDeGoc);
    }

    @Override
    public int getItemViewType(int position) {
        if (danhSachHienThi.get(position) instanceof ChuDeModel) return TYPE_CHU_DE;
        else return TYPE_MUC_CON;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_CHU_DE) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_chude, parent, false);
            return new ChuDeViewHolder(view);
        } else {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_chude_phu, parent, false);
            return new MucConViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_CHU_DE) {

            // --- XỬ LÝ CHỦ ĐỀ (MỤC CHA) ---
            ChuDeModel chuDe = (ChuDeModel) danhSachHienThi.get(position);
            ChuDeViewHolder h = (ChuDeViewHolder) holder;

            h.tenChuDe.setText(chuDe.getTenChuDe());
            h.iconChuDe.setImageResource(chuDe.getIdIcon());

            // sự kiện mở/thu gọn
            View.OnClickListener expandListener = v -> {
                if (chuDe.getDanhSachMucCon() == null || chuDe.getDanhSachMucCon().isEmpty()) {
                    return;
                }

                int index = danhSachHienThi.indexOf(chuDe);
                if (index < 0) return;

                if (!chuDe.isMoRong()) {
                    // MỞ RỘNG - thêm tất cả bài học (BaiHocResponse) vào danhSachHienThi
                    chuDe.setMoRong(true);
                    danhSachHienThi.addAll(index + 1, chuDe.getDanhSachMucCon());
                    notifyItemRangeInserted(index + 1, chuDe.getDanhSachMucCon().size());
                } else {
                    // THU GỌN - xóa các mục con
                    chuDe.setMoRong(false);
                    int soLuong = chuDe.getDanhSachMucCon().size();
                    for (int i = 0; i < soLuong; i++) {
                        if (danhSachHienThi.size() > index + 1) {
                            danhSachHienThi.remove(index + 1);
                        }
                    }
                    notifyItemRangeRemoved(index + 1, soLuong);
                }
            };

            h.iconDropdown.setOnClickListener(expandListener);
            h.khungItem.setOnClickListener(expandListener); // cho phép click cả khung

        } else {

            // --- XỬ LÝ MỤC CON (BÀI HỌC) ---
            BaiHocResponse mucCon = (BaiHocResponse) danhSachHienThi.get(position);
            MucConViewHolder h = (MucConViewHolder) holder;

            // chỉnh tên hiển thị - đổi tên method nếu DTO khác
            String ten = null;
            try {
                ten = mucCon.getTenBaiHoc(); // <-- nếu method khác, đổi lại
            } catch (Exception ex) {
                // fallback: thử gọi getTen() hoặc getName() nếu DTO khác (bạn có thể sửa trực tiếp cho đúng)
                try {
                    ten = (String) mucCon.getClass().getMethod("getTen").invoke(mucCon);
                } catch (Exception e) {
                    ten = "Bài học";
                }
            }

            h.tenMucCon.setText(ten);

            // nếu DTO có icon -> không chắc chắn, dùng icon mặc định
            h.iconMucCon.setImageResource(R.drawable.img_ic_family_activityday);

            h.khungItem.setBackgroundColor(ContextCompat.getColor(context, R.color.sub_item_color));

            // CLICK VÀO BÀI HỌC -> mở BaiHocActivity
            String finalTen = ten;
            h.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, BaiHocActivity.class);
                // lấy id bài học: assume getId()
                Integer idBai = null;
                try {
                    idBai = (Integer) mucCon.getClass().getMethod("getId").invoke(mucCon);
                } catch (Exception e) {
                    // ignore
                }
                intent.putExtra("SUB_ITEM_ID", idBai != null ? idBai : -1);
                intent.putExtra("SUB_ITEM_NAME", finalTen);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return danhSachHienThi.size();
    }

    // update data an toàn
    public void setData(List<ChuDeModel> newData) {
        danhSachChuDeGoc.clear();
        if (newData != null) danhSachChuDeGoc.addAll(newData);

        danhSachHienThi.clear();
        // ban đầu chỉ thêm các chủ đề (không auto mở)
        danhSachHienThi.addAll(danhSachChuDeGoc);

        notifyDataSetChanged();
    }

    // ViewHolder CHỦ ĐỀ
    public static class ChuDeViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout khungItem;
        final TextView tenChuDe;
        final ImageView iconChuDe;
        final ImageView iconDropdown;

        public ChuDeViewHolder(View itemView) {
            super(itemView);
            khungItem = itemView.findViewById(R.id.khungchua_item);
            tenChuDe = itemView.findViewById(R.id.tv_ten_danhmuc);
            iconChuDe = itemView.findViewById(R.id.icon_danhmuc);
            iconDropdown = itemView.findViewById(R.id.icon_muiten);
        }
    }

    // ViewHolder MỤC CON
    public static class MucConViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout khungItem;
        final TextView tenMucCon;
        final ImageView iconMucCon;

        public MucConViewHolder(View itemView) {
            super(itemView);
            khungItem = itemView.findViewById(R.id.khungchua_item);
            tenMucCon = itemView.findViewById(R.id.tv_ten_danhmuc);
            iconMucCon = itemView.findViewById(R.id.icon_danhmuc);
        }
    }
}
