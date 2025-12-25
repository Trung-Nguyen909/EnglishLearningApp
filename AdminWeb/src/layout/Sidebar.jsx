import { NavLink, useNavigate } from 'react-router-dom';
import { 
  FaChartPie, 
  FaBook, 
  FaVideo, 
  FaDumbbell, 
  FaUsers,
  FaSignOutAlt 
} from 'react-icons/fa';
import { PiExamFill } from "react-icons/pi";
import clsx from 'clsx';
import styles from './Sidebar.module.css';

const Sidebar = () => {
  const navigate = useNavigate();
  
  const navItems = [
    { path: '/', label: 'Bảng Điều Khiển', icon: <FaChartPie /> },
    { path: '/courses', label: 'Khóa Học', icon: <FaBook /> },
    { path: '/lessons', label: 'Bài Học', icon: <FaVideo /> },
    { path: '/exercises', label: 'Bài Tập', icon: <PiExamFill /> },
    { path: '/users', label: 'Người Dùng', icon: <FaUsers /> },
  ];

  const handleLogout = () => {
    if (window.confirm('Bạn có chắc chắn muốn đăng xuất không?')) {
        localStorage.removeItem('token');
        navigate('/login');
    }
  };

  return (
    <aside className={styles.sidebar}>
      <div className={styles.logoContainer}>
        <div className={styles.logoIcon}>EL</div>
        <h1 className={styles.logoText}>Quản Trị</h1>
      </div>

      <nav className={styles.nav}>
        <ul className={styles.navList}>
          {navItems.map((item) => (
            <li key={item.path} className={styles.navItem}>
              <NavLink 
                to={item.path} 
                className={({ isActive }) => clsx(styles.navLink, isActive && styles.active)}
              >
                <span className={styles.icon}>{item.icon}</span>
                <span className={styles.label}>{item.label}</span>
              </NavLink>
            </li>
          ))}
        </ul>
      </nav>

      <div className={styles.footer}>
        <button className={styles.logoutBtn} onClick={handleLogout}>
          <FaSignOutAlt />
          <span>Đăng Xuất</span>
        </button>
      </div>
    </aside>
  );
};

export default Sidebar;
