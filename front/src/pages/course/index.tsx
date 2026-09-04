import '../../index.css';
import Navbar from '../../components/navbar';
import { Dialog, DialogPanel, DialogTitle } from '@headlessui/react';
import { ChartBarIcon, ChevronDownIcon, PlayIcon, StarIcon } from '@heroicons/react/20/solid';
import { ArrowRightIcon, ChevronRightIcon, ClockIcon, PencilIcon, TrashIcon } from '@heroicons/react/24/outline';
import { useCourse } from '../../hooks/useCourses';
import Loading from '../../components/loading';
import { useNavigate, useParams, Link } from 'react-router-dom';
import Thumb from '../../assets/image.png';
import { useEnrollment } from '../../hooks/useEnrollment';
import { useAuth } from '../../hooks/useAuth';
import { useState } from 'react';
import type { CourseStatus } from '../../types/course';
import courseService from '../../services/courseService';
import moduleService from '../../services/moduleService';
import Footbar from '../../components/footbar';
import timeFormater from '../../utils/timeFormarter'
import courseLevelFormarter from '../../utils/courseLevelFormater';

type DeleteTarget = { type: 'course' } | { type: 'module'; id: number };

function Course() {
    const { slug } = useParams<{ slug: string }>();
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [publishing, setPublishing] = useState(false);
    const [publishError, setPublishError] = useState('');
    const [courseStatus, setCourseStatus] = useState<CourseStatus>();
    const [deletingModuleId, setDeletingModuleId] = useState<number>();
    const [deletingCourse, setDeletingCourse] = useState(false);
    const [deleteTarget, setDeleteTarget] = useState<DeleteTarget | null>(null);
    const [deleteError, setDeleteError] = useState('');
    const { course, loading: loadingCourse } = useCourse(slug);
    const { isAuthenticated, userId } = useAuth();
    const { enrolled, loading: loadingEnroll, enroll, cancel } = useEnrollment(course?.id);
    const [openModule, setOpenModule] = useState<number | null>(null);

    const isCourseOwner = Boolean(course && userId === course.instructor.id);
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

    function requestDeleteModule(moduleId: number) {
        setDeleteError('');
        setDeleteTarget({ type: 'module', id: moduleId });
    }

    function requestDeleteCourse() {
        setDeleteError('');
        setDeleteTarget({ type: 'course' });
    }

    async function handleConfirmDelete() {
        if (!course || !deleteTarget) return;
        try {
            if (deleteTarget.type === 'module') {
                setDeletingModuleId(deleteTarget.id);
                await moduleService.delete(course.id, deleteTarget.id);
                navigate(0);
            } else {
                setDeletingCourse(true);
                await courseService.delete(course.id);
                navigate('/');
            }
        } catch {
            setDeleteError(deleteTarget.type === 'module' ? 'Não foi possível excluir o módulo.' : 'Não foi possível excluir o curso.');
        } finally {
            setDeletingModuleId(undefined);
            setDeletingCourse(false);
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

    function toggleModule(moduleId: number) {
        setOpenModule(current =>
            current === moduleId ? null : moduleId
        );
    }

    const dialogExclude = (
        <Dialog open={deleteTarget !== null} onClose={() => !deletingCourse && deletingModuleId === undefined && setDeleteTarget(null)} className="relative z-50">
            <div className="fixed inset-0 bg-black/50" aria-hidden="true" />
            <div className="fixed inset-0 flex items-center justify-center p-4">
                <DialogPanel className="w-full max-w-sm rounded bg-white p-6">
                    <DialogTitle className="text-lg font-semibold text-gray-900">Confirmar exclusão</DialogTitle>
                    <p className="mt-3 text-gray-700">{deleteTarget?.type === 'course' ? 'Tem certeza que deseja excluir este curso, seus módulos e aulas?' : 'Tem certeza que deseja excluir este módulo e todas as suas aulas?'}</p>
                    {deleteError && <p className="mt-3 text-sm text-red-600">{deleteError}</p>}
                    <div className="mt-6 flex justify-end gap-2">
                        <button type="button" onClick={() => setDeleteTarget(null)} disabled={deletingCourse || deletingModuleId !== undefined} className="rounded border px-4 py-2">Cancelar</button>
                        <button type="button" onClick={() => void handleConfirmDelete()} disabled={deletingCourse || deletingModuleId !== undefined} className="rounded bg-red-600 px-4 py-2 text-white disabled:opacity-50">{deletingCourse || deletingModuleId !== undefined ? 'Excluindo...' : 'Excluir'}</button>
                    </div>
                </DialogPanel>
            </div>
        </Dialog>
    )

    const dialogCancel = (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50">
            <div className="max-w-sm rounded bg-white p-6">
                <p className="mb-4">Tem certeza que deseja cancelar sua matrícula?</p>
                <div className="flex justify-end gap-2">
                    <button onClick={() => setConfirmOpen(false)} className="rounded border px-4 py-2">Voltar</button>
                    <button onClick={() => { setConfirmOpen(false); cancel(); }} className="rounded bg-red-400 px-4 py-2 text-white">Sim, cancelar</button>
                </div>
            </div>
        </div>
    )

    let courseDuration = 0;
    let totalLessons = 0

    course?.modules.forEach(module => {
        module.lessons.forEach(lesson => {
            totalLessons += 1
            courseDuration += lesson.durationSeconds ?? 0;
        });
    });

    return (
        <main className='bg-indigo-100/30'>
            <Navbar />
            <div className="px-10 py-8">

                <div className="flex items-stretch">
                    <div className="w-3/5">
                        <img src={Thumb} alt="Thumbnail" className="rounded-3xl mb-[20px]" />

                        {/* Menu de aulas e módulos */}
                        <div>
                            {isCourseOwner &&
                                <Link
                                    to={`/instructor/courses/${course?.id}/modules/new`}
                                    className="mb-3 block rounded-xl bg-indigo-600 p-4 text-center text-sm font-semibold text-white transition hover:bg-indigo-700 w-60"
                                >
                                    Novo módulo
                                </Link>
                            }

                            {/* Módulos e Aulas */}
                            {course?.modules?.map((module) => (
                                <div key={module.id}>
                                    <div className="overflow-hidden rounded-xl border border-gray-200 bg-white">
                                        <button
                                            type="button"
                                            onClick={() => toggleModule(module.id)}
                                            className="flex w-full items-start gap-3 p-5 text-left transition hover:bg-gray-200/30"
                                        >
                                            {/* Ordem */}
                                            <span className="flex h-9 w-9 shrink-0 items-center justify-center rounded-full bg-indigo-600 text-sm font-bold text-white">
                                                {module.orderIndex + 1}
                                            </span>

                                            {/* Título + descrição */}
                                            <div className="min-w-0 flex-1">
                                                <h3 className="line-clamp-2 font-semibold text-gray-900">
                                                    {module.title}
                                                </h3>

                                                <p className="mt-1 line-clamp-2 text-sm leading-5 text-gray-500">
                                                    {module.description}
                                                </p>
                                            </div>

                                            {/* Ações */}
                                            {isCourseOwner && (
                                                <div
                                                    className="flex shrink-0 items-center gap-1"
                                                    onClick={(event) => event.stopPropagation()}
                                                >
                                                    <Link
                                                        to={`/instructor/courses/${course.id}/modules/${module.id}/edit`}
                                                        className="rounded-md p-2 text-indigo-600 hover:bg-indigo-50"
                                                    >
                                                        <PencilIcon className="h-5 w-5" />
                                                    </Link>

                                                    <button
                                                        type="button"
                                                        onClick={() => requestDeleteModule(module.id)}
                                                        className="rounded-md p-2 text-red-600 hover:bg-red-50"
                                                    >
                                                        <TrashIcon className="h-5 w-5" />
                                                    </button>
                                                </div>
                                            )}

                                            {/* Chevron */}
                                            <ChevronDownIcon
                                                className={`mt-1 h-5 w-5 shrink-0 text-indigo-600 transition-transform ${openModule === module.id ? 'rotate-180' : ''
                                                    }`}
                                            />
                                        </button>

                                        {openModule === module.id && (
                                            <div className="border-t border-gray-200 bg-gray-50">
                                                <div className="bg-gray-100">
                                                    {module.lessons.length === 0 && isCourseOwner && (
                                                        <div className="p-5 text-sm text-gray-500">
                                                            Este módulo ainda não possui aulas.
                                                        </div>
                                                    )}
                                                    {isCourseOwner && (
                                                        <div className="flex gap-3 border-b border-indigo-100 p-3 text-sm">
                                                            <Link to={`/instructor/courses/${course.id}/modules/${module.id}/lessons/new`} className="text-indigo-700 hover:underline">Nova aula</Link>
                                                        </div>
                                                    )}
                                                    {module.lessons.map((lesson) => (
                                                        <div key={lesson.id}>
                                                            <div className="flex items-center justify-between border-b border-gray-200 bg-white transition hover:bg-indigo-50">
                                                                {isAuthenticated && (isCourseOwner || enrolled || lesson.freePreview) ? (
                                                                    <Link to={`/course/${slug}/module/${module.id}/lesson/${lesson.id}`} className="min-w-0 flex-1 px-5 py-3 text-sm font-medium text-gray-700">{lesson.title}</Link>
                                                                ) : <div className="block flex-1 cursor-not-allowed p-3">{lesson.title}</div>}
                                                                <p className='flex px-2 h-7 w-10 shrink-0 items-center justify-center rounded-full bg-indigo-300/50 text-sm font-semibold'>
                                                                    {lesson.durationSeconds != null
                                                                        ? timeFormater(lesson.durationSeconds)
                                                                        : "Sem duração"}
                                                                </p>
                                                                {isCourseOwner && <Link to={`/instructor/courses/${course.id}/modules/${module.id}/lessons/${lesson.id}/edit`} aria-label="Editar aula" title="Editar aula" className="mr-2 rounded-md p-2 text-indigo-600 hover:bg-indigo-100"><PencilIcon className="h-4 w-4" /></Link>}
                                                            </div>
                                                        </div>
                                                    ))}
                                                </div>
                                            </div>
                                        )}
                                    </div>
                                </div>
                            ))}
                        </div>
                    </div>

                    {/* Bloco de infos. - lateral direita */}
                    <div className="flex flex-col ml-4 w-2/5 rounded-xl bg-white px-10 py-5">
                        {/* Categorias */}
                        <div className='flex mb-2'>
                            <ul className="mt-[15px] text-lg text-black">
                                {!loadingCourse &&
                                    course?.categories.map((category) =>
                                        <li
                                            key={category.id}
                                            className='py-1 px-2 bg-indigo-200/50 text-lg font-semibold text-indigo-700 rounded-md border border-indigo-300'
                                        >
                                            {category.name}
                                        </li>
                                    )}
                            </ul>
                        </div>
                        <div className="flex items-start justify-between">
                            <h1 className="text-4xl font-bold text-black">
                                {course?.title}
                            </h1>

                            <div className="flex items-center">
                                {isCourseOwner && displayedStatus === 'DRAFT' &&
                                    <button
                                        type="button"
                                        onClick={handlePublish}
                                        disabled={publishing}
                                        className="rounded bg-emerald-600 px-3 py-2 text-sm font-semibold text-white hover:bg-emerald-700 disabled:opacity-50"
                                    >
                                        {publishing ? 'Publicando...' : 'Publicar'}
                                    </button>
                                }
                                {isCourseOwner && course &&
                                    <Link
                                        to={`/instructor/courses/${course.id}/edit`}
                                        aria-label="Editar curso"
                                        title="Editar curso"
                                        className="rounded p-2 text-indigo-700 hover:bg-indigo-50"
                                    >
                                        <PencilIcon className="h-5 w-5" />
                                    </Link>
                                }
                                {isCourseOwner &&
                                    <button
                                        type="button"
                                        onClick={requestDeleteCourse}
                                        disabled={deletingCourse}
                                        aria-label="Excluir curso"
                                        title="Excluir curso"
                                        className=" rounded p-2 text-red-600 hover:bg-red-50 disabled:opacity-50"
                                    >
                                        <TrashIcon className="h-5 w-5" />
                                    </button>
                                }
                            </div>
                        </div>
                        {displayedStatus === 'DRAFT' && (
                            <span className="mt-3 inline-flex w-fit rounded bg-amber-100 px-2 py-1 text-sm font-semibold text-amber-800">
                                Rascunho
                            </span>
                        )}
                        {publishError &&
                            <p className="mt-3 text-sm text-red-600">
                                {publishError}
                            </p>
                        }

                        {/* Avaliações */}
                        <div className='mt-4 flex justify-between max-w-[280px] items-center text-gray-700'>
                            <div className='flex items-center'>
                                <StarIcon className='text-yellow-400 h-5 w-5' />
                                <p className='px-2'>4,9</p>
                                <p>(231 avaliações)</p>
                            </div>
                            <p className='px-5'>|</p>
                            <p>1.236 alunos</p>
                        </div>

                        <div className="line-clamp-4 leading-7 mt-[15px] text-xl text-gray-700">
                            {course?.description}
                        </div>

                        {/* Instrutor */}
                        <div className='mt-10'>
                            <p className='text-2xl font-semibold mb-4'>
                                Instrutor do curso
                            </p>

                            <div className='bg-indigo-50/40 flex rounded-xl border border-gray-200 shadow-sm p-2 m-2 items-center justify-between'>
                                <div className='flex items-center'>
                                    <img src={Thumb} alt="Foto do instrutor" className='rounded-full size-16 m-2' />
                                    <div className='flex flex-col'>
                                        <p className='font-semibold text-lg'>{course?.instructor.fullName}</p>
                                        <p className='text-indigo-700 font-semibold'>{course?.instructor.email}</p>
                                    </div>
                                </div>
                                <ChevronRightIcon className='w-5 h-5' />
                            </div>
                        </div>

                        <div className='flex justify-between py-10'>
                            <div className='flex flex-col m-4 bg-indigo-50/40 rounded-xl border border-gray-200 h-32 w-32 items-center justify-center'>
                                <PlayIcon className='size-9 text-indigo-700' />
                                <p className='font-bold'>{totalLessons}</p>
                                <p className='text-gray-500'>
                                    {totalLessons > 1 ? 'Aulas' : 'Aula'}
                                </p>
                            </div>
                            <div className='flex flex-col m-4 bg-indigo-50/40 rounded-xl border border-gray-200 h-32 w-32 items-center justify-center'>
                                <ClockIcon className='size-9 text-indigo-700' />
                                <p className='font-bold'>{courseDuration}</p>
                                <p className='text-gray-500'>Horas</p>
                            </div>
                            <div className='flex flex-col m-4 bg-indigo-50/40 rounded-xl border border-gray-200 h-32 w-32 items-center justify-center'>
                                <ChartBarIcon
                                    className={`size-9 
                                    ${course?.level === "BEGINNER" ? `text-green-500` : course?.level === "INTERMEDIATE" ? `text-orange-500`: `text-red-500`}`} />
                                <p className='font-bold'>{courseLevelFormarter(course?.level)}</p>
                                <p className='text-gray-500'>Nível</p>
                            </div>
                            <div className='flex flex-col m-4 bg-indigo-50/40 rounded-xl border border-gray-200 h-32 w-32 items-center justify-center'>
                                <ClockIcon className='size-9 text-indigo-700' />
                                <p className='font-bold'>x</p>
                                <p className='text-gray-500'>Horas</p>
                            </div>
                        </div>

                        <button
                            onClick={enrolled ? () => setConfirmOpen(true) : enroll}
                            disabled={loadingEnroll}
                            className={`relative mt-auto flex items-center justify-center rounded p-3 text-2xl text-white disabled:opacity-50 ${enrolled ? 'bg-red-400' : 'bg-blue-400'}`}
                        >
                            {loadingEnroll ? <Loading /> : (enrolled ? 'Cancelar matrícula' : 'Matricule-se')}
                            <ArrowRightIcon className='absolute right-3 h-5 w-5' />
                        </button>
                        {confirmOpen &&
                            dialogCancel
                        }
                        {dialogExclude}
                    </div>
                </div>
            </div>
            <Footbar />
        </main>
    );
}

export default Course;
