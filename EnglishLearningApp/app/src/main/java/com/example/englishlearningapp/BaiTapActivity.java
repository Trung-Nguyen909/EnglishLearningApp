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

import com.example.englishlearningapp.Model.CauHoi;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BaiTapActivity extends AppCompatActivity implements CauHoiAdapter.OnAnswerSelectedListener {

    private List<CauHoi> cauHois;

    // Global UI Components
    private Button btn_HoanThanh;
    private ProgressBar thanhTienTrinh;
    private TextView tv_demSoCauHoi;
    private TextView tv_phanTramTienTrinh;

    // Logic Tracking
    private Set<Integer> idCauHoiDaTraLoi = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baitap); // Tên layout chính của bạn

        // 1. Khởi tạo dữ liệu và ánh xạ Views
        cauHois = taoCauHoi();

        RecyclerView questionsRecyclerView = findViewById(R.id.questions_recycler_view);
        ImageView backButton = findViewById(R.id.back_button);

        // Ánh xạ thành phần Global từ Layout Chính
        btn_HoanThanh = findViewById(R.id.complete_button);
        thanhTienTrinh = findViewById(R.id.main_progress_bar);
        tv_demSoCauHoi = findViewById(R.id.question_count_text);
        tv_phanTramTienTrinh = findViewById(R.id.progress_percent_text);

        // 2. Thiết lập RecyclerView
        CauHoiAdapter adapter = new CauHoiAdapter(cauHois, this);
        questionsRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );
        questionsRecyclerView.setAdapter(adapter);

        // 3. Thiết lập trạng thái ban đầu
        thanhTienTrinh.setMax(cauHois.size());
        capNhatTrangThaiTienTrinh();

        // Xử lý sự kiện
        backButton.setOnClickListener(v -> finish());
        btn_HoanThanh.setOnClickListener(v -> {
            System.out.println("Bài kiểm tra hoàn thành!");
        });
    }

    @Override
    public void onAnswerSelected(int questionId, String selectedAnswer) {
        // Cập nhật Set theo dõi
        if (selectedAnswer != null) {
            idCauHoiDaTraLoi.add(questionId);
        } else {
            idCauHoiDaTraLoi.remove(questionId);
        }

        // Cập nhật giao diện toàn cục
        capNhatTrangThaiTienTrinh();
    }

    /**
     * Cập nhật ProgressBar, các TextView và trạng thái nút Hoàn thành.
     */
    private void capNhatTrangThaiTienTrinh() {
        int answeredCount = idCauHoiDaTraLoi.size();
        int total = cauHois.size();

        // Tính phần trăm
        int progressPercent = (int) (((float) answeredCount / (float) total) * 100);

        // 1. Cập nhật TextViews và ProgressBar
        tv_demSoCauHoi.setText("Câu " + answeredCount + "/" + total);
        tv_phanTramTienTrinh.setText(progressPercent + "%");
        thanhTienTrinh.setProgress(answeredCount);

        // 2. Điều khiển nút Hoàn thành
        if (answeredCount == total) {
            btn_HoanThanh.setEnabled(true);
            btn_HoanThanh.setVisibility(View.VISIBLE);
            // Dùng R.color.colorPrimary (cần định nghĩa trong colors.xml)
            btn_HoanThanh.setBackgroundTintList(getColorStateList(R.color.colorPrimary));
        } else {
            btn_HoanThanh.setEnabled(false);
            btn_HoanThanh.setVisibility(View.VISIBLE); // Giữ nút hiển thị nhưng vô hiệu hóa
            // Dùng R.color.colorGray
            btn_HoanThanh.setBackgroundTintList(getColorStateList(R.color.colorGray));
        }
    }

    private List<CauHoi> taoCauHoi() {
        return Arrays.asList(
                new CauHoi(1, "Choose the correct word to complete the sentence:", "She ____ to the store yesterday.", Arrays.asList("go", "goes", "went", "going"), "went"),

                new CauHoi(2, "Vocabulary: Choose the word with the opposite meaning.", "The word 'difficult' is the opposite of ____.", Arrays.asList("hard", "easy", "long", "bad"), "easy"),

                new CauHoi(3, "Grammar: Present Continuous tense", "She ____ dinner right now.", Arrays.asList("cooks", "is cooking", "cook", "was cooking"), "is cooking"),

                new CauHoi(4, "Communication: What is the polite way to ask for help?", "You say: ____", Arrays.asList("Help me!", "Give me that!", "Could you help me, please?", "You must help me!"), "Could you help me, please?"),

                new CauHoi(5, "Vocabulary: Choose the correct meaning.", "The word 'improve' means ____.", Arrays.asList("làm tệ hơn", "cải thiện", "phá hỏng", "bắt đầu"), "cải thiện"),

                new CauHoi(6, "Grammar: Choose the correct past tense form.", "They ____ a movie last night.", Arrays.asList("watch", "watched", "watching", "watches"), "watched"),

                new CauHoi(7, "Pronunciation: Which word has a different vowel sound?", "Choose the odd one out.", Arrays.asList("beat", "seat", "great", "heat"), "great"),

                new CauHoi(8, "Vocabulary: Choose the correct preposition.", "I am interested ____ learning English.", Arrays.asList("on", "in", "with", "to"), "in"),

                new CauHoi(9, "Reading: Choose the best answer.", "‘I usually study English in the evening.’ What does ‘usually’ mean?", Arrays.asList("luôn luôn", "thỉnh thoảng", "thường xuyên", "hiếm khi"), "thường xuyên"),

                new CauHoi(10, "Grammar: Articles", "She bought ____ umbrella yesterday.", Arrays.asList("a", "an", "the", "no article"), "an")
        );
    }

}