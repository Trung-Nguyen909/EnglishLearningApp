import api from './api';

const LESSON_URL = '/baihoc';

export const lessonService = {
  getAllLessons: async () => {
    const response = await api.get(LESSON_URL);
    return response.data;
  },

  getLessonById: async (id) => {
    const response = await api.get(`${LESSON_URL}/${id}`);
    return response.data;
  },

  createLesson: async (lessonData) => {
    const response = await api.post(LESSON_URL, lessonData);
    return response.data;
  },

  updateLesson: async (id, lessonData) => {
    const response = await api.put(`${LESSON_URL}/${id}`, lessonData);
    return response.data;
  },

  deleteLesson: async (id) => {
    const response = await api.delete(`${LESSON_URL}/${id}`);
    return response.data;
  },

  getLessonsByCourseId: async (courseId) => {
    const response = await api.get(`${LESSON_URL}/khoahoc/${courseId}`);
    return response.data;
  }
};
