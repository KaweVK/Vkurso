import '../../index.css'
import { Link } from 'react-router-dom';
import { AcademicCapIcon, ArrowLongRightIcon, BookOpenIcon, } from "@heroicons/react/24/outline";
import { useEffect, useState } from 'react';
import progressService from '../../services/progressService';
import type { CourseProgress } from '../../types/courseProgress';
import CardProgress from '../../components/card-progress';

function MyJourney() {
    const [inProgressCourses, setInProgressCourses] = useState<CourseProgress[]>([]);
    const [completedCourses, setCompletedCourses] = useState<CourseProgress[]>([]);

    useEffect(() => {
        const loadProgress = async () => {
            try {
                const data = await progressService.findStudentProgress();

                const inProgress = data.content.filter(
                    course => course.status === "IN_PROGRESS"
                );

                const completed = data.content.filter(
                    course => course.status === "COMPLETED"
                );

                setInProgressCourses(inProgress);
                setCompletedCourses(completed);

            } catch (error) {
                console.error('Erro ao carregar progresso:', error);
            }
        };

        loadProgress()
    }, []);

    return (
        <main className='bg-indigo-100/30'>
            <div className="flex px-[3%] min-h-screen items-start">
                <div className='flex flex-col ml-20 p-4 min-h-[620px] rounded-xl mt-20'>
                    <section id='EmAndamento'>
                        <div className='flex justify-between'>
                            <div>
                                <div className='flex'>
                                    <BookOpenIcon className='text-blue-700 font-semibold h-10 w-10' />
                                    <p className='text-2xl font-semibold ml-4'>
                                        Em andamento
                                    </p>
                                </div>
                                <span className='text-gray-700'>Cursos que você está matriculado atualmente.</span>
                            </div>
                            {inProgressCourses.length > 6 &&
                                <div className='flex'>
                                    <Link to={'/catalog'} className='flex font-semibold items-center hover:text-blue-600 transition delay-150 duration-300 ease-in-out hover:-translate-y-1 hover:scale-105'>
                                        <p>Ver todos</p>
                                        <ArrowLongRightIcon className='size-[20px] m-2 mt-3' />
                                    </Link>
                                </div>
                            }
                        </div>
                        <div>
                            {
                                <div className='grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6 py-8'>
                                    {inProgressCourses.slice(0, 6).map(course =>
                                    (
                                        <CardProgress key={course.courseId} course={course} />
                                    )
                                    )}
                                </div>
                            }
                        </div>
                    </section>

                    <section id='Concluidos'>
                        <div className='flex justify-between'>
                            <div>
                                <div className='flex'>
                                    <AcademicCapIcon className='text-blue-700 font-semibold h-10 w-10' />
                                    <p className='text-2xl font-semibold ml-4'>
                                        Concluídos
                                    </p>
                                </div>
                                <span className='text-gray-700'>Cursos que você concluiu anteriormente.</span>
                            </div>
                            {completedCourses.length > 6 &&
                                <div className='flex'>
                                    <Link to={'/catalog'} className='flex font-semibold items-center hover:text-blue-600 transition delay-150 duration-300 ease-in-out hover:-translate-y-1 hover:scale-105'>
                                        <p>Ver todos</p>
                                        <ArrowLongRightIcon className='size-[20px] m-2 mt-3' />
                                    </Link>
                                </div>
                            }
                        </div>
                        <div>
                            <div className='grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6 py-8'>
                                {completedCourses.slice(0, 6).map(course =>
                                (
                                    <CardProgress key={course.courseId} course={course} />
                                )
                                )}
                            </div>
                        </div>
                    </section>

                </div>
            </div>
        </main>
    )
}

export default MyJourney;