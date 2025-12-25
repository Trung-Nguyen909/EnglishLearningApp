import { useState, useEffect } from 'react';
import { FaPlus, FaEdit, FaTrash, FaSearch } from 'react-icons/fa';
import styles from './Courses.module.css';
import { courseService } from '../../services/courseService';
import Modal from '../../components/Modal';

const Courses = () => {
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  // Modal State
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalMode, setModalMode] = useState('add');
  const [selectedCourse, setSelectedCourse] = useState(null);
  const [formData, setFormData] = useState({
    tenKhoaHoc: '',
    moTa: '',
    trinhDo: 'BEGINNER',
    iconUrl: ''
  });

  useEffect(() => {
    fetchCourses();
  }, []);

  const fetchCourses = async () => {
    try {
      setLoading(true);
      const data = await courseService.getAllCourses();
      setCourses(data);
    } catch (err) {
      setError('Không thể tải danh sách khóa học');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleOpenModal = (mode, course = null) => {
    setModalMode(mode);
    setSelectedCourse(course);
    if (mode === 'edit' && course) {
      setFormData({
        tenKhoaHoc: course.tenKhoaHoc,
        moTa: course.moTa,
        trinhDo: course.trinhDo || 'BEGINNER',
        iconUrl: course.iconUrl || ''
      });
    } else {
      setFormData({ tenKhoaHoc: '', moTa: '', trinhDo: 'BEGINNER', iconUrl: '' });
    }
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setSelectedCourse(null);
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
      if (modalMode === 'add') {
        const payload = { ...formData, ngayTao: new Date().toISOString().split('T')[0] };
        await courseService.createCourse(payload);
        fetchCourses();
      } else {
        await courseService.updateCourse(selectedCourse.id, formData);
        fetchCourses(); 
      }
      handleCloseModal();
    } catch (err) {
      console.error('Failed to save course', err);
      alert('Lưu khóa học thất bại.');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Bạn có chắc chắn muốn xóa khóa học này không?')) {
      try {
        await courseService.deleteCourse(id);
        setCourses(courses.filter(course => course.id !== id));
      } catch (err) {
        alert('Xóa khóa học thất bại');
        console.error(err);
      }
    }
  };

  if (loading) return <div className={styles.loading}>Đang tải...</div>;
  if (error) return <div className={styles.error}>{error}</div>;

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h2 className={styles.title}>Quản Lý Khóa Học</h2>
        <button className={styles.addBtn} onClick={() => handleOpenModal('add')}>
          <FaPlus />
          Thêm Khóa Học
        </button>
      </div>

      <div className={styles.toolbar}>
        <div className={styles.searchBox}>
          <FaSearch className={styles.searchIcon} />
          <input type="text" placeholder="Tìm kiếm khóa học..." className={styles.searchInput} />
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
            {courses.length > 0 ? courses.map((course) => (
              <tr key={course.id}>
                <td>#{course.id}</td>
                <td className={styles.courseTitle}>
                    <div className={styles.titleWrapper}>
                        
                        {course.tenKhoaHoc}
                    </div>
                </td> 
                <td>{course.moTa || 'Không có mô tả'}</td>
                <td>
                    <span className={styles.badge}>{course.trinhDo}</span>
                </td>
                <td>
                  <div className={styles.actions}>
                    <button 
                        className={styles.actionBtn} 
                        title="Sửa"
                        onClick={() => handleOpenModal('edit', course)}
                    >
                      <FaEdit />
                    </button>
                    <button 
                        className={`${styles.actionBtn} ${styles.deleteBtn}`} 
                        title="Xóa"
                        onClick={() => handleDelete(course.id)}
                    >
                      <FaTrash />
                    </button>
                  </div>
                </td>
              </tr>
            )) : (
                <tr>
                    <td colSpan="5" style={{textAlign: 'center', padding: '20px'}}>Không tìm thấy khóa học nào</td>
                </tr>
            )}
          </tbody>
        </table>
      </div>

      <Modal 
        isOpen={isModalOpen} 
        onClose={handleCloseModal}
        title={modalMode === 'add' ? 'Thêm Khóa Học Mới' : 'Chỉnh Sửa Khóa Học'}
      >
        <form className={styles.form} onSubmit={handleSubmit}>
          <div className={styles.formGroup}>
            <label>Tên Khóa Học</label>
            <input 
              type="text" 
              name="tenKhoaHoc" 
              value={formData.tenKhoaHoc} 
              onChange={handleInputChange} 
              required 
            />
          </div>
          <div className={styles.formGroup}>
            <label>Mô Tả</label>
            <textarea 
              name="moTa" 
              value={formData.moTa} 
              onChange={handleInputChange} 
              rows="3"
            />
          </div>
          <div className={styles.formGroup}>
            <label>Trình Độ</label>
            <select 
              name="trinhDo" 
              value={formData.trinhDo} 
              onChange={handleInputChange}
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
            />
          </div>
          <div className={styles.formActions}>
            <button type="button" className={styles.cancelBtn} onClick={handleCloseModal}>Hủy</button>
            <button type="submit" className={styles.submitBtn}>
              {modalMode === 'add' ? 'Tạo Khóa Học' : 'Lưu Thay Đổi'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default Courses;
