import axios from 'axios';

// Create axios instance with base URL
// Assuming the backend is running on localhost:8080 as is common with Spring Boot
const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add a request interceptor to attach the token if it exists
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add a response interceptor to handle common errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      // Handle unauthorized access (e.g., redirect to login)
      console.error('Unauthorized access');
      // window.location.href = '/login'; 
    }
    return Promise.reject(error);
  }
);

// API functions
export const apiCreate = (endpoint, data) => {
  return api.post(endpoint, data);
};

export const apiUpdate = (endpoint, data) => {
  return api.put(endpoint, data);
};

export const apiDelete = (endpoint) => {
  return api.delete(endpoint);
};

export const apiGet = (endpoint) => {
  return api.get(endpoint);
};

export default api;
