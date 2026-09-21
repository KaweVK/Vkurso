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
import Layout from './components/layouts/default';
import Dashboard from './pages/admin/dashboard';
import Users from './pages/admin/users';
import Categories from './pages/admin/categories';
import Courses from './pages/admin/courses';
import AdminLayout from './components/layouts/admin';
import UserForm from './pages/user-form';
import CategoryForm from './pages/category-form';

export function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        <Route element={<Layout />}>
          <Route path="/" element={<Home />} />
          <Route path='/catalog' element={<Catalog />} />
          <Route path='/about' element={<About />} />
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

            <Route path='/admin' element={<AdminLayout />}>
              <Route index element={<Dashboard />} />
              <Route path='users' element={<Users />} />
              <Route path="users/:userId/edit" element={<UserForm />} />
              <Route path='courses' element={<Courses />} />
              <Route path='categories' element={<Categories />} />
              <Route path="categories/create" element={<CategoryForm />} />
              <Route path="categories/:categoryId/edit" element={<CategoryForm />} />
            </Route>
          </Route>
        </Route>

      </Routes>
    </BrowserRouter>
  );
}
