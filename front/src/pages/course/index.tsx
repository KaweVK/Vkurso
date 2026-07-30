import '../../index.css';
import Navbar from '../../components/navbar';
import { Menu, MenuButton, MenuItem, MenuItems } from '@headlessui/react';
import { ChevronDownIcon } from '@heroicons/react/20/solid';
import { PencilIcon, TrashIcon } from '@heroicons/react/24/outline';
import { useCourse } from '../../hooks/useCourses';
import Loading from '../../components/loading';
import { useNavigate, useParams, Link } from 'react-router-dom';
import Thumb from '../../assets/image.png';
import { useCategories } from '../../hooks/useCategories';
import { useEnrollment } from '../../hooks/useEnrollment';
import { useAuth } from '../../hooks/useAuth';
import { useState } from 'react';
import type { CourseStatus } from '../../types/course';
import courseService from '../../services/courseService';
import moduleService from '../../services/moduleService';

function Course() {
    const { slug } = useParams<{ slug: string }>();
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [publishing, setPublishing] = useState(false);
    const [publishError, setPublishError] = useState('');
    const [courseStatus, setCourseStatus] = useState<CourseStatus>();
    const [deletingModuleId, setDeletingModuleId] = useState<number>();
    const { course, loading: loadingCourse } = useCourse(slug);
    const { isAuthenticated, userId } = useAuth();
    const { categories, loading: loadingCategories } = useCategories();
    const { enrolled, loading: loadingEnroll, enroll, cancel } = useEnrollment(course?.id);

    const courseCategories = categories.filter((category) => course?.categoryIds?.includes(category.id));
    const isCourseOwner = Boolean(course && userId === course.instructorId);
    const displayedStatus = courseStatus ?? course?.status;

    async function handlePublish() {
        if (!course) return;
        setPublishing(true);
        setPublishError('');
        try {
            const publishedCourse = await courseService.publish(course.id);
            setCourseStatus(publishedCourse.status);
        } catch {
            setPublishError('Não foi possível publicar. O curso precisa ter módulos e aulas.');
        } finally {
            setPublishing(false);
        }
    }

    const navigate = useNavigate();

    async function handleDeleteModule(moduleId: number) {
        if (!course || !window.confirm('Tem certeza que deseja excluir este módulo e todas as suas aulas?')) return;
        setDeletingModuleId(moduleId);
        try {
            await moduleService.delete(course.id, moduleId);
            navigate(0);
        } catch {
            window.alert('Não foi possível excluir o módulo.');
        } finally {
            setDeletingModuleId(undefined);
        }
    }

    if (loadingCourse) {
        return (
            <>
                <Navbar />
                <div className="flex h-screen w-full items-center justify-center gap-8 bg-indigo-100 dark:bg-indigo-950"><Loading /></div>
            </>
        );
    }

    return (
        <>
            <Navbar />
            <div className="min-h-screen bg-indigo-100 px-10 py-8 dark:bg-indigo-950">
                <div className="flex min-h-screen rounded-xl bg-indigo-950 px-10 py-8 dark:bg-indigo-200">
                    <div className="w-3/5">
                        <img src={Thumb} alt="Thumbnail" className="ml-[6%] w-[90%] rounded-lg" />

                        <div>
                            {course?.modules?.map((module, moduleIndex) => (
                                <div key={module.id} className={`ml-[6%] mt-[10px] w-[90%] ${moduleIndex === 0 ? 'flex items-start gap-3' : ''}`}>
                                    <Menu as="div" className={`mb-2 ${moduleIndex === 0 ? 'min-w-0 flex-1' : 'w-full'}`}>
                                        <MenuButton className="flex w-full justify-between rounded bg-indigo-600 p-3 text-white">
                                            <span className="flex min-w-0 items-center gap-2">
                                                <span className="truncate">{module.title}</span>
                                                {isCourseOwner && course && <Link to={`/instructor/courses/${course.id}/modules/${module.id}/edit`} aria-label="Editar módulo" title="Editar módulo" className="shrink-0 text-white hover:text-indigo-200"><PencilIcon className="h-5 w-5" /></Link>}
                                                {isCourseOwner && <button type="button" onClick={(event) => { event.stopPropagation(); void handleDeleteModule(module.id); }} disabled={deletingModuleId === module.id} aria-label="Excluir módulo" title="Excluir módulo" className="shrink-0 text-white hover:text-red-200 disabled:opacity-50"><TrashIcon className="h-5 w-5" /></button>}
                                            </span>
                                            <ChevronDownIcon className="h-5 w-5 shrink-0" />
                                        </MenuButton>
                                        <MenuItems className="bg-gray-100">
                                            {isCourseOwner && course && (
                                                <div className="flex gap-3 border-b border-indigo-100 p-3 text-sm">
                                                    <Link to={`/instructor/courses/${course.id}/modules/${module.id}/lessons/new`} className="text-indigo-700 hover:underline">Nova aula</Link>
                                                </div>
                                            )}
                                            {module.lessons.map((lesson) => (
                                                <MenuItem key={lesson.id}>
                                                    <div className="flex items-center justify-between bg-indigo-300">
                                                        {isAuthenticated && (isCourseOwner || enrolled || lesson.freePreview) ? (
                                                            <Link to={`/course/${slug}/module/${module.id}/lesson/${lesson.id}`} className="block flex-1 p-3">{lesson.title}</Link>
                                                        ) : <div className="block flex-1 cursor-not-allowed p-3">{lesson.title}</div>}
                                                        {isCourseOwner && course && <Link to={`/instructor/courses/${course.id}/modules/${module.id}/lessons/${lesson.id}/edit`} aria-label="Editar aula" title="Editar aula" className="px-3 text-indigo-800 hover:text-indigo-950"><PencilIcon className="h-4 w-4" /></Link>}
                                                    </div>
                                                </MenuItem>
                                            ))}
                                        </MenuItems>
                                    </Menu>
                                    {moduleIndex === 0 && isCourseOwner && course && <Link to={`/instructor/courses/${course.id}/modules/new`} className="rounded bg-indigo-600 px-4 py-3 text-sm font-semibold text-white hover:bg-indigo-700">Novo módulo</Link>}
                                </div>
                            ))}
                            {(!course?.modules || course.modules.length === 0) && isCourseOwner && course && (
                                <div className="ml-[6%] mt-[10px] w-[90%]">
                                    <Link to={`/instructor/courses/${course.id}/modules/new`} className="inline-block rounded bg-indigo-600 px-4 py-3 text-sm font-semibold text-white hover:bg-indigo-700">Novo módulo</Link>
                                </div>
                            )}
                        </div>
                    </div>

                    <div className="relative ml-[3%] w-2/5 rounded-xl bg-white px-10 py-8">
                        <div className="flex items-start justify-between gap-4">
                            <h1 className="ml-[6%] text-5xl font-bold text-black">{course?.title}</h1>
                            <div className="flex items-center gap-2">
                                {isCourseOwner && displayedStatus === 'DRAFT' && <button type="button" onClick={handlePublish} disabled={publishing} className="rounded bg-emerald-600 px-3 py-2 text-sm font-semibold text-white hover:bg-emerald-700 disabled:opacity-50">{publishing ? 'Publicando...' : 'Publicar'}</button>}
                                {isCourseOwner && course && <Link to={`/instructor/courses/${course.id}/edit`} aria-label="Editar curso" title="Editar curso" className="shrink-0 rounded p-2 text-indigo-700 hover:bg-indigo-50"><PencilIcon className="h-5 w-5" /></Link>}
                            </div>
                        </div>
                        {displayedStatus === 'DRAFT' && <span className="ml-[6%] mt-3 inline-block rounded bg-amber-100 px-2 py-1 text-sm font-semibold text-amber-800">Rascunho</span>}
                        {publishError && <p className="ml-[6%] mt-3 text-sm text-red-600">{publishError}</p>}
                        <div className="ml-[6%] mt-[15px] text-2xl text-black">{!loadingCategories && courseCategories.map((category) => category.name).join(', ')}</div>
                        <div className="ml-[6%] mt-[15px] text-2xl text-black">{course?.description}</div>

                        <button onClick={enrolled ? () => setConfirmOpen(true) : enroll} disabled={loadingEnroll} className={`absolute bottom-2 rounded p-2 text-2xl text-white disabled:opacity-50 ${enrolled ? 'bg-red-400' : 'bg-blue-400'}`}>
                            {loadingEnroll ? <Loading /> : enrolled ? 'Cancelar matrícula' : 'Matricule-se'}
                        </button>
                        {confirmOpen && (
                            <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50">
                                <div className="max-w-sm rounded bg-white p-6">
                                    <p className="mb-4">Tem certeza que deseja cancelar sua matrícula?</p>
                                    <div className="flex justify-end gap-2">
                                        <button onClick={() => setConfirmOpen(false)} className="rounded border px-4 py-2">Voltar</button>
                                        <button onClick={() => { setConfirmOpen(false); cancel(); }} className="rounded bg-red-400 px-4 py-2 text-white">Sim, cancelar</button>
                                    </div>
                                </div>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </>
    );
}

export default Course;
