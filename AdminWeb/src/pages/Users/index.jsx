import { useState, useEffect } from "react";
import {
  FaEdit,
  FaSearch,
  FaUserCircle,
  FaTrash,
  FaPlus,
} from "react-icons/fa";
import styles from "./Users.module.css";
import { userService } from "../../services/userService";
import Modal from "../../components/Modal";

const Users = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Modal State
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalMode, setModalMode] = useState("add");
  const [selectedUser, setSelectedUser] = useState(null);
  const [formData, setFormData] = useState({
    tenDangNhap: "",
    matKhau: "",
    email: "",
  });

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      setLoading(true);
      const data = await userService.getAllUsers();
      setUsers(data);
    } catch (err) {
      setError("Không thể tải danh sách người dùng");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleOpenModal = (mode, user = null) => {
    setModalMode(mode);
    setSelectedUser(user);
    if (mode === "edit" && user) {
      setFormData({
        hoTen: user.hoTen || user.name,
        email: user.email,
        matKhau: "", // Use empty for security, or valid if logic allows
        role: user.role || "USER",
      });
    } else {
      setFormData({ hoTen: "", email: "", matKhau: "", role: "USER" });
    }
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setSelectedUser(null);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (modalMode === "add") {
        const newUser = await userService.registerUser({
          tenDangNhap: formData.email.split("@")[0], // Generate simple username
          ...formData,
        });
        fetchUsers();
      } else {
        await userService.updateUser(selectedUser.id, formData);
        fetchUsers();
      }
      handleCloseModal();
    } catch (err) {
      console.error("Failed to save user", err);
      alert("Lưu người dùng thất bại");
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("Bạn có chắc chắn muốn xóa người dùng này không?")) {
      try {
        await userService.deleteUser(id);
        setUsers(users.filter((u) => u.id !== id));
      } catch (err) {
        alert("Xóa người dùng thất bại");
        console.error(err);
      }
    }
  };

  if (loading) return <div className={styles.loading}>Đang tải...</div>;
  if (error) return <div className={styles.error}>{error}</div>;

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h2 className={styles.title}>Quản Lý Người Dùng</h2>
        <button
          className={styles.addBtn}
          onClick={() => handleOpenModal("add")}
        >
          <FaPlus />
          Thêm Người Dùng
        </button>
      </div>

      <div className={styles.toolbar}>
        <div className={styles.searchBox}>
          <FaSearch className={styles.searchIcon} />
          <input
            type="text"
            placeholder="Tìm người dùng theo tên hoặc email..."
            className={styles.searchInput}
          />
        </div>
      </div>

      <div className={styles.tableContainer}>
        <table className={styles.table}>
          <thead>
            <tr>
              <th>ID</th>
              <th>Người Dùng</th>
              <th>Vai Trò</th>
              <th>Thao Tác</th>
            </tr>
          </thead>
          <tbody>
            {users.length > 0 ? (
              users.map((user) => (
                <tr key={user.id}>
                  <td>#{user.id}</td>
                  <td className={styles.userCell}>
                    <FaUserCircle className={styles.userIcon} />
                    <div className={styles.userInfo}>
                      <div className={styles.userName}>
                        {user.tenDangNhap ||
                          user.hoTen ||
                          user.name ||
                          "Chưa có tên"}
                      </div>
                      <div className={styles.userEmail}>{user.email}</div>
                    </div>
                  </td>
                  <td>
                    <span
                      className={
                        user.role === "ADMIN"
                          ? styles.roleAdmin
                          : styles.roleUser
                      }
                    >
                      {user.role}
                    </span>
                  </td>
                  <td>
                    <div className={styles.actions}>
                      <button
                        className={styles.actionBtn}
                        title="Sửa"
                        onClick={() => handleOpenModal("edit", user)}
                      >
                        <FaEdit />
                      </button>
                      <button
                        className={`${styles.actionBtn} ${styles.deleteBtn}`}
                        title="Xóa"
                        onClick={() => handleDelete(user.id)}
                      >
                        <FaTrash />
                      </button>
                    </div>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td
                  colSpan="4"
                  style={{ textAlign: "center", padding: "20px" }}
                >
                  Không tìm thấy người dùng
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title={
          modalMode === "add" ? "Thêm Người Dùng Mới" : "Chỉnh Sửa Người Dùng"
        }
      >
        <form className={styles.form} onSubmit={handleSubmit}>
          <div className={styles.formGroup}>
            <label>Họ Tên</label>
            <input
              type="text"
              name="hoTen"
              value={formData.hoTen}
              onChange={handleInputChange}
              required
            />
          </div>
          <div className={styles.formGroup}>
            <label>Email</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleInputChange}
              required
            />
          </div>
          <div className={styles.formGroup}>
            <label>Mật Khẩu</label>
            <input
              type="password"
              name="matKhau"
              value={formData.matKhau}
              onChange={handleInputChange}
              required={modalMode === "add"}
              placeholder={modalMode === "edit" ? "Để trống nếu không đổi" : ""}
            />
          </div>
          <div className={styles.formGroup}>
            <label>Vai Trò</label>
            <select
              name="role"
              value={formData.role}
              onChange={handleInputChange}
            >
              <option value="USER">Người dùng (User)</option>
              <option value="ADMIN">Quản trị viên (Admin)</option>
            </select>
          </div>
          <div className={styles.formActions}>
            <button
              type="button"
              className={styles.cancelBtn}
              onClick={handleCloseModal}
            >
              Hủy
            </button>
            <button type="submit" className={styles.submitBtn}>
              {modalMode === "add" ? "Tạo Người Dùng" : "Lưu Thay Đổi"}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default Users;
