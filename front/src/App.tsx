import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './pages/login';
import Home from './pages/home'
import Course from './pages/course';

export function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/course/:slug" element={<Course />} />
      </Routes>
    </BrowserRouter>
  );
}
