import { useState, useEffect } from 'react';
import { FaUserGraduate, FaBookOpen, FaVideo, FaClipboardList } from 'react-icons/fa';
import styles from './Dashboard.module.css';
import { userService } from '../../services/userService';
import { courseService } from '../../services/courseService';
import { lessonService } from '../../services/lessonService';
import { exerciseService } from '../../services/exerciseService';

const StatCard = ({ title, value, icon, color }) => (
  <div className={styles.card}>
    <div className={styles.cardContent}>
      <div>
        <p className={styles.cardTitle}>{title}</p>
        <h3 className={styles.cardValue}>{value}</h3>
      </div>
      <div className={styles.cardIcon} style={{ backgroundColor: color }}>
        {icon}
      </div>
    </div>
  </div>
);

const Dashboard = () => {
  const [stats, setStats] = useState({
    usersCount: 0,
    coursesCount: 0,
    lessonsCount: 0,
    exercisesCount: 0
  });
  const [topUsers, setTopUsers] = useState([]);
  const [recentCourses, setRecentCourses] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [usersData, coursesData, lessonsData, exercisesData] = await Promise.all([
        userService.getAllUsers(),
        courseService.getAllCourses(),
        lessonService.getAllLessons(),
        exerciseService.getAllExercises()
      ]);

      setStats({
        usersCount: usersData.length,
        coursesCount: coursesData.length,
        lessonsCount: lessonsData.length,
        exercisesCount: exercisesData.length
      });

      // Process Top Users (by Streak)
      const sortedUsers = [...usersData].sort((a, b) => (b.streak || 0) - (a.streak || 0)).slice(0, 5);
      setTopUsers(sortedUsers);

      // Process Recent Courses (Assuming higher ID is newer for now, or just slice)
      // Ideally backend sorts, but we do frontend sort for now
      const sortedCourses = [...coursesData].sort((a, b) => b.id - a.id).slice(0, 5);
      setRecentCourses(sortedCourses);

    } catch (error) {
      console.error('Failed to fetch dashboard data', error);
    } finally {
      setLoading(false);
    }
  };

  const statItems = [
    { title: 'Tổng Người Dùng', value: loading ? '...' : stats.usersCount, icon: <FaUserGraduate />, color: '#E0E7FF' },
    { title: 'Khóa Học', value: loading ? '...' : stats.coursesCount, icon: <FaBookOpen />, color: '#ECFDF5' },
    { title: 'Tổng Bài Học', value: loading ? '...' : stats.lessonsCount, icon: <FaVideo />, color: '#FEF3C7' },
    { title: 'Tổng Bài Tập', value: loading ? '...' : stats.exercisesCount, icon: <FaClipboardList />, color: '#FEE2E2' },
  ];

  return (
    <div>
      <h2 className={styles.pageTitle}>Tổng Quan Hệ Thống</h2>
      
      <div className={styles.statsGrid}>
        {statItems.map((stat, index) => (
          <StatCard key={index} {...stat} />
        ))}
      </div>

      <div className={styles.contentGrid}>
        {/* Top Users Section */}
        <div className={styles.section}>
          <h3 className={styles.sectionTitle}>🏆 Top Học Viên Tích Cực</h3>
          <div className={styles.list}>
            {loading ? <p>Đang tải...</p> : topUsers.map((user, index) => (
              <div key={user.id} className={styles.listItem}>
                <div className={styles.rank}>{index + 1}</div>
                <div className={styles.avatar}>
                    <FaUserGraduate />
                </div>
                <div className={styles.info}>
                  <span className={styles.name}>{user.hoTen || user.name || user.tenDangNhap}</span>
                  <span className={styles.subText}>Streak: {user.streak || 0} ngày 🔥</span>
                </div>
              </div>
            ))}
            {!loading && topUsers.length === 0 && <p className={styles.subText}>Chưa có dữ liệu người dùng</p>}
          </div>
        </div>

        {/* New Courses Section */}
        <div className={styles.section}>
          <h3 className={styles.sectionTitle}>📚 Khóa Học Mới Nhất</h3>
           <div className={styles.list}>
            {loading ? <p>Đang tải...</p> : recentCourses.map((course) => (
              <div key={course.id} className={styles.listItem}>
                <div className={styles.avatar} style={{color: '#10B981'}}>
                    <FaBookOpen />
                </div>
                <div className={styles.info}>
                  <span className={styles.name}>{course.tenKhoaHoc}</span>
                  <span className={styles.subText}>{course.trinhDo || 'Cơ bản'}</span>
                </div>
                 <span className={styles.badge} style={{backgroundColor: '#ECFDF5', color: '#059669'}}>
                    Mới
                </span>
              </div>
            ))}
             {!loading && recentCourses.length === 0 && <p className={styles.subText}>Chưa có khóa học nào</p>}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
