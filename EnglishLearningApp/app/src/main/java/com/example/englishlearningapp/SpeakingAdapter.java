package com.example.englishlearningapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.englishlearningapp.Model.SpeakingQuestion;

import java.util.List;

public class SpeakingAdapter extends RecyclerView.Adapter<SpeakingAdapter.SpeakingViewHolder> {

    private Context context;
    private List<SpeakingQuestion> questionList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onRecordClick(int position);
        void onListenClick(String text);
    }

    public SpeakingAdapter(Context context, List<SpeakingQuestion> questionList, OnItemClickListener listener) {
        this.context = context;
        this.questionList = questionList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SpeakingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_speaking, parent, false);
        return new SpeakingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpeakingViewHolder holder, int position) {
        SpeakingQuestion question = questionList.get(position);

        holder.tvQuestionNumber.setText("Câu " + (position + 1));
        holder.tvTargetSentence.setText(question.getSentence());

        if (question.getUserAnswer() != null && !question.getUserAnswer().isEmpty()) {
            holder.tvUserResult.setVisibility(View.VISIBLE);
            holder.tvUserResult.setText(question.getUserAnswer());
            holder.imgStatus.setVisibility(View.VISIBLE);

            if (question.isCorrect()) {
                holder.tvUserResult.setTextColor(Color.parseColor("#4CAF50"));
                holder.imgStatus.setImageResource(R.drawable.ic_check_circle);
            } else {
                holder.tvUserResult.setTextColor(Color.parseColor("#F44336"));
                holder.imgStatus.setImageResource(R.drawable.ic_error);
            }
        } else {
            holder.tvUserResult.setVisibility(View.GONE);
            holder.imgStatus.setVisibility(View.GONE);
        }

        holder.btnRecord.setOnClickListener(v -> {
            if (listener != null) listener.onRecordClick(position);
        });

        holder.btnListen.setOnClickListener(v -> {
            if (listener != null) listener.onListenClick(question.getSentence());
        });
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class SpeakingViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionNumber, tvTargetSentence, tvUserResult;
        ImageView btnRecord, btnListen, imgStatus;

        public SpeakingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
            tvTargetSentence = itemView.findViewById(R.id.tvTargetSentence);
            tvUserResult = itemView.findViewById(R.id.tvUserResult);
            btnRecord = itemView.findViewById(R.id.btnRecord);
            btnListen = itemView.findViewById(R.id.btnListen);
            imgStatus = itemView.findViewById(R.id.imgStatus);
        }
    }
}