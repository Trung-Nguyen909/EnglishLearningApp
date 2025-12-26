import { useState, useEffect } from "react";
import { FaPlus, FaEdit, FaTrash, FaSearch } from "react-icons/fa";
import styles from "./Courses.module.css";
import { courseService } from "../../services/courseService";
import Modal from "../../components/Modal";

const Courses = () => {
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [successMessage, setSuccessMessage] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");

  // Modal State
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalMode, setModalMode] = useState("add");
  const [selectedCourse, setSelectedCourse] = useState(null);
  const [isSaving, setIsSaving] = useState(false);
  const [formError, setFormError] = useState(null);

  const [formData, setFormData] = useState({
    tenKhoaHoc: "",
    moTa: "",
    trinhDo: "BEGINNER",
    iconUrl: "",
  });

  useEffect(() => {
    fetchCourses();
  }, []);

  // Tự động xóa success message sau 3 giây
  useEffect(() => {
    if (successMessage) {
      const timer = setTimeout(() => setSuccessMessage(null), 3000);
      return () => clearTimeout(timer);
    }
  }, [successMessage]);

  const fetchCourses = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await courseService.getAllCourses();
      setCourses(data);
    } catch (err) {
      setError("Không thể tải danh sách khóa học");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleOpenModal = (mode, course = null) => {
    setModalMode(mode);
    setSelectedCourse(course);
    setFormError(null);

    if (mode === "edit" && course) {
      setFormData({
        tenKhoaHoc: course.tenKhoaHoc,
        moTa: course.moTa || "",
        trinhDo: course.trinhDo || "BEGINNER",
        iconUrl: course.iconUrl || "",
      });
    } else {
      setFormData({
        tenKhoaHoc: "",
        moTa: "",
        trinhDo: "BEGINNER",
        iconUrl: "",
      });
    }
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setSelectedCourse(null);
    setFormError(null);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
    // Xóa lỗi khi người dùng bắt đầu gõ
    if (formError) setFormError(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validation
    if (!formData.tenKhoaHoc.trim()) {
      setFormError("Tên khóa học không được để trống");
      return;
    }

    try {
      setIsSaving(true);
      setFormError(null);

      if (modalMode === "add") {
        const payload = {
          ...formData,
          tenKhoaHoc: formData.tenKhoaHoc.trim(),
          moTa: formData.moTa.trim(),
          ngayTao: new Date().toISOString().split("T")[0],
        };
        await courseService.createCourse(payload);
        setSuccessMessage("Thêm khóa học thành công");
      } else {
        const updateResult = await courseService.updateCourse(
          selectedCourse.id,
          {
            ...formData,
            tenKhoaHoc: formData.tenKhoaHoc.trim(),
            moTa: formData.moTa.trim(),
            trinhDo: formData.trinhDo,
            iconUrl: formData.iconUrl.trim(),
          }
        );
        setSuccessMessage("Cập nhật khóa học thành công");
      }
      await fetchCourses();
      setTimeout(() => {
        handleCloseModal();
      }, 500);
    } catch (err) {
      console.error("❌ Chi tiết lỗi:", err.response || err);
      let errorMessage = "Không thể lưu khóa học";

      if (err.response?.data?.message) {
        errorMessage = err.response.data.message;
      } else if (err.response?.data) {
        errorMessage = JSON.stringify(err.response.data);
      } else if (err.message) {
        errorMessage = err.message;
      }

      console.error("❌ Thông báo lỗi:", errorMessage);
      setFormError(errorMessage);
    } finally {
      setIsSaving(false);
    }
  };

  const handleDelete = async (id, courseName) => {
    if (
      window.confirm(
        `Bạn có chắc chắn muốn xóa khóa học "${courseName}" không?`
      )
    ) {
      try {
        await courseService.deleteCourse(id);
        setCourses(courses.filter((course) => course.id !== id));
        setSuccessMessage("Xóa khóa học thành công");
      } catch (err) {
        const errorMessage =
          err.response?.data?.message || "Xóa khóa học thất bại";
        setError(errorMessage);
        console.error(err);
      }
    }
  };

  // Filter courses by search term
  const filteredCourses = courses.filter(
    (course) =>
      course.tenKhoaHoc.toLowerCase().includes(searchTerm.toLowerCase()) ||
      (course.moTa &&
        course.moTa.toLowerCase().includes(searchTerm.toLowerCase()))
  );

  if (loading) return <div className={styles.loading}>Đang tải...</div>;

  return (
    <div className={styles.container}>
      {error && <div className={styles.error}>{error}</div>}
      {successMessage && <div className={styles.success}>{successMessage}</div>}

      <div className={styles.header}>
        <h2 className={styles.title}>Quản Lý Khóa Học</h2>
        <button
          className={styles.addBtn}
          onClick={() => handleOpenModal("add")}
        >
          <FaPlus />
          Thêm Khóa Học
        </button>
      </div>

      <div className={styles.toolbar}>
        <div className={styles.searchBox}>
          <FaSearch className={styles.searchIcon} />
          <input
            type="text"
            placeholder="Tìm kiếm khóa học..."
            className={styles.searchInput}
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>
      </div>

      <div className={styles.tableContainer}>
        <table className={styles.table}>
          <thead>
            <tr>
              <th>ID</th>
              <th>Tên Khóa Học</th>
              <th>Mô Tả</th>
              <th>Trình Độ</th>
              <th>Thao Tác</th>
            </tr>
          </thead>
          <tbody>
            {filteredCourses.length > 0 ? (
              filteredCourses.map((course) => (
                <tr key={course.id}>
                  <td>#{course.id}</td>
                  <td className={styles.courseTitle}>
                    <div className={styles.titleWrapper}>
                      {course.tenKhoaHoc}
                    </div>
                  </td>
                  <td>{course.moTa || "Không có mô tả"}</td>
                  <td>
                    <span className={styles.badge}>{course.trinhDo}</span>
                  </td>
                  <td>
                    <div className={styles.actions}>
                      <button
                        className={styles.actionBtn}
                        title="Sửa"
                        onClick={() => handleOpenModal("edit", course)}
                      >
                        <FaEdit />
                      </button>
                      <button
                        className={`${styles.actionBtn} ${styles.deleteBtn}`}
                        title="Xóa"
                        onClick={() =>
                          handleDelete(course.id, course.tenKhoaHoc)
                        }
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
                  colSpan="5"
                  style={{ textAlign: "center", padding: "20px" }}
                >
                  {searchTerm
                    ? "Không tìm thấy khóa học nào"
                    : "Chưa có khóa học nào"}
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title={modalMode === "add" ? "Thêm Khóa Học Mới" : "Chỉnh Sửa Khóa Học"}
      >
        <form className={styles.form} onSubmit={handleSubmit}>
          {formError && <div className={styles.formError}>{formError}</div>}

          <div className={styles.formGroup}>
            <label>
              Tên Khóa Học <span style={{ color: "red" }}>*</span>
            </label>
            <input
              type="text"
              name="tenKhoaHoc"
              value={formData.tenKhoaHoc}
              onChange={handleInputChange}
              placeholder="Nhập tên khóa học"
              disabled={isSaving}
            />
          </div>

          <div className={styles.formGroup}>
            <label>Mô Tả</label>
            <textarea
              name="moTa"
              value={formData.moTa}
              onChange={handleInputChange}
              placeholder="Nhập mô tả khóa học"
              rows="3"
              disabled={isSaving}
            />
          </div>

          <div className={styles.formGroup}>
            <label>Trình Độ</label>
            <select
              name="trinhDo"
              value={formData.trinhDo}
              onChange={handleInputChange}
              disabled={isSaving}
            >
              <option value="BEGINNER">Cơ Bản (Beginner)</option>
              <option value="INTERMEDIATE">Trung Cấp (Intermediate)</option>
              <option value="ADVANCED">Nâng Cao (Advanced)</option>
            </select>
          </div>

          <div className={styles.formGroup}>
            <label>URL Icon</label>
            <input
              type="text"
              name="iconUrl"
              value={formData.iconUrl}
              onChange={handleInputChange}
              placeholder="http://example.com/icon.png"
              disabled={isSaving}
            />
          </div>

          <div className={styles.formActions}>
            <button
              type="button"
              className={styles.cancelBtn}
              onClick={handleCloseModal}
              disabled={isSaving}
            >
              Hủy
            </button>
            <button
              type="submit"
              className={styles.submitBtn}
              disabled={isSaving}
            >
              {isSaving
                ? "Đang lưu..."
                : modalMode === "add"
                ? "Tạo Khóa Học"
                : "Lưu Thay Đổi"}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default Courses;
