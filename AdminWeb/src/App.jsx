import { Routes, Route } from 'react-router-dom';
import MainLayout from './layout/MainLayout';
import Dashboard from './pages/Dashboard';
import Courses from './pages/Courses';
import Lessons from './pages/Lessons';
import Exercises from './pages/Exercises';
import Users from './pages/Users';
import Login from './pages/Login';

function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/" element={<MainLayout />}>
        <Route index element={<Dashboard />} />
        <Route path="courses" element={<Courses />} />
        <Route path="lessons" element={<Lessons />} />
        <Route path="exercises" element={<Exercises />} />
        <Route path="users" element={<Users />} />
      </Route>
    </Routes>
  );
}

export default App;
