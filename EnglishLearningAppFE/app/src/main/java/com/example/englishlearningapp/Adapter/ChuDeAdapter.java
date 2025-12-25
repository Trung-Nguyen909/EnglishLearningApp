package com.example.englishlearningapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Activity.LessonVocabularyActivity;
import com.example.englishlearningapp.DTO.Response.BaiHocResponse;
import com.example.englishlearningapp.Model.ChuDeModel;
import com.example.englishlearningapp.R;

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
        danhSachHienThi.addAll(danhSachChuDeGoc);
    }

    @Override
    public int getItemViewType(int position) {
        return (danhSachHienThi.get(position) instanceof ChuDeModel) ? TYPE_CHU_DE : TYPE_MUC_CON;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_CHU_DE) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chude, parent, false);
            return new ChuDeViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chude_phu, parent, false);
            return new MucConViewHolder(view);
        }
    }

    // ✅ map tên drawable (vd: "img_ic_animal_course") -> R.drawable.xxx
    private int getDrawableId(String drawableName, int defaultRes) {
        if (drawableName == null || drawableName.trim().isEmpty()) return defaultRes;

        int resId = context.getResources()
                .getIdentifier(drawableName, "drawable", context.getPackageName());

        return resId != 0 ? resId : defaultRes;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_CHU_DE) {

            // --- CHỦ ĐỀ (CHA) ---
            ChuDeModel chuDe = (ChuDeModel) danhSachHienThi.get(position);
            ChuDeViewHolder h = (ChuDeViewHolder) holder;

            h.tenChuDe.setText(chuDe.getTenChuDe());
            h.iconChuDe.setImageResource(chuDe.getIdIcon()); // icon cha đã set từ Fragment

            View.OnClickListener expandListener = v -> {
                if (chuDe.getDanhSachMucCon() == null || chuDe.getDanhSachMucCon().isEmpty()) return;

                int index = danhSachHienThi.indexOf(chuDe);
                if (index < 0) return;

                if (!chuDe.isMoRong()) {
                    chuDe.setMoRong(true);
                    danhSachHienThi.addAll(index + 1, chuDe.getDanhSachMucCon());
                    notifyItemRangeInserted(index + 1, chuDe.getDanhSachMucCon().size());
                } else {
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
            h.khungItem.setOnClickListener(expandListener);

        } else {

            // --- MỤC CON (BÀI HỌC) ---
            BaiHocResponse mucCon = (BaiHocResponse) danhSachHienThi.get(position);
            MucConViewHolder h = (MucConViewHolder) holder;

            String ten = (mucCon.getTenBaiHoc() != null && !mucCon.getTenBaiHoc().trim().isEmpty())
                    ? mucCon.getTenBaiHoc()
                    : "Bài học";
            h.tenMucCon.setText(ten);

            // DB trả về ví dụ: "img_ic_family_activityday"
            int iconCon = getDrawableId(
                    mucCon.getIconUrl(),
                    R.drawable.img_ic_family_activityday // default
            );
            h.iconMucCon.setImageResource(iconCon);

            h.khungItem.setBackgroundColor(ContextCompat.getColor(context, R.color.sub_item_color));

            // CLICK -> mở LessonVocabularyActivity
            h.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, LessonVocabularyActivity.class);

                Integer idBai = mucCon.getId(); //  nếu BaiHocResponse có getId()
                Log.d("idbaihoc", String.valueOf(idBai));

                intent.putExtra("BAIHOC_ID", idBai != null ? idBai : -1);
                intent.putExtra("TEN_BAI_HOC", ten);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return danhSachHienThi.size();
    }

    public void setData(List<ChuDeModel> newData) {
        danhSachChuDeGoc.clear();
        if (newData != null) danhSachChuDeGoc.addAll(newData);

        danhSachHienThi.clear();
        danhSachHienThi.addAll(danhSachChuDeGoc);

        notifyDataSetChanged();
    }

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
