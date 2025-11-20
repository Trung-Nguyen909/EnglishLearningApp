package com.example.englishlearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.englishlearningapp.Model.Lesson;
import java.util.ArrayList;
import java.util.List;

public class BaihocActivity extends AppCompatActivity {

    private RecyclerView lessonsRecyclerView;
    private BaihocAdapter lessonAdapter;
    private List<Lesson> lessonList;
    private TextView tvHeaderTitle;
    private ImageView btnBack;

    // Định nghĩa màu sắc cố định cho từng kỹ năng
    private static final int COLOR_VOCABULARY = R.color.orange_500;
    private static final int COLOR_GRAMMAR = R.color.green_500;
    private static final int COLOR_SPEAKING = R.color.purple_500;
    private static final int COLOR_LISTENING = R.color.red_500;
    private static final int COLOR_READING = R.color.yellow_500;
    private static final int COLOR_WRITING = R.color.blue_500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        lessonsRecyclerView = findViewById(R.id.lessons_recycler_view);
        tvHeaderTitle = findViewById(R.id.course_title);
        lessonsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnBack = findViewById(R.id.back_button);

        // --- NHẬN DỮ LIỆU (Code cũ) ---
        Intent intent = getIntent();
        int subItemId = -1;
        String subItemName = "Bài học";

        if (intent != null) {
            subItemId = intent.getIntExtra("SUB_ITEM_ID", -1);
            subItemName = intent.getStringExtra("SUB_ITEM_NAME");
        }

        if (subItemName != null) tvHeaderTitle.setText(subItemName);

        // --- TẠO DỮ LIỆU (Code cũ) ---
        lessonList = new ArrayList<>();
        loadDataByTopicId(subItemId, subItemName);

        // Khởi tạo Adapter
        lessonAdapter = new BaihocAdapter(this, lessonList);

        // >>>>>> PHẦN MỚI THÊM VÀO: SỰ KIỆN CLICK <<<<<<
        // Sử dụng đúng tên hàm tiếng Việt đã tạo trong Adapter
        lessonAdapter.datSuKienClick(new BaihocAdapter.SuKienClickItem() {
            @Override
            public void khiAnVaoItem(Lesson baiHoc) {
                // Chuyển sang trang Chi tiết (ExerciseDetailActivity)
                // Nếu bạn đã đổi tên trang chi tiết thành ChitietBaihocActivity thì nhớ sửa lại tên class ở đây nhé
                Intent intentDetail = new Intent(BaihocActivity.this, ExerciseDetailActivity.class);

                // Gửi dữ liệu sang trang chi tiết
                intentDetail.putExtra("TITLE", baiHoc.getTitle());
                intentDetail.putExtra("TYPE", baiHoc.getType());
                intentDetail.putExtra("LEVEL", baiHoc.getLevel());
                intentDetail.putExtra("TIME", baiHoc.getTime());

                startActivity(intentDetail);
            }
        });
        // >>>>>> KẾT THÚC PHẦN THÊM MỚI <<<<<<

        lessonsRecyclerView.setAdapter(lessonAdapter);
        btnBack.setOnClickListener(v -> finish());
    }

    // --- CÁC HÀM DƯỚI GIỮ NGUYÊN KHÔNG SỬA ---

    // Hàm chính để chọn nội dung theo ID
    private void loadDataByTopicId(int id, String defaultName) {
        switch (id) {
            case 1: // Family Members
                addFullLessonSet(
                        "Các thành viên trong gia đình",
                        "Sở hữu cách (Possessive's)",
                        "Giới thiệu về gia đình bạn",
                        "Đoạn hội thoại hàng ngày",
                        "Truyền thống gia đình",
                        "Viết thư kể về gia đình"
                );
                break;

            case 2: // Daily Activities
                addFullLessonSet(
                        "Hoạt động buổi sáng",
                        "Thì hiện tại đơn",
                        "Hỏi giờ giấc",
                        "Lịch trình xe buýt",
                        "Nhật ký của Anna",
                        "Kế hoạch cuối tuần"
                );
                break;

            case 3: // Relationshhip
                addFullLessonSet(
                        "Mối quan hệ trong gia đình",
                        "Thì hiện tại tiếp diễn",
                        "Chào hỏi trong gia đình",
                        "Hội thoại trong gia đình",
                        "Giới thiệu về gia đình",
                        "Thành viên trong gia đình của bạn"
                );
                break;

            default:
                addGenericLessons(defaultName);
                break;
        }
    }

    private void addFullLessonSet(String vocabTitle, String grammarTitle, String speakTitle,
                                  String listenTitle, String readTitle, String writeTitle) {

        lessonList.add(new Lesson("Vocabulary", vocabTitle, "Beginner", "10 min", COLOR_VOCABULARY));
        lessonList.add(new Lesson("Grammar", grammarTitle, "Beginner", "12 min", COLOR_GRAMMAR));
        lessonList.add(new Lesson("Speaking", speakTitle, "Intermediate", "15 min", COLOR_SPEAKING));
        lessonList.add(new Lesson("Listening", listenTitle, "Intermediate", "15 min", COLOR_LISTENING));
        lessonList.add(new Lesson("Reading", readTitle, "Advanced", "20 min", COLOR_READING));
        lessonList.add(new Lesson("Writing", writeTitle, "Intermediate", "18 min", COLOR_WRITING));
    }

    private void addGenericLessons(String topicName) {
        lessonList.add(new Lesson("Vocabulary", "Từ vựng về " + topicName, "Beginner", "10 min", COLOR_VOCABULARY));
        lessonList.add(new Lesson("Grammar", "Ngữ pháp liên quan", "Beginner", "12 min", COLOR_GRAMMAR));
        lessonList.add(new Lesson("Speaking", "Luyện nói về " + topicName, "Intermediate", "15 min", COLOR_SPEAKING));
        lessonList.add(new Lesson("Listening", "Bài nghe chủ đề " + topicName, "Intermediate", "15 min", COLOR_LISTENING));
        lessonList.add(new Lesson("Reading", "Bài đọc hiểu", "Advanced", "20 min", COLOR_READING));
        lessonList.add(new Lesson("Writing", "Bài tập viết", "Intermediate", "18 min", COLOR_WRITING));
    }
}