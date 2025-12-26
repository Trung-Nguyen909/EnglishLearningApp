import { Routes, Route, Navigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import MainLayout from './layout/MainLayout';
import Dashboard from './pages/Dashboard';
import Courses from './pages/Courses';
import Lessons from './pages/Lessons';
import Exercises from './pages/Exercises';
import Users from './pages/Users';
import Login from './pages/Login';
import authService from './services/authService';

function ProtectedRoute({ children }) {
  const [isAuthenticated, setIsAuthenticated] = useState(null);

  useEffect(() => {
    setIsAuthenticated(authService.isAuthenticated());
  }, []);

  if (isAuthenticated === null) {
    return <div>Loading...</div>;
  }

  return isAuthenticated ? children : <Navigate to="/login" replace />;
}

function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route 
        path="/" 
        element={
          <ProtectedRoute>
            <MainLayout />
          </ProtectedRoute>
        }
      >
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
