import '../../index.css'
import type { CourseProgress } from "../../types"
import Image from '../../assets/image.png'
import { CheckCircleIcon } from '@heroicons/react/20/solid';
import { BookOpenIcon } from '@heroicons/react/24/outline';
import { Link } from 'react-router-dom';

type Props = {
    course: CourseProgress;
};

export default function CardProgress({ course }: Readonly<Props>) {
    return (
        <div className="flex flex-col min-h-[180px] max-h-[220px] min-w-[300px] max-w-[380px] overflow-hidden rounded-xl bg-gray-50 shadow-md hover:shadow-lg">

            {/*Infos*/}
            <div className='flex'>
                <div className="relative m-4 rounded-xl h-[90px] w-[90px] shrink-0 overflow-hidden">
                    <img
                        src={Image}
                        alt="Thumbnail do curso"
                        className="h-full w-full object-cover"
                    />
                </div>

                <div className="flex flex-col mt-4">
                    <h2 className="text-lg max-w-[250px] font-bold">
                        {course.title}
                    </h2>
                    <p className="mt-1 max-w-[180px] line-clamp-3 text-sm leading-5 text-gray-600">
                        {course.description}
                    </p>
                </div>
            </div>

            {/* Progresso */}
            <div className='flex m-4'>
                {course.status == "IN_PROGRESS" ?
                    (
                        <div className="w-full">
                            <div className="flex justify-between mb-2">
                                <span className="text-sm text-gray-600">
                                    Progresso
                                </span>
                                <span className="text-sm font-semibold text-blue-700">
                                    {course.progressPercentage}%
                                </span>
                            </div>

                            <div className="w-full h-3 bg-gray-200 rounded-full overflow-hidden">
                                <div
                                    className="h-full bg-blue-600 rounded-full transition-all duration-500"
                                    style={{
                                        width: `${course.progressPercentage}%`
                                    }}
                                />
                            </div>
                        </div>
                    ) : (
                        <div className="flex w-full rounded-md bg-green-400/30 h-[35px] justify-start items-center">
                            <CheckCircleIcon className='text-green-700 size-[25px] m-2' />
                            <p className='text-green-700 text-sm font-semibold'>
                                Curso concluído em sucesso!
                            </p>
                        </div>
                    )}
            </div>

            {/* Total + Botão */}
            <div className='flex mt-0 m-4 justify-between items-center'>
                <div className='flex justify-center items-center'>
                    <BookOpenIcon className='text-gray-600 size-[20px] m-2' />
                    <p className='text-sm text-gray-700'>
                        {course.totalLessons} Aulas
                    </p>
                </div>
                <Link key={course.courseId} to={`/course/${course.slug}`}>
                    <button type='button' className='text-blue-900 bg-blue-700/20 h-[30px] w-[80px] rounded-md transition hover:-translate-y-1 hover:bg-blue-500 hover:text-white'>
                        Ver Curso
                    </button>
                </Link>
            </div>
        </div>
    )
}
