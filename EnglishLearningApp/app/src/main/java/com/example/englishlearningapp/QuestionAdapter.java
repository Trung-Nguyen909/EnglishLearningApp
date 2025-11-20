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

import com.example.englishlearningapp.Model.Question;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {

    private final List<Question> questions;
    private final OnAnswerSelectedListener listener;
    // Đảm bảo bạn có file R.layout.item_question_option trong dự án

    public interface OnAnswerSelectedListener {
        void onAnswerSelected(int questionId, String selectedAnswer);
    }

    public QuestionAdapter(List<Question> questions, OnAnswerSelectedListener listener) {
        this.questions = questions;
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
        Question question = questions.get(position);
        holder.bind(question);
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView instructionText;
        private final TextView sentenceText;
        private final LinearLayout optionsContainer;
        private final Context context;
        // KHÔNG CÓ Ánh xạ Views ProgressBar và Count Text ở đây

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            // Ánh xạ các Views nội dung câu hỏi
            instructionText = itemView.findViewById(R.id.instruction_text);
            sentenceText = itemView.findViewById(R.id.question_sentence_text);
            optionsContainer = itemView.findViewById(R.id.options_container);

            // Đã xóa Ánh xạ: itemQuestionCountText, itemProgressPercentText, itemMainProgressBar
        }

        public void bind(Question question) {
            // KHÔNG CÓ LOGIC CẬP NHẬT PROGRESS BAR Ở ĐÂY

            instructionText.setText(question.getInstruction());
            sentenceText.setText(question.getSentence());

            optionsContainer.removeAllViews();

            for (String option : question.getOptions()) {
                // Sử dụng R.layout.item_question_option (cần tạo file này)
                View optionView = LayoutInflater.from(context).inflate(R.layout.item_question_option, optionsContainer, false);

                TextView optionText = optionView.findViewById(R.id.option_text);
                RadioButton optionRadio = optionView.findViewById(R.id.option_radio);

                optionText.setText(option);

                // Thiết lập trạng thái và style (Cần file drawable)
                optionView.setBackgroundResource(R.drawable.option_background_selector);
                optionRadio.setChecked(option.equals(question.getSelectedAnswer()));

                // Xử lý click
                optionView.setOnClickListener(v -> {
                    questions.get(getAdapterPosition()).setSelectedAnswer(option);
                    if (listener != null) {
                        // Gọi Activity để cập nhật ProgressBar toàn cục
                        listener.onAnswerSelected(question.getId(), option);
                    }
                    notifyItemChanged(getAdapterPosition());
                });

                optionsContainer.addView(optionView);
            }
        }
    }
}