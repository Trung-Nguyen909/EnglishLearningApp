package com.example.englishlearningapp.Interface;

public interface LangNgheSuKien {
    /**
     * Hàm này sẽ được gọi từ Adapter khi người dùng thay đổi câu trả lời.
     *
     * @param maCauHoi ID của câu hỏi (để biết là câu nào trong danh sách).
     * @param dapAn    Nội dung trả lời (Text văn bản, hoặc đường dẫn file ghi âm).
     * Nếu dapAn là null hoặc rỗng, tức là người dùng đã xóa câu trả lời.
     */
    void khiDapAnDuocChon(int maCauHoi, String dapAn);
}