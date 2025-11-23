package com.example.englishlearningapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Model.CauHoiModel;

import java.util.List;

public class CauHoiAdapter extends RecyclerView.Adapter<CauHoiAdapter.ViewHolder> {

    private final List<CauHoiModel> danhSachCauHoi;
    private final LangNgheSuKienChonDapAn suKienLangNghe;

    public interface LangNgheSuKienChonDapAn {
        void khiDapAnDuocChon(int maCauHoi, String dapAnDuocChon);
    }

    public CauHoiAdapter(List<CauHoiModel> danhSachCauHoi, LangNgheSuKienChonDapAn suKienLangNghe) {
        this.danhSachCauHoi = danhSachCauHoi;
        this.suKienLangNghe = suKienLangNghe;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CauHoiModel cauHoi = danhSachCauHoi.get(position);
        holder.ganDuLieu(cauHoi);
    }

    @Override
    public int getItemCount() {
        return danhSachCauHoi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvHuongDan;
        private final TextView tvNoiDungCauHoi;
        private final LinearLayout khungChuaLuaChon;
        private final Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            tvHuongDan = itemView.findViewById(R.id.instruction_text);
            tvNoiDungCauHoi = itemView.findViewById(R.id.question_sentence_text);
            khungChuaLuaChon = itemView.findViewById(R.id.options_container);
        }

        public void ganDuLieu(CauHoiModel cauHoi) {
            // SỬA Ở ĐÂY: Dùng get... thay vì lay...
            tvHuongDan.setText(cauHoi.getHuongDan());
            tvNoiDungCauHoi.setText(cauHoi.getNoiDung());

            khungChuaLuaChon.removeAllViews();

            for (String luaChon : cauHoi.getCacLuaChon()) { // Dùng getCacLuaChon()
                View viewLuaChon = LayoutInflater.from(context).inflate(R.layout.item_question_option, khungChuaLuaChon, false);

                TextView tvNoiDungLuaChon = viewLuaChon.findViewById(R.id.option_text);
                RadioButton radioNutChon = viewLuaChon.findViewById(R.id.option_radio);

                tvNoiDungLuaChon.setText(luaChon);
                viewLuaChon.setBackgroundResource(R.drawable.option_background_selector);

                // Dùng getDapAnDaChon()
                radioNutChon.setChecked(luaChon.equals(cauHoi.getDapAnDaChon()));

                viewLuaChon.setOnClickListener(v -> {
                    // Dùng setDapAnDaChon()
                    danhSachCauHoi.get(getAdapterPosition()).setDapAnDaChon(luaChon);

                    if (suKienLangNghe != null) {
                        // Dùng getMaCauHoi()
                        suKienLangNghe.khiDapAnDuocChon(cauHoi.getMaCauHoi(), luaChon);
                    }
                    notifyItemChanged(getAdapterPosition());
                });

                khungChuaLuaChon.addView(viewLuaChon);
            }
        }
    }
}