package com.example.englishlearningapp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.englishlearningapp.R;
import com.example.englishlearningapp.Model.QuestionResult;
import java.util.ArrayList;
import java.util.List;

public class QuestionResultAdapter extends RecyclerView.Adapter<QuestionResultAdapter.ViewHolder> {

    private List<QuestionResult> questions;

    public QuestionResultAdapter(List<QuestionResult> questions) {
        this.questions = questions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chitietcauhoi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuestionResult question = questions.get(position);

        // --- ĐỪNG QUÊN CÁC DÒNG NÀY ---
        holder.tvQuestionNumber.setText("Câu " + (position + 1) + " / " + questions.size());
        holder.tvQuestionText.setText(question.getQuestionText());
        holder.tvCorrectAnswer.setText(question.getCorrectAnswer());
        // ------------------------------

        // Xử lý logic hiển thị giải thích
        if (!question.getUserAnswer().equals(question.getCorrectAnswer())) {
            // --- TRƯỜNG HỢP LÀM SAI ---
            holder.layoutWrongAnswer.setVisibility(View.VISIBLE);
            holder.tvWrongAnswer.setText(question.getUserAnswer());

            // 1. Hiển thị nút bấm "Xem giải thích"
            holder.tvShowExplanation.setVisibility(View.VISIBLE);

            // 2. Set nội dung giải thích
            if (question.getExplanation() != null && !question.getExplanation().isEmpty()) {
                holder.tvExplanationText.setText(question.getExplanation());
            } else {
                holder.tvExplanationText.setText("Không có giải thích chi tiết.");
            }

            // 3. Reset trạng thái (Mặc định ẩn khi mới load)
            holder.tvExplanationText.setVisibility(View.GONE);
            holder.tvShowExplanation.setText("Xem giải thích");

            // 4. Bắt sự kiện Click
            holder.tvShowExplanation.setOnClickListener(v -> {
                if (holder.tvExplanationText.getVisibility() == View.VISIBLE) {
                    // Đang hiện -> Ẩn đi
                    holder.tvExplanationText.setVisibility(View.GONE);
                    holder.tvShowExplanation.setText("Xem giải thích");
                } else {
                    // Đang ẩn -> Hiện lên
                    holder.tvExplanationText.setVisibility(View.VISIBLE);
                    holder.tvShowExplanation.setText("Ẩn giải thích");
                }
            });

        } else {
            // --- TRƯỜNG HỢP LÀM ĐÚNG ---
            holder.layoutWrongAnswer.setVisibility(View.GONE);
            holder.tvShowExplanation.setVisibility(View.GONE);
            holder.tvExplanationText.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    // Filter methods
    public void filterAll(List<QuestionResult> allQuestions) {
        this.questions = allQuestions;
        notifyDataSetChanged();
    }

    public void filterCorrect(List<QuestionResult> allQuestions) {
        List<QuestionResult> correctQuestions = new ArrayList<>();
        for (QuestionResult q : allQuestions) {
            if (q.getUserAnswer().equals(q.getCorrectAnswer())) {
                correctQuestions.add(q);
            }
        }
        this.questions = correctQuestions;
        notifyDataSetChanged();
    }

    public void filterIncorrect(List<QuestionResult> allQuestions) {
        List<QuestionResult> incorrectQuestions = new ArrayList<>();
        for (QuestionResult q : allQuestions) {
            if (!q.getUserAnswer().equals(q.getCorrectAnswer())) {
                incorrectQuestions.add(q);
            }
        }
        this.questions = incorrectQuestions;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionNumber, tvQuestionText, tvWrongAnswer, tvCorrectAnswer, tvShowExplanation;
        LinearLayout layoutWrongAnswer, layoutCorrectAnswer;

        // Thêm biến này
        TextView tvExplanationText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionNumber = itemView.findViewById(R.id.tv_question_number);
            tvQuestionText = itemView.findViewById(R.id.tv_question_text);
            layoutWrongAnswer = itemView.findViewById(R.id.layout_wrong_answer);
            tvWrongAnswer = itemView.findViewById(R.id.tv_wrong_answer);
            layoutCorrectAnswer = itemView.findViewById(R.id.layout_correct_answer);
            tvCorrectAnswer = itemView.findViewById(R.id.tv_correct_answer);
            tvShowExplanation = itemView.findViewById(R.id.tv_show_explanation);

            // Ánh xạ View mới thêm
            tvExplanationText = itemView.findViewById(R.id.tv_explanation_text);
        }
    }
}
