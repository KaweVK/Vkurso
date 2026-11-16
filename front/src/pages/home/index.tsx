import '../../index.css'
import CardCurso from '../../components/card-curso';
import { Link } from 'react-router-dom';
import { useFeaturedCourses } from '../../hooks/useCourses';
import Loading from '../../components/loading';
import { AcademicCapIcon, ArrowLongRightIcon, CodeBracketIcon, ComputerDesktopIcon, RocketLaunchIcon } from "@heroicons/react/24/outline";

function Home() {
    const { course, loading } = useFeaturedCourses();

    return (
        <main className='bg-indigo-100/30 mx-auto'>
            <div className='flex flex-col bg-blue-950 h-[640px] lg:h-[520px] px-[10%] justify-center rounded-b-[30px]'>
                <p className='font-bold text-6xl text-white'>Aprenda no seu ritmo.</p>
                <p className='font-bold text-6xl text-white mt-4'>Conquiste <span className="text-blue-600">novos objetivos.</span></p>
                <p className='font-semibold text-2xl text-white mt-4 max-w-[350px]'>Cursos práticos, objetivos e 100% online para você evoluir na carreira e nos seus projetos pessoais.</p>
                <div className='flex'>
                    <Link
                        to={'/catalog'}
                        className='flex justify-center items-center mt-5 bg-blue-500 w-[150px] h-10 rounded-xl hover:bg-indigo-700'
                    >
                        <p className='text-white font-semibold'>Explorar Catálogo</p>
                    </Link>
                    <Link
                        to={'/about'}
                        className='flex justify-center items-center mt-5 w-[150px] h-10 transition delay-150 duration-300 ease-in-out hover:-translate-y-1 hover:scale-105'
                    >
                        <p className='text-white font-semibold '>Sobre</p> <ArrowLongRightIcon className="mt-1 text-white font-semibold h-5 w-8" />
                    </Link>
                </div>
            </div>
            <div className='grid grid-cols-2 md:grid-cols-4 bg-white h-full w-[75%] lg:w-[50%] justify-center rounded-3xl mt-[-3%] mx-auto shadow-lg'>
                <div className='flex flex-col items-center justify-center text-center col-span-1 bg-white rounded-3xl m-[10px] shadow-md p-2'>
                    <RocketLaunchIcon className='text-indigo-700 font-semibold h-14 w-14' />
                    <p className='font-semibold max-w-[120px]'>Atualidade do mercado.</p>
                </div>
                <div className='flex flex-col items-center justify-center text-center col-span-1 bg-white rounded-3xl mt-[10px] mb-[10px] shadow-md p-2'>
                    <ComputerDesktopIcon className='text-indigo-700 font-semibold h-14 w-14' />
                    <p className='font-semibold max-w-[120px]'>Aprenda onde quiser</p>
                </div>
                <div className='flex flex-col items-center justify-center text-center col-span-1 bg-white rounded-3xl mt-[10px] mb-[10px] ml-[10px] shadow-md p-2'>
                    <AcademicCapIcon className='text-indigo-700 font-semibold h-14 w-14' />
                    <p className='font-semibold max-w-[120px]'>Certifique seu conhecimento</p>
                </div>
                <div className='flex flex-col items-center justify-center text-center col-span-1 bg-white rounded-3xl m-[10px] shadow-md p-2'>
                    <CodeBracketIcon className='text-indigo-700 font-semibold h-14 w-14' />
                    <p className='font-semibold max-w-[120px]'>Programe como Sênior</p>
                </div>
            </div>
            <div className='flex px-[7%] mt-20 justify-between items-center'>
                <p className='font-bold text-5xl text-black'>Cursos em destaque</p>
                <Link
                    to={'/catalog'}
                    className='flex justify-center items-center w-[150px] h-10 transition delay-150 duration-300 ease-in-out hover:-translate-y-1 hover:scale-105'
                >
                    <p className='text-blue-700 font-bold '>Ver todos</p> <ArrowLongRightIcon className="mt-1 text-blue-700 font-bold h-5 w-8" />
                </Link>
            </div>
            {loading ?
                <div className='flex w-full items-center justify-center py-20'>
                    <Loading />
                </div>
             : 
                <div className='flex w-full flex-wrap justify-center gap-6 px-8 py-8'>
                    {course?.map(c =>
                        c.status === 'PUBLISHED' && (
                            <Link key={c.id} to={`/course/${c.slug}`}>
                                <CardCurso course={c} />
                            </Link>
                        )
                    )}
                </div>
            }
        </main>
    )
}

export default Home;