import api from "./api";
import { userService } from "./userService";

const AUTH_URL = "/user";

export const authService = {
  // Đăng nhập
  login: async (email, password) => {
    try {
      // Clear any existing token to avoid sending invalid Auth headers
      localStorage.removeItem("token");
      localStorage.removeItem("user");

      const response = await api.post("/user/login", {
        email,
        matkhau: password,
      });
      if (response.data.token) {
        localStorage.setItem("token", response.data.token);
        localStorage.setItem("user", JSON.stringify(response.data));
      }
      return response.data;
    } catch (error) {
      console.error("Login failed:", error);

      throw error;
    }
  },

  // Đăng xuất
  logout: () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
  },

  // Lấy token từ localStorage
  getToken: () => {
    return localStorage.getItem("token");
  },

  // Lấy thông tin user từ localStorage
  getUser: () => {
    const user = localStorage.getItem("user");
    return user ? JSON.parse(user) : null;
  },

  // Kiểm tra xem đã đăng nhập chưa
  isAuthenticated: () => {
    return !!localStorage.getItem("token");
  },

  // Auto login với tài khoản admin
  autoLoginAdmin: async (
    email = "admin@example.com",
    password = "admin123"
  ) => {
    try {
      const response = await authService.login(email, password);
      if (response.token) {
        console.log("✅ Admin logged in successfully");
        console.log("Token scope:", response.scope || "ADMIN");
        return response;
      }
    } catch (error) {
      console.error(
        "❌ Auto login admin failed:",
        error.response?.data?.message || error.message
      );
      throw error;
    }
  },
};

export default authService;
