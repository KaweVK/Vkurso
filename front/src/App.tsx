import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './pages/login';
import Home from './pages/home'
import Course from './pages/course';
import Lesson from './pages/lesson';
import CourseForm from './pages/course-form';
import ModuleForm from './pages/module-form';
import LessonForm from './pages/lesson-form';

export function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/course/:slug" element={<Course />} />
        <Route path="/course/:slug/module/:moduleId/lesson/:lessonId" element={<Lesson />} />
        <Route path="/instructor/courses/new" element={<CourseForm />} />
        <Route path="/instructor/courses/:courseId/edit" element={<CourseForm />} />
        <Route path="/instructor/courses/:courseId/modules/new" element={<ModuleForm />} />
        <Route path="/instructor/courses/:courseId/modules/:moduleId/edit" element={<ModuleForm />} />
        <Route path="/instructor/courses/:courseId/modules/:moduleId/lessons/new" element={<LessonForm />} />
        <Route path="/instructor/courses/:courseId/modules/:moduleId/lessons/:lessonId/edit" element={<LessonForm />} />
      </Routes>
    </BrowserRouter>
  );
}
