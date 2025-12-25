import { FaBell, FaSearch, FaUserCircle } from 'react-icons/fa';
import styles from './Header.module.css';

const Header = () => {
  return (
    <header className={styles.header}>
      <div className={styles.searchContainer}>
        <FaSearch className={styles.searchIcon} />
        <input 
          type="text" 
          placeholder="Search..." 
          className={styles.searchInput} 
        />
      </div>

      <div className={styles.actions}>
        <button className={styles.iconBtn}>
          <FaBell />
          <span className={styles.badge}>3</span>
        </button>
        <div className={styles.profile}>
          <div className={styles.avatar}>
            <FaUserCircle />
          </div>
          <div className={styles.userInfo}>
            <span className={styles.userName}>Admin User</span>
            <span className={styles.userRole}>Super Admin</span>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;
