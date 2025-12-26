import { useState, useEffect } from "react";
import { FaPlus, FaEdit, FaTrash, FaSearch, FaFileAlt } from "react-icons/fa";
import styles from "./Lessons.module.css";
import { lessonService } from "../../services/lessonService";
import { courseService } from "../../services/courseService";
import Modal from "../../components/Modal";

const Lessons = () => {
  const [lessons, setLessons] = useState([]);
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [successMessage, setSuccessMessage] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");

  // Modal State
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalMode, setModalMode] = useState("add");
  const [selectedLesson, setSelectedLesson] = useState(null);
  const [isSaving, setIsSaving] = useState(false);
  const [formError, setFormError] = useState(null);

  const [formData, setFormData] = useState({
    tenBaiHoc: "",
    moTa: "",
    noiDung: "",
    idKhoaHoc: "",
    thuTuBaiHoc: 0,
    trangThai: "Chưa làm",
    iconUrl: "",
  });

  useEffect(() => {
    fetchLessons();
    fetchCourses();
  }, []);

  // Tự động xóa success message sau 3 giây
  useEffect(() => {
    if (successMessage) {
      const timer = setTimeout(() => setSuccessMessage(null), 3000);
      return () => clearTimeout(timer);
    }
  }, [successMessage]);

  const fetchLessons = async () => {
    try {
      setLoading(true);
      const data = await lessonService.getAllLessons();
      setLessons(data);
    } catch (err) {
      setError("Không thể tải danh sách bài học");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const fetchCourses = async () => {
    try {
      const data = await courseService.getAllCourses();
      setCourses(data);
    } catch (err) {
      console.error("Failed to fetch courses for select", err);
    }
  };

  const handleOpenModal = (mode, lesson = null) => {
    setModalMode(mode);
    setSelectedLesson(lesson);
    setFormError(null);
    if (mode === "edit" && lesson) {
      setFormData({
        tenBaiHoc: lesson.tenBaiHoc,
        moTa: lesson.moTa,
        noiDung: lesson.noiDung || "",
        idKhoaHoc: lesson.idKhoaHoc || lesson.idKhoaHoc, // Assuming idKhoaHoc is consistently used now
        thuTuBaiHoc: lesson.thuTuBaiHoc || 0,
        trangThai: lesson.trangThai || "Chưa làm",
        iconUrl: lesson.iconUrl || "",
      });
    } else {
      setFormData({
        tenBaiHoc: "",
        moTa: "",
        noiDung: "",
        idKhoaHoc: "",
        thuTuBaiHoc: 0,
        trangThai: "Chưa làm",
        iconUrl: "",
      });
    }
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setSelectedLesson(null);
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
    if (!formData.tenBaiHoc.trim()) {
      setFormError("Tên bài học không được để trống");
      return;
    }
    if (!formData.idKhoaHoc) {
      setFormError("Khóa học không được để trống");
      return;
    }

    try {
      setIsSaving(true);
      setFormError(null);

      const payload = {
        ...formData,
        tenBaiHoc: formData.tenBaiHoc.trim(),
        moTa: formData.moTa.trim(),
        noiDung: formData.noiDung.trim(),
        idKhoaHoc: parseInt(formData.idKhoaHoc),
        thuTuBaiHoc: parseInt(formData.thuTuBaiHoc) || 0,
        trangThai: formData.trangThai,
      };

      if (modalMode === "add") {
        await lessonService.createLesson(payload);
        console.log("✅ Thêm bài học thành công");
        setSuccessMessage("Thêm bài học thành công");
      } else {
        await lessonService.updateLesson(selectedLesson.id, payload);
        console.log("✅ Cập nhật bài học thành công");
        setSuccessMessage("Cập nhật bài học thành công");
      }

      await fetchLessons();

      // Đóng modal sau 500ms
      setTimeout(() => {
        handleCloseModal();
      }, 500);
    } catch (err) {
      console.error("❌ Chi tiết lỗi:", err.response || err);
      let errorMessage = "Không thể lưu bài học";

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

  const handleDelete = async (id, lessonName) => {
    if (
      window.confirm(
        `Bạn có chắc chắn muốn xóa bài học \"${lessonName}\" không?`
      )
    ) {
      try {
        await lessonService.deleteLesson(id);
        setLessons(lessons.filter((l) => l.id !== id));
        setSuccessMessage("Xóa bài học thành công");
      } catch (err) {
        const errorMessage =
          err.response?.data?.message || "Xóa bài học thất bại";
        setError(errorMessage);
        console.error(err);
      }
    }
  };

  // Filter lessons by search term
  const filteredLessons = lessons.filter(
    (lesson) =>
      lesson.tenBaiHoc.toLowerCase().includes(searchTerm.toLowerCase()) ||
      (lesson.moTa &&
        lesson.moTa.toLowerCase().includes(searchTerm.toLowerCase()))
  );

  if (loading) return <div className={styles.loading}>Đang tải...</div>;

  return (
    <div className={styles.container}>
      {error && <div className={styles.error}>{error}</div>}
      {successMessage && <div className={styles.success}>{successMessage}</div>}
      <div className={styles.header}>
        <h2 className={styles.title}>Quản Lý Bài Học</h2>
        <button
          className={styles.addBtn}
          onClick={() => handleOpenModal("add")}
        >
          <FaPlus />
          Thêm Bài Học
        </button>
      </div>

      <div className={styles.toolbar}>
        <div className={styles.searchBox}>
          <FaSearch className={styles.searchIcon} />
          <input
            type="text"
            placeholder="Tìm kiếm bài học..."
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
              <th>Tên Bài Học</th>
              <th>ID Khóa Học</th>
              <th>Trạng Thái</th>
              <th>Thứ Tự</th>
              <th>Thao Tác</th>
            </tr>
          </thead>
          <tbody>
            {filteredLessons.length > 0 ? (
              filteredLessons.map((lesson) => (
                <tr key={lesson.id}>
                  <td>#{lesson.id}</td>
                  <td className={styles.lessonTitle}>
                    <div className={styles.titleWrapper}>
                      <FaFileAlt className={styles.icon} />
                      {lesson.tenBaiHoc}
                    </div>
                  </td>
                  <td>{lesson.idKhoaHoc}</td>
                  <td>
                    <span className={styles.badge}>{lesson.trangThai}</span>
                  </td>
                  <td>{lesson.thuTuBaiHoc}</td>
                  <td>
                    <div className={styles.actions}>
                      <button
                        className={styles.actionBtn}
                        title="Sửa"
                        onClick={() => handleOpenModal("edit", lesson)}
                      >
                        <FaEdit />
                      </button>
                      <button
                        className={`${styles.actionBtn} ${styles.deleteBtn}`}
                        title="Xóa"
                        onClick={() =>
                          handleDelete(lesson.id, lesson.tenBaiHoc)
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
                  colSpan="6"
                  style={{ textAlign: "center", padding: "20px" }}
                >
                  {searchTerm
                    ? "Không tìm thấy bài học nào"
                    : "Chưa có bài học nào"}
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title={modalMode === "add" ? "Thêm Bài Học Mới" : "Chỉnh Sửa Bài Học"}
      >
        <form className={styles.form} onSubmit={handleSubmit}>
          {formError && <div className={styles.formError}>{formError}</div>}
          <div className={styles.formGroup}>
            <label>
              Tên Bài Học <span style={{ color: "red" }}>*</span>
            </label>
            <input
              type="text"
              name="tenBaiHoc"
              value={formData.tenBaiHoc}
              onChange={handleInputChange}
              placeholder="Nhập tên bài học"
              disabled={isSaving}
            />
          </div>
          <div className={styles.formGroup}>
            <label>
              Thuộc Khóa Học <span style={{ color: "red" }}>*</span>
            </label>
            <select
              name="idKhoaHoc"
              value={formData.idKhoaHoc}
              onChange={handleInputChange}
              disabled={isSaving}
            >
              <option value="">Chọn khóa học</option>
              {courses.map((course) => (
                <option key={course.id} value={course.id}>
                  {course.tenKhoaHoc}
                </option>
              ))}
            </select>
          </div>
          <div className={styles.formGroup}>
            <label>Mô Tả</label>
            <textarea
              name="moTa"
              value={formData.moTa}
              onChange={handleInputChange}
              placeholder="Nhập mô tả bài học"
              rows="2"
              disabled={isSaving}
            />
          </div>
          <div className={styles.formGroup}>
            <label>Nội Dung (Content)</label>
            <textarea
              name="noiDung"
              value={formData.noiDung}
              onChange={handleInputChange}
              placeholder="Nhập nội dung bài học"
              rows="4"
              disabled={isSaving}
            />
          </div>
          <div className={styles.row}>
            <div className={styles.formGroup}>
              <label>Thứ Tự</label>
              <input
                type="number"
                name="thuTuBaiHoc"
                value={formData.thuTuBaiHoc}
                onChange={handleInputChange}
                disabled={isSaving}
              />
            </div>
            <div className={styles.formGroup}>
              <label>Trạng Thái</label>
              <select
                name="trangThai"
                value={formData.trangThai}
                onChange={handleInputChange}
                disabled={isSaving}
              >
                <option value="Chưa làm">Chưa làm</option>
                <option value="Đang Hoàn thành">Đang Hoàn thành</option>
                <option value="Đã hoàn thành">Đã hoàn thành</option>
              </select>
            </div>
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
                ? "Tạo Bài Học"
                : "Lưu Thay Đổi"}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default Lessons;
