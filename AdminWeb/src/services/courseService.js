import api from './api';

const COURSE_URL = '/KhoaHoc';

export const courseService = {
  getAllCourses: async () => {
    const response = await api.get(COURSE_URL);
    return response.data;
  },

  getCourseById: async (id) => {
    const response = await api.get(`${COURSE_URL}/${id}`);
    return response.data;
  },

  createCourse: async (courseData) => {
    const response = await api.post(COURSE_URL, courseData);
    return response.data;
  },

  updateCourse: async (id, courseData) => {
    const response = await api.put(`${COURSE_URL}/${id}`, courseData);
    return response.data;
  },

  deleteCourse: async (id) => {
    const response = await api.delete(`${COURSE_URL}/${id}`);
    return response.data;
  }
};
