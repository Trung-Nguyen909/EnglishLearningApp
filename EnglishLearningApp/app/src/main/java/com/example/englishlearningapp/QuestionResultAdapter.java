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

        // Set question number and text
        holder.tvQuestionNumber.setText("Question " + (position + 1) + " of " + questions.size());
        holder.tvQuestionText.setText(question.getQuestionText());

        // Set correct answer
        holder.tvCorrectAnswer.setText(question.getCorrectAnswer());

        // Check if user's answer is wrong
        if (!question.getUserAnswer().equals(question.getCorrectAnswer())) {
            // Show wrong answer
            holder.layoutWrongAnswer.setVisibility(View.VISIBLE);
            holder.tvWrongAnswer.setText(question.getUserAnswer());
            holder.tvShowExplanation.setVisibility(View.VISIBLE);

            // Handle explanation click
            holder.tvShowExplanation.setOnClickListener(v -> {
                // Toggle explanation
                // You can implement expand/collapse logic here
            });
        } else {
            // Hide wrong answer section
            holder.layoutWrongAnswer.setVisibility(View.GONE);
            holder.tvShowExplanation.setVisibility(View.GONE);
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
        TextView tvQuestionNumber;
        TextView tvQuestionText;
        LinearLayout layoutWrongAnswer;
        TextView tvWrongAnswer;
        LinearLayout layoutCorrectAnswer;
        TextView tvCorrectAnswer;
        TextView tvShowExplanation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionNumber = itemView.findViewById(R.id.tv_question_number);
            tvQuestionText = itemView.findViewById(R.id.tv_question_text);
            layoutWrongAnswer = itemView.findViewById(R.id.layout_wrong_answer);
            tvWrongAnswer = itemView.findViewById(R.id.tv_wrong_answer);
            layoutCorrectAnswer = itemView.findViewById(R.id.layout_correct_answer);
            tvCorrectAnswer = itemView.findViewById(R.id.tv_correct_answer);
            tvShowExplanation = itemView.findViewById(R.id.tv_show_explanation);
        }
    }
}
