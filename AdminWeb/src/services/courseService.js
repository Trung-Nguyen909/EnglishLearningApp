import api from "./api";

const COURSE_URL = "/KhoaHoc";

export const courseService = {
  // Lấy tất cả khóa học
  getAllCourses: async () => {
    try {
      const response = await api.get(COURSE_URL);
      return response.data;
    } catch (error) {
      console.error("Lỗi khi lấy danh sách khóa học:", error);
      throw error;
    }
  },

  // Lấy khóa học theo ID
  getCourseById: async (id) => {
    try {
      const response = await api.get(`${COURSE_URL}/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Lỗi khi lấy khóa học ${id}:`, error);
      throw error;
    }
  },

  // Tạo khóa học mới
  createCourse: async (courseData) => {
    try {
      // Validation
      if (!courseData.tenKhoaHoc || courseData.tenKhoaHoc.trim() === "") {
        throw new Error("Tên khóa học không được để trống");
      }

      const payload = {
        ...courseData,
        tenKhoaHoc: courseData.tenKhoaHoc.trim(),
        moTa: courseData.moTa?.trim() || "",
        trinhDo: courseData.trinhDo || "BEGINNER",
        iconUrl: courseData.iconUrl?.trim() || "",
      };

      const response = await api.post(COURSE_URL, payload);
      return response.data;
    } catch (error) {
      console.error("Lỗi khi tạo khóa học:", error);
      throw error;
    }
  },

  // Cập nhật khóa học
  updateCourse: async (id, courseData) => {
    try {
      // Validation
      if (!courseData.tenKhoaHoc || courseData.tenKhoaHoc.trim() === "") {
        throw new Error("Tên khóa học không được để trống");
      }

      const payload = {
        ...courseData,
        tenKhoaHoc: courseData.tenKhoaHoc.trim(),
        moTa: courseData.moTa?.trim() || "",
        trinhDo: courseData.trinhDo || "BEGINNER",
        iconUrl: courseData.iconUrl?.trim() || "",
      };

      const response = await api.put(`${COURSE_URL}/${id}`, payload);
      return response.data;
    } catch (error) {
      console.error(`Lỗi khi cập nhật khóa học ${id}:`, error);
      throw error;
    }
  },

  // Xóa khóa học
  deleteCourse: async (id) => {
    try {
      const response = await api.delete(`${COURSE_URL}/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Lỗi khi xóa khóa học ${id}:`, error);
      throw error;
    }
  },
};
