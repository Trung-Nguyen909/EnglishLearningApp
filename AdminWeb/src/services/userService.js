import api from './api';

const USER_URL = '/user';

export const userService = {
  getAllUsers: async () => {
    const response = await api.get(`${USER_URL}/admin/all`);
    return response.data;
  },

  getUserById: async (id) => {
    const response = await api.get(`${USER_URL}/${id}`);
    return response.data;
  },

  registerUser: async (userData) => {
    const response = await api.post(`${USER_URL}/register`, userData);
    return response.data;
  },

  updateUser: async (id, userData) => {
    const response = await api.put(`${USER_URL}/${id}`, userData);
    return response.data;
  },

  deleteUser: async (id) => {
    const response = await api.delete(`${USER_URL}/${id}`);
    return response.data;
  },
  
  login: async (credentials) => {
    const response = await api.post(`${USER_URL}/login`, credentials);
    return response.data;
  },
  
  changePassword: async (passwordData) => {
    const response = await api.post(`${USER_URL}/changePass`, passwordData);
    return response.data;
  },
  
  getUserSummary: async () => {
    const response = await api.get(`${USER_URL}/summary`);
    return response.data;
  },
  
  uploadAvatar: async (formData) => {
    const response = await api.post(`${USER_URL}/upAva`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
    return response.data;
  }
};
