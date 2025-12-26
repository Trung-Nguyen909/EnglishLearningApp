import { useState, useEffect } from 'react';
import { FaPlus, FaEdit, FaTrash, FaSearch } from 'react-icons/fa';
import styles from './Exercises.module.css';
import { exerciseService } from '../../services/exerciseService';
import { lessonService } from '../../services/lessonService';
import Modal from '../../components/Modal';

const Exercises = () => {
  const [exercises, setExercises] = useState([]);
  const [lessons, setLessons] = useState([]); 
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Modal State
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalMode, setModalMode] = useState('add');
  const [selectedExercise, setSelectedExercise] = useState(null);
  const [formData, setFormData] = useState({
    tenBaiTap: '',
    idBaiHoc: '',
    loaiBaiTap: 'VOCABULARY', // Default type
    trangThai: 'Chưa làm',
    capdo: 'EASY',
    thoigian: '' // LocalTime, e.g. "00:00:00"
  });

  useEffect(() => {
    fetchExercises();
    fetchLessons();
  }, []);

  const fetchExercises = async () => {
    try {
      setLoading(true);
      const data = await exerciseService.getAllExercises();
      setExercises(data);
    } catch (err) {
      setError('Không thể tải danh sách bài tập');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const fetchLessons = async () => {
      try {
          const data = await lessonService.getAllLessons();
          setLessons(data);
      } catch (err) {
          console.error(err);
      }
  };

  const handleOpenModal = (mode, exercise = null) => {
    setModalMode(mode);
    setSelectedExercise(exercise);
    if (mode === 'edit' && exercise) {
      // Handle case where idBaiHoc might be inside a nested object or direct property
      const currentIdBaiHoc = exercise.idBaiHoc || (exercise.baiHoc ? exercise.baiHoc.id : '');
      
      setFormData({
        tenBaiTap: exercise.tenBaiTap,
        idBaiHoc: currentIdBaiHoc,
        loaiBaiTap: exercise.loaiBaiTap || 'VOCABULARY',
        trangThai: exercise.trangThai || 'Chưa làm',
        capdo: exercise.capdo || 'EASY',
        thoigian: exercise.thoigian || ''
      });
    } else {
      setFormData({ 
          tenBaiTap: '', 
          idBaiHoc: '', 
          loaiBaiTap: 'VOCABULARY', 
          trangThai: 'Chưa làm',
          capdo: 'EASY',
          thoigian: ''
      });
    }
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setSelectedExercise(null);
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
      // Ensure time is in HH:mm:ss format
      let formattedTime = formData.thoigian;
      if (formattedTime && formattedTime.length === 5) {
          formattedTime += ':00';
      }

      const payload = {
          ...formData,
          idBaiHoc: formData.idBaiHoc ? parseInt(formData.idBaiHoc) : null,
          thoigian: formattedTime || null
      };
      console.log('Sending payload:', payload);

      if (modalMode === 'add') {
        await exerciseService.createExercise(payload);
        fetchExercises();
        alert('Tạo bài tập thành công!');
      } else {
        await exerciseService.updateExercise(selectedExercise.id, payload);
        fetchExercises();
        alert('Cập nhật bài tập thành công!');
      }
      handleCloseModal();
    } catch (err) {
      console.error('Failed to save exercise', err);
      // Log detailed error info for debugging
      if (err.response) {
          console.log('Error Status:', err.response.status);
          console.log('Error Headers:', err.response.headers);
          console.log('Error Data:', err.response.data);
      }
      const errorMessage = err.response?.data?.message || JSON.stringify(err.response?.data) || err.message || 'Lưu bài tập thất bại';
      alert('Lỗi: ' + errorMessage);
    }
  };

  const handleDelete = async (id) => {
      if (window.confirm('Bạn có chắc chắn muốn xóa bài tập này không?')) {
          try {
              await exerciseService.deleteExercise(id);
              setExercises(exercises.filter(e => e.id !== id));
          } catch (err) {
              alert('Xóa bài tập thất bại');
              console.error(err);
          }
      }
  };

  if (loading) return <div className={styles.loading}>Đang tải...</div>;
  if (error) return <div className={styles.error}>{error}</div>;

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h2 className={styles.title}>Quản Lý Bài Tập</h2>
        <button className={styles.addBtn} onClick={() => handleOpenModal('add')}>
          <FaPlus />
          Thêm Bài Tập
        </button>
      </div>

      <div className={styles.toolbar}>
        <div className={styles.searchBox}>
          <FaSearch className={styles.searchIcon} />
          <input type="text" placeholder="Tìm kiếm bài tập..." className={styles.searchInput} />
        </div>
      </div>

      <div className={styles.tableContainer}>
        <table className={styles.table}>
          <thead>
            <tr>
              <th>ID</th>
              <th>Tên Bài Tập</th>
              <th>Loại</th>
              <th>Cấp Độ</th>
              <th>Trạng Thái</th>
              <th>Thao Tác</th>
            </tr>
          </thead>
          <tbody>
            {exercises.length > 0 ? exercises.map((exercise) => (
              <tr key={exercise.id}>
                <td>#{exercise.id}</td>
                <td className={styles.questionText}>{exercise.tenBaiTap}</td>
                <td><span className={styles.badge}>{exercise.loaiBaiTap}</span></td>
                <td>{exercise.capdo}</td>
                <td>{exercise.trangThai}</td>
                <td>
                  <div className={styles.actions}>
                    <button 
                        className={styles.actionBtn} 
                        title="Sửa"
                        onClick={() => handleOpenModal('edit', exercise)}
                    >
                      <FaEdit />
                    </button>
                    <button 
                        className={`${styles.actionBtn} ${styles.deleteBtn}`} 
                        title="Xóa"
                        onClick={() => handleDelete(exercise.id)}
                    >
                      <FaTrash />
                    </button>
                  </div>
                </td>
              </tr>
            )) : (
                <tr>
                    <td colSpan="6" style={{textAlign: 'center', padding: '20px'}}>Không tìm thấy bài tập nào</td>
                </tr>
            )}
          </tbody>
        </table>
      </div>

      <Modal 
        isOpen={isModalOpen} 
        onClose={handleCloseModal}
        title={modalMode === 'add' ? 'Thêm Bài Tập Mới' : 'Chỉnh Sửa Bài Tập'}
      >
        <form className={styles.form} onSubmit={handleSubmit}>
          <div className={styles.formGroup}>
            <label>Tên Bài Tập</label>
            <input 
              type="text"
              name="tenBaiTap" 
              value={formData.tenBaiTap} 
              onChange={handleInputChange} 
              required 
            />
          </div>
          <div className={styles.formGroup}>
            <label>Thuộc Bài Học</label>
            <select 
                name="idBaiHoc" 
                value={formData.idBaiHoc} 
                onChange={handleInputChange}
                required
            >
                <option value="">Chọn bài học</option>
                {lessons.map(lesson => (
                     <option key={lesson.id} value={lesson.id}>
                        {lesson.tenBaiHoc}
                    </option>
                ))}
            </select>
          </div>
          <div className={styles.row}>
             <div className={styles.formGroup}>
                <label>Loại Bài Tập (Type)</label>
                <select 
                    name="loaiBaiTap" 
                    value={formData.loaiBaiTap} 
                    onChange={handleInputChange}
                >
                    <option value="VOCABULARY">Từ vựng (Vocabulary)</option>
                    <option value="GRAMMAR">Ngữ pháp (Grammar)</option>
                    <option value="LISTENING">Nghe (Listening)</option>
                    <option value="PRONUNCIATION">Phát âm (Pronunciation)</option>
                </select>
            </div>
            <div className={styles.formGroup}>
                <label>Cấp Độ (Level)</label>
                <select 
                    name="capdo" 
                    value={formData.capdo} 
                    onChange={handleInputChange}
                >
                    <option value="EASY">Dễ (Easy)</option>
                    <option value="MEDIUM">Vừa (Medium)</option>
                    <option value="HARD">Khó (Hard)</option>
                </select>
            </div>
          </div>
          <div className={styles.row}>
            <div className={styles.formGroup}>
                <label>Trạng Thái</label>
                 <select 
                    name="trangThai" 
                    value={formData.trangThai} 
                    onChange={handleInputChange}
                >
                    <option value="Chưa làm">Chưa làm</option>
                    <option value="Đang Hoàn thành">Đang Hoàn thành</option>
                    <option value="Đã hoàn thành">Đã hoàn thành</option>
                </select>
            </div>
            <div className={styles.formGroup}>
                <label>Thời Gian Làm Bài (HH:mm:ss)</label>
                <input 
                    type="text" 
                    name="thoigian" 
                    placeholder="00:15:00"
                    value={formData.thoigian} 
                    onChange={handleInputChange} 
                />
            </div>
          </div>
         
          <div className={styles.formActions}>
            <button type="button" className={styles.cancelBtn} onClick={handleCloseModal}>Hủy</button>
            <button type="submit" className={styles.submitBtn}>
              {modalMode === 'add' ? 'Tạo Bài Tập' : 'Lưu Thay Đổi'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default Exercises;
