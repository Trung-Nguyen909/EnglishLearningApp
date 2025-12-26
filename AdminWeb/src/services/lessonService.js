import api from "./api";

const LESSON_URL = "/baihoc";

export const lessonService = {
  // Lấy tất cả bài học
  getAllLessons: async () => {
    try {
      const response = await api.get(LESSON_URL);
      return response.data;
    } catch (error) {
      console.error("Lỗi khi lấy danh sách bài học:", error);
      throw error;
    }
  },

  // Lấy bài học theo ID
  getLessonById: async (id) => {
    try {
      const response = await api.get(`${LESSON_URL}/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Lỗi khi lấy bài học ${id}:`, error);
      throw error;
    }
  },

  // Tạo bài học mới
  createLesson: async (lessonData) => {
    try {
      if (!lessonData.tenBaiHoc || lessonData.tenBaiHoc.trim() === "") {
        throw new Error("Tên bài học không được để trống");
      }
      if (!lessonData.idKhoaHoc) {
        throw new Error("Khóa học không được để trống");
      }

      const payload = {
        ...lessonData,
        tenBaiHoc: lessonData.tenBaiHoc.trim(),
        moTa: lessonData.moTa?.trim() || "",
        noiDung: lessonData.noiDung?.trim() || "",
        thuTuBaiHoc: lessonData.thuTuBaiHoc || 0,
        trangThai: lessonData.trangThai || "ACTIVE",
        iconUrl: lessonData.iconUrl?.trim() || "",
      };

      const response = await api.post(LESSON_URL, payload);
      return response.data;
    } catch (error) {
      console.error("Lỗi khi tạo bài học:", error);
      throw error;
    }
  },

  // Cập nhật bài học
  updateLesson: async (id, lessonData) => {
    try {
      if (!lessonData.tenBaiHoc || lessonData.tenBaiHoc.trim() === "") {
        throw new Error("Tên bài học không được để trống");
      }
      if (!lessonData.idKhoaHoc) {
        throw new Error("Khóa học không được để trống");
      }

      const payload = {
        ...lessonData,
        tenBaiHoc: lessonData.tenBaiHoc.trim(),
        moTa: lessonData.moTa?.trim() || "",
        noiDung: lessonData.noiDung?.trim() || "",
        thuTuBaiHoc: lessonData.thuTuBaiHoc || 0,
        trangThai: lessonData.trangThai || "ACTIVE",
        iconUrl: lessonData.iconUrl?.trim() || "",
      };

      const response = await api.put(`${LESSON_URL}/${id}`, payload);
      return response.data;
    } catch (error) {
      console.error(`Lỗi khi cập nhật bài học ${id}:`, error);
      throw error;
    }
  },

  // Xóa bài học
  deleteLesson: async (id) => {
    try {
      const response = await api.delete(`${LESSON_URL}/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Lỗi khi xóa bài học ${id}:`, error);
      throw error;
    }
  },

  // Lấy bài học theo ID khóa học
  getLessonsByCourseId: async (courseId) => {
    try {
      const response = await api.get(`${LESSON_URL}/khoahoc/${courseId}`);
      return response.data;
    } catch (error) {
      console.error(`Lỗi khi lấy bài học của khóa học ${courseId}:`, error);
      throw error;
    }
  },
};
