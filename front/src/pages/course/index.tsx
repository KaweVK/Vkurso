import '../../index.css'
import Navbar from '../../components/navbar'
import { Menu, MenuButton, MenuItem, MenuItems } from '@headlessui/react'
import { ChevronDownIcon } from '@heroicons/react/20/solid'
import { useCourse } from '../../hooks/useCourses';
import Loading from '../../components/loading';
import { useParams } from 'react-router-dom';
import Thumb from '../../assets/image.png'
import { useCategories } from '../../hooks/useCategories';
import { useEnrollment } from '../../hooks/useEnrollment';
import { Link } from 'react-router-dom';
import { useState } from 'react';

function Course() {
    const { slug } = useParams<{ slug: string }>();
    const [confirmOpen, setConfirmOpen] = useState(false);
    const { course, loading: loadingCourse } = useCourse(slug);
    const { categories, loading: loadingCategories } = useCategories()
    const { enrolled, loading: loadingEnroll, enroll, cancel } = useEnrollment(course?.id);

    const courseCategories = categories.filter((c) =>
        course?.categoryIds?.includes(c.id)
    );

    if (loadingCourse) {
        return (
            <>
                <Navbar />
                <div className='flex bg-indigo-100 dark:bg-indigo-950 h-screen w-full items-center justify-center gap-8'>
                    <Loading />
                </div>
            </>
        );
    }

    return (
        <>
            <Navbar />
            <div className="min-h-screen bg-indigo-100 dark:bg-indigo-950 px-10 py-8">
                <div className="flex min-h-screen bg-indigo-950 dark:bg-indigo-200 px-10 py-8 rounded-xl">
                    <div className='w-3/5'>
                        <img src={Thumb} alt="Thumnail" className='ml-[6%] w-[90%] rounded-lg' />

                        {course?.modules?.map(module =>
                            <Menu
                                as="div"
                                key={module.id}
                                className="mb-2 ml-[6%] mt-[10px] w-[90%]"
                            >
                                <MenuButton className="flex w-full justify-between bg-indigo-600 text-white rounded p-3">
                                    {module.title}
                                    <ChevronDownIcon className="w-5 h-5" />
                                </MenuButton>
                                <MenuItems className="bg-gray-100">
                                    {module.lessons.map((lesson) => (
                                        <MenuItem key={lesson.id}>
                                            {enrolled || lesson.freePreview ? (
                                                <Link
                                                    to={`/course/${slug}/module/${module.id}/lesson/${lesson.id}`}
                                                    className="block p-3 bg-indigo-300"
                                                >
                                                    {lesson.title}
                                                </Link>
                                            ) : (
                                                <div className="block p-3 bg-indigo-300 cursor-not-allowed">
                                                    {lesson.title}
                                                </div>
                                            )}
                                        </MenuItem>
                                    ))}
                                </MenuItems>
                            </Menu>
                        )}
                    </div>

                    <div className="bg-white dark:bg-white px-10 py-8 ml-[3%] w-2/5 rounded-xl">
                        <h1 className="text-5xl font-bold ml-[6%] text-white dark:text-black">{course?.title}</h1>
                        <div className="text-2xl text-white ml-[6%] mt-[15px] dark:text-black">
                            {!loadingCategories && courseCategories.map((category) => category.name).join(", ")}
                        </div>
                        <div className="text-2xl text-white ml-[6%] mt-[15px] dark:text-black">
                            {course?.description}
                        </div>

                        <button
                            onClick={enrolled ? () => setConfirmOpen(true) : enroll}
                            disabled={loadingEnroll}
                            className={`text-2  xl text-white absolute bottom-2 rounded p-2 disabled:opacity-50 ${enrolled ? 'bg-red-400' : 'bg-blue-400'
                                }`}
                        >
                            {loadingEnroll ? <Loading /> : enrolled ? 'Cancelar matrícula' : 'Matricule-se'}
                        </button>
                        {confirmOpen && (
                            <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
                                <div className="bg-white rounded p-6 max-w-sm">
                                    <p className="mb-4">Tem certeza que deseja cancelar sua matrícula?</p>
                                    <div className="flex gap-2 justify-end">
                                        <button
                                            onClick={() => setConfirmOpen(false)}
                                            className="px-4 py-2 rounded border"
                                        >
                                            Voltar
                                        </button>
                                        <button
                                            onClick={() => { setConfirmOpen(false); cancel(); }}
                                            className="px-4 py-2 rounded bg-red-400 text-white"
                                        >
                                            Sim, cancelar
                                        </button>
                                    </div>
                                </div>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </>
    )
}

export default Course;