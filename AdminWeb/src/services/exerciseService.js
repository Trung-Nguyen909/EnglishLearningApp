import api from './api';

const EXERCISE_URL = '/baitap';

export const exerciseService = {
  // Get all exercises
  getAllExercises: async () => {
    try {
      const response = await api.get('/baitap/all');
      return response.data;
    } catch (error) {
      console.error('Error fetching exercises:', error);
      throw error;
    }
  },

  // Get exercises by lesson ID
  getExercisesByLessonId: async (lessonId) => {
    try {
      const response = await api.get(`/baitap/baihoc/${lessonId}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching exercises for lesson ${lessonId}:`, error);
      throw error;
    }
  },

  // Get exercises by type
  getExercisesByType: async (type) => {
    try {
      const response = await api.get(`/baitap/${type}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching exercises of type ${type}:`, error);
      throw error;
    }
  },

  // Create new exercise
  createExercise: async (exerciseData) => {
    try {
        const response = await api.post('/baitap', exerciseData);
        return response.data;
    } catch (error) {
        console.error('Error creating exercise:', error);
        throw error;
    }
  },

  // Update exercise
  updateExercise: async (id, exerciseData) => {
    try {
        const response = await api.put(`/baitap/${id}`, exerciseData);
        return response.data;
    } catch (error) {
        console.error(`Error updating exercise ${id}:`, error);
        throw error;
    }
  },

  // Delete exercise
  deleteExercise: async (id) => {
    try {
        const response = await api.delete(`/baitap/${id}`);
        return response.data;
    } catch (error) {
        console.error(`Error deleting exercise ${id}:`, error);
        throw error;
    }
  }
};
