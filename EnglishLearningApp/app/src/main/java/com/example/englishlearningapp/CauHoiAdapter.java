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

import com.example.englishlearningapp.Model.CauHoi;

import java.util.List;

public class CauHoiAdapter extends RecyclerView.Adapter<CauHoiAdapter.ViewHolder> {

    private final List<CauHoi> cauHois;
    private final OnAnswerSelectedListener listener;
    // Đảm bảo bạn có file R.layout.item_question_option trong dự án

    public interface OnAnswerSelectedListener {
        void onAnswerSelected(int questionId, String selectedAnswer);
    }

    public CauHoiAdapter(List<CauHoi> cauHois, OnAnswerSelectedListener listener) {
        this.cauHois = cauHois;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sử dụng layout item đã được dọn dẹp
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CauHoi cauHoi = cauHois.get(position);
        holder.bind(cauHoi);
    }

    @Override
    public int getItemCount() {
        return cauHois.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_HuongDan;
        private final TextView tv_NoiDungCauHoi;
        private final LinearLayout khungLuaChon;
        private final Context context;
        // KHÔNG CÓ Ánh xạ Views ProgressBar và Count Text ở đây

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            // Ánh xạ các Views nội dung câu hỏi
            tv_HuongDan = itemView.findViewById(R.id.instruction_text);
            tv_NoiDungCauHoi = itemView.findViewById(R.id.question_sentence_text);
            khungLuaChon = itemView.findViewById(R.id.options_container);

            // Đã xóa Ánh xạ: itemQuestionCountText, itemProgressPercentText, itemMainProgressBar
        }

        public void bind(CauHoi cauHoi) {
            // KHÔNG CÓ LOGIC CẬP NHẬT PROGRESS BAR Ở ĐÂY

            tv_HuongDan.setText(cauHoi.getInstruction());
            tv_NoiDungCauHoi.setText(cauHoi.getSentence());

            khungLuaChon.removeAllViews();

            for (String option : cauHoi.getOptions()) {
                // Sử dụng R.layout.item_question_option (cần tạo file này)
                View optionView = LayoutInflater.from(context).inflate(R.layout.item_question_option, khungLuaChon, false);

                TextView optionText = optionView.findViewById(R.id.option_text);
                RadioButton optionRadio = optionView.findViewById(R.id.option_radio);

                optionText.setText(option);

                // Thiết lập trạng thái và style (Cần file drawable)
                optionView.setBackgroundResource(R.drawable.option_background_selector);
                optionRadio.setChecked(option.equals(cauHoi.getSelectedAnswer()));

                // Xử lý click
                optionView.setOnClickListener(v -> {
                    cauHois.get(getAdapterPosition()).setSelectedAnswer(option);
                    if (listener != null) {
                        // Gọi Activity để cập nhật ProgressBar toàn cục
                        listener.onAnswerSelected(cauHoi.getId(), option);
                    }
                    notifyItemChanged(getAdapterPosition());
                });

                khungLuaChon.addView(optionView);
            }
        }
    }
}