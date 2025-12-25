import { useState, useEffect } from 'react';
import { FaPlus, FaEdit, FaTrash, FaSearch, FaFileAlt } from 'react-icons/fa';
import styles from './Lessons.module.css';
import { lessonService } from '../../services/lessonService';
import { courseService } from '../../services/courseService';
import Modal from '../../components/Modal';

const Lessons = () => {
  const [lessons, setLessons] = useState([]);
  const [courses, setCourses] = useState([]); // For Course Select Dropdown
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Modal State
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalMode, setModalMode] = useState('add');
  const [selectedLesson, setSelectedLesson] = useState(null);
  const [formData, setFormData] = useState({
    tenBaiHoc: '',
    moTa: '',
    noiDung: '', // New Content Field
    idKhoaHoc: '', 
    thuTuBaiHoc: 0,
    trangThai: 'ACTIVE',
    iconUrl: ''
  });

  useEffect(() => {
    fetchLessons();
    fetchCourses();
  }, []);

  const fetchLessons = async () => {
    try {
      setLoading(true);
      const data = await lessonService.getAllLessons();
      setLessons(data);
    } catch (err) {
      setError('Không thể tải danh sách bài học');
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
        console.error('Failed to fetch courses for select', err);
    }
  };

  const handleOpenModal = (mode, lesson = null) => {
    setModalMode(mode);
    setSelectedLesson(lesson);
    if (mode === 'edit' && lesson) {
      setFormData({
        tenBaiHoc: lesson.tenBaiHoc,
        moTa: lesson.moTa,
        noiDung: lesson.noiDung || '',
        idKhoaHoc: lesson.idKhoaHoc || lesson.idKhoaHoc, // Assuming idKhoaHoc is consistently used now
        thuTuBaiHoc: lesson.thuTuBaiHoc || 0,
        trangThai: lesson.trangThai || 'ACTIVE',
        iconUrl: lesson.iconUrl || ''
      });
    } else {
      setFormData({ tenBaiHoc: '', moTa: '', noiDung: '', idKhoaHoc: '', thuTuBaiHoc: 0, trangThai: 'ACTIVE', iconUrl: '' });
    }
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setSelectedLesson(null);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const payload = {
          ...formData,
          idKhoaHoc: parseInt(formData.idKhoaHoc)
      };

      if (modalMode === 'add') {
        await lessonService.createLesson(payload);
        fetchLessons(); 
      } else {
        await lessonService.updateLesson(selectedLesson.id, payload);
        fetchLessons(); 
      }
      handleCloseModal();
    } catch (err) {
      console.error('Failed to save lesson', err);
      alert('Lưu bài học thất bại.');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Bạn có chắc chắn muốn xóa bài học này không?')) {
      try {
        await lessonService.deleteLesson(id);
        setLessons(lessons.filter(l => l.id !== id));
      } catch (err) {
        alert('Xóa bài học thất bại');
        console.error(err);
      }
    }
  };

  if (loading) return <div className={styles.loading}>Đang tải...</div>;
  if (error) return <div className={styles.error}>{error}</div>;

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h2 className={styles.title}>Quản Lý Bài Học</h2>
        <button className={styles.addBtn} onClick={() => handleOpenModal('add')}>
          <FaPlus />
          Thêm Bài Học
        </button>
      </div>

      <div className={styles.toolbar}>
        <div className={styles.searchBox}>
          <FaSearch className={styles.searchIcon} />
          <input type="text" placeholder="Tìm kiếm bài học..." className={styles.searchInput} />
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
            {lessons.length > 0 ? lessons.map((lesson) => (
              <tr key={lesson.id}>
                <td>#{lesson.id}</td>
                <td className={styles.lessonTitle}>
                    <div className={styles.titleWrapper}>
                        <FaFileAlt className={styles.icon} />
                        {lesson.tenBaiHoc}
                    </div>
                </td>
                <td>{lesson.idKhoaHoc}</td>
                <td><span className={styles.badge}>{lesson.trangThai}</span></td>
                <td>{lesson.thuTuBaiHoc}</td>
                <td>
                  <div className={styles.actions}>
                    <button 
                        className={styles.actionBtn} 
                        title="Sửa"
                        onClick={() => handleOpenModal('edit', lesson)}
                    >
                      <FaEdit />
                    </button>
                    <button 
                        className={`${styles.actionBtn} ${styles.deleteBtn}`} 
                        title="Xóa"
                        onClick={() => handleDelete(lesson.id)}
                    >
                      <FaTrash />
                    </button>
                  </div>
                </td>
              </tr>
            )) : (
                 <tr>
                    <td colSpan="6" style={{textAlign: 'center', padding: '20px'}}>Không tìm thấy bài học nào</td>
                </tr>
            )}
          </tbody>
        </table>
      </div>

      <Modal 
        isOpen={isModalOpen} 
        onClose={handleCloseModal}
        title={modalMode === 'add' ? 'Thêm Bài Học Mới' : 'Chỉnh Sửa Bài Học'}
      >
        <form className={styles.form} onSubmit={handleSubmit}>
          <div className={styles.formGroup}>
            <label>Tên Bài Học</label>
            <input 
              type="text" 
              name="tenBaiHoc" 
              value={formData.tenBaiHoc} 
              onChange={handleInputChange} 
              required 
            />
          </div>
          <div className={styles.formGroup}>
            <label>Thuộc Khóa Học</label>
            <select 
                name="idKhoaHoc" 
                value={formData.idKhoaHoc} 
                onChange={handleInputChange}
                required
            >
                <option value="">Chọn khóa học</option>
                {courses.map(course => (
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
              rows="2"
            />
          </div>
          <div className={styles.formGroup}>
            <label>Nội Dung (Content)</label>
            <textarea 
              name="noiDung" 
              value={formData.noiDung} 
              onChange={handleInputChange} 
              rows="4"
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
                />
            </div>
            <div className={styles.formGroup}>
                <label>Trạng Thái</label>
                <select 
                    name="trangThai" 
                    value={formData.trangThai} 
                    onChange={handleInputChange}
                >
                    <option value="ACTIVE">Hoạt động (Active)</option>
                    <option value="INACTIVE">Ngưng hoạt động (Inactive)</option>
                </select>
            </div>
          </div>
          <div className={styles.formActions}>
            <button type="button" className={styles.cancelBtn} onClick={handleCloseModal}>Hủy</button>
            <button type="submit" className={styles.submitBtn}>
              {modalMode === 'add' ? 'Tạo Bài Học' : 'Lưu Thay Đổi'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default Lessons;
