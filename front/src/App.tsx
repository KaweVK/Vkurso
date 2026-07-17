import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './pages/login';
import Home from './pages/home'
import Course from './pages/course';
import Lesson from './pages/lesson';

export function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/course/:slug" element={<Course />} />
        <Route path="/course/:slug/module/:moduleId/lesson/:lessonId" element={<Lesson />} />
      </Routes>
    </BrowserRouter>
  );
}
