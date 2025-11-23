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

import com.example.englishlearningapp.Model.ChuDePhuModel;
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
        this.danhSachChuDeGoc = danhSachChuDe;
        danhSachHienThi.addAll(danhSachChuDe);
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
                    .inflate(R.layout.item_category_row_course, parent, false);
            return new ChuDeViewHolder(view);
        } else {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_sub_category_row, parent, false);
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

            int indexChuDe = danhSachChuDeGoc.indexOf(chuDe);
            int mauNen = (indexChuDe % 2 == 0)
                    ? ContextCompat.getColor(context, R.color.even_item_color)
                    : ContextCompat.getColor(context, R.color.odd_item_color);

            h.khungItem.setBackgroundColor(mauNen);

            // --- SỬA LỖI MỞ RỘNG TẠI ĐÂY ---
            // Nên cho phép bấm vào cả cái khung (khungItem) hoặc iconDropdown
            View.OnClickListener expandListener = v -> {
                // 1. KIỂM TRA NULL: Nếu không có danh sách con thì dừng ngay, tránh crash
                if (chuDe.getDanhSachMucCon() == null || chuDe.getDanhSachMucCon().isEmpty()) {
                    return;
                }

                int index = danhSachHienThi.indexOf(chuDe);
                if (index < 0) return; // Kiểm tra an toàn

                if (!chuDe.isMoRong()) {
                    // MỞ RỘNG
                    chuDe.setMoRong(true);
                    danhSachHienThi.addAll(index + 1, chuDe.getDanhSachMucCon());
                    notifyItemRangeInserted(index + 1, chuDe.getDanhSachMucCon().size());
                } else {
                    // THU GỌN
                    chuDe.setMoRong(false);
                    int soLuong = chuDe.getDanhSachMucCon().size();

                    // Xóa an toàn từng phần tử
                    if (danhSachHienThi.size() > index + 1) {
                        for (int i = 0; i < soLuong; i++) {
                            if (danhSachHienThi.size() > index + 1) {
                                danhSachHienThi.remove(index + 1);
                            }
                        }
                        notifyItemRangeRemoved(index + 1, soLuong);
                    }
                }
            };

            // Gán sự kiện cho cả icon mũi tên
            h.iconDropdown.setOnClickListener(expandListener);

        } else {

            // --- XỬ LÝ MỤC CON (BÀI HỌC) ---
            ChuDePhuModel mucCon = (ChuDePhuModel) danhSachHienThi.get(position);
            MucConViewHolder h = (MucConViewHolder) holder;

            h.tenMucCon.setText(mucCon.getTenChuDePhu());

            h.iconMucCon.setImageResource(mucCon.getMaIcon());

            h.khungItem.setBackgroundColor(ContextCompat.getColor(context, R.color.sub_item_color));

            // >>> CLICK VÀO BÀI HỌC <<<
            h.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, BaiHocActivity.class);
                intent.putExtra("SUB_ITEM_ID", mucCon.getMaChuDePhu());
                intent.putExtra("SUB_ITEM_NAME", mucCon.getTenChuDePhu());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return danhSachHienThi.size();
    }

    // ViewHolder CHỦ ĐỀ
    public static class ChuDeViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout khungItem;
        final TextView tenChuDe;
        final ImageView iconChuDe;
        final ImageView iconDropdown;

        public ChuDeViewHolder(View itemView) {
            super(itemView);
            khungItem = itemView.findViewById(R.id.linear_layout_container);
            tenChuDe = itemView.findViewById(R.id.text_category_name);
            iconChuDe = itemView.findViewById(R.id.icon_category);
            iconDropdown = itemView.findViewById(R.id.icon_dropdown);
        }
    }

    // ViewHolder MỤC CON
    public static class MucConViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout khungItem;
        final TextView tenMucCon;
        final ImageView iconMucCon;

        public MucConViewHolder(View itemView) {
            super(itemView);
            khungItem = itemView.findViewById(R.id.linear_layout_container);
            tenMucCon = itemView.findViewById(R.id.text_category_name);
            iconMucCon = itemView.findViewById(R.id.icon_category);
        }
    }
}