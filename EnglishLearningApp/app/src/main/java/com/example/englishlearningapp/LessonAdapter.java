package com.example.englishlearningapp;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Model.Lesson;

import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private final List<Lesson> lessonList;
    private final Context context;

    public LessonAdapter(Context context, List<Lesson> lessonList) {
        this.context = context;
        this.lessonList = lessonList;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_exercise, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson lesson = lessonList.get(position);

        holder.typeLabel.setText(lesson.getType());
        holder.title.setText(lesson.getTitle());
        holder.level.setText(lesson.getLevel());
        holder.time.setText(lesson.getTime());

        // Thay đổi màu nền của nhãn loại bài học
        GradientDrawable background = (GradientDrawable) holder.typeLabel.getBackground();
        background.setColor(context.getColor(lesson.getTypeColor()));
    }

    @Override
    public int getItemCount() {
        return lessonList.size();
    }

    public static class LessonViewHolder extends RecyclerView.ViewHolder {
        TextView typeLabel;
        TextView title;
        TextView level;
        TextView time;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            typeLabel = itemView.findViewById(R.id.lesson_type_label);
            title = itemView.findViewById(R.id.lesson_title);
            level = itemView.findViewById(R.id.lesson_level);
            time = itemView.findViewById(R.id.lesson_time);
            // Các thành phần khác như icon bookmark, level, time
        }
    }
}