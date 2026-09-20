import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './pages/login';
import Register from './pages/register';
import Home from './pages/home'
import Course from './pages/course';
import Lesson from './pages/lesson';
import CourseForm from './pages/course-form';
import ModuleForm from './pages/module-form';
import LessonForm from './pages/lesson-form';
import Catalog from './pages/catalog';
import About from './pages/about';
import MyJourney from './pages/my-journey'
import InstructorPainel from './pages/instructor-painel';
import RequireAuth from './components/require-auth'
import Me from './pages/me/indes';

export function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path='/catalog' element={<Catalog />} />
        <Route path='/about' element={<About />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/course/:slug" element={<Course />} />
        <Route path="/course/:slug/module/:moduleId/lesson/:lessonId" element={<Lesson />} />

        <Route element={<RequireAuth />}>
          <Route path='/journey' element={<MyJourney />} />
          <Route path='/me' element={<Me />} />
          <Route path='/painel' element={<InstructorPainel />} />
          <Route path="/instructor/courses/new" element={<CourseForm />} />
          <Route path="/instructor/courses/:courseId/edit" element={<CourseForm />} />
          <Route path="/instructor/courses/:courseId/modules/new" element={<ModuleForm />} />
          <Route path="/instructor/courses/:courseId/modules/:moduleId/edit" element={<ModuleForm />} />
          <Route path="/instructor/courses/:courseId/modules/:moduleId/lessons/new" element={<LessonForm />} />
          <Route path="/instructor/courses/:courseId/modules/:moduleId/lessons/:lessonId/edit" element={<LessonForm />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
