package com.example.englishlearningapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishlearningapp.Model.Question;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BaiTapActivity extends AppCompatActivity implements QuestionAdapter.OnAnswerSelectedListener {

    private List<Question> questions;

    // Global UI Components
    private Button completeButton;
    private ProgressBar mainProgressBar;
    private TextView questionCountText;
    private TextView progressPercentText;

    // Logic Tracking
    private Set<Integer> answeredQuestionIds = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baitap); // Tên layout chính của bạn

        // 1. Khởi tạo dữ liệu và ánh xạ Views
        questions = createDummyQuestions();

        RecyclerView questionsRecyclerView = findViewById(R.id.questions_recycler_view);
        ImageView backButton = findViewById(R.id.back_button);

        // Ánh xạ thành phần Global từ Layout Chính
        completeButton = findViewById(R.id.complete_button);
        mainProgressBar = findViewById(R.id.main_progress_bar);
        questionCountText = findViewById(R.id.question_count_text);
        progressPercentText = findViewById(R.id.progress_percent_text);

        // 2. Thiết lập RecyclerView
        QuestionAdapter adapter = new QuestionAdapter(questions, this);
        questionsRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );
        questionsRecyclerView.setAdapter(adapter);

        // 3. Thiết lập trạng thái ban đầu
        mainProgressBar.setMax(questions.size());
        updateProgressState();

        // Xử lý sự kiện
        backButton.setOnClickListener(v -> finish());
        completeButton.setOnClickListener(v -> {
            System.out.println("Bài kiểm tra hoàn thành!");
        });
    }

    @Override
    public void onAnswerSelected(int questionId, String selectedAnswer) {
        // Cập nhật Set theo dõi
        if (selectedAnswer != null) {
            answeredQuestionIds.add(questionId);
        } else {
            answeredQuestionIds.remove(questionId);
        }

        // Cập nhật giao diện toàn cục
        updateProgressState();
    }

    /**
     * Cập nhật ProgressBar, các TextView và trạng thái nút Hoàn thành.
     */
    private void updateProgressState() {
        int answeredCount = answeredQuestionIds.size();
        int total = questions.size();

        // Tính phần trăm
        int progressPercent = (int) (((float) answeredCount / (float) total) * 100);

        // 1. Cập nhật TextViews và ProgressBar
        questionCountText.setText("Câu " + answeredCount + "/" + total);
        progressPercentText.setText(progressPercent + "%");
        mainProgressBar.setProgress(answeredCount);

        // 2. Điều khiển nút Hoàn thành
        if (answeredCount == total) {
            completeButton.setEnabled(true);
            completeButton.setVisibility(View.VISIBLE);
            // Dùng R.color.colorPrimary (cần định nghĩa trong colors.xml)
            completeButton.setBackgroundTintList(getColorStateList(R.color.colorPrimary));
        } else {
            completeButton.setEnabled(false);
            completeButton.setVisibility(View.VISIBLE); // Giữ nút hiển thị nhưng vô hiệu hóa
            // Dùng R.color.colorGray
            completeButton.setBackgroundTintList(getColorStateList(R.color.colorGray));
        }
    }

    private List<Question> createDummyQuestions() {
        return Arrays.asList(
                new Question(1, "Choose the correct word to complete the sentence:", "She ____ to the store yesterday.", Arrays.asList("go", "goes", "went", "going"), "went"),

                new Question(2, "Vocabulary: Choose the word with the opposite meaning.", "The word 'difficult' is the opposite of ____.", Arrays.asList("hard", "easy", "long", "bad"), "easy"),

                new Question(3, "Grammar: Present Continuous tense", "She ____ dinner right now.", Arrays.asList("cooks", "is cooking", "cook", "was cooking"), "is cooking"),

                new Question(4, "Communication: What is the polite way to ask for help?", "You say: ____", Arrays.asList("Help me!", "Give me that!", "Could you help me, please?", "You must help me!"), "Could you help me, please?"),

                new Question(5, "Vocabulary: Choose the correct meaning.", "The word 'improve' means ____.", Arrays.asList("làm tệ hơn", "cải thiện", "phá hỏng", "bắt đầu"), "cải thiện"),

                new Question(6, "Grammar: Choose the correct past tense form.", "They ____ a movie last night.", Arrays.asList("watch", "watched", "watching", "watches"), "watched"),

                new Question(7, "Pronunciation: Which word has a different vowel sound?", "Choose the odd one out.", Arrays.asList("beat", "seat", "great", "heat"), "great"),

                new Question(8, "Vocabulary: Choose the correct preposition.", "I am interested ____ learning English.", Arrays.asList("on", "in", "with", "to"), "in"),

                new Question(9, "Reading: Choose the best answer.", "‘I usually study English in the evening.’ What does ‘usually’ mean?", Arrays.asList("luôn luôn", "thỉnh thoảng", "thường xuyên", "hiếm khi"), "thường xuyên"),

                new Question(10, "Grammar: Articles", "She bought ____ umbrella yesterday.", Arrays.asList("a", "an", "the", "no article"), "an")
        );
    }

}