import '../../index.css'
import Navbar from '../../components/navbar'
import Footbar from '../../components/footbar'
import CardCurso from '../../components/card-curso';
import { Link } from 'react-router-dom';
import useCourses from '../../hooks/useCourses';
import Loading from '../../components/loading';
import { AcademicCapIcon, ArrowLongRightIcon, CodeBracketIcon, ComputerDesktopIcon, RocketLaunchIcon } from "@heroicons/react/24/outline";

function Home() {
    const { courses, loading } = useCourses();

    return (
        <body className='bg-indigo-100/30'>
            <Navbar />
            <div className='flex flex-col bg-blue-950 h-[520px] px-[10%] justify-center rounded-b-[30px]'>
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
            <div className='flex grid grid-cols-4 bg-white h-[200px] w-[75%] justify-center rounded-[30px] mt-[-40px] mx-auto shadow-lg'>
                <div className='flex flex-col items-center justify-center col-span-1 bg-white rounded-[30px] m-[10px] shadow-md'>
                    <RocketLaunchIcon className='text-indigo-700 font-semibold h-20 w-20' />
                    <p className='font-semibold'>Tecnologias emergentes</p>
                    <p className='font-semibold'>Atualidade do mercado de trabalho.</p>
                </div>
                <div className='flex flex-col items-center justify-center col-span-1 bg-white rounded-[30px] mt-[10px] mb-[10px] shadow-md'>
                    <ComputerDesktopIcon className='text-indigo-700 font-semibold h-20 w-20' />
                    <p className='font-semibold'>Aprenda onde quiser</p>
                    <p className='font-semibold'>Aulas 100% online</p>
                </div>
                <div className='flex flex-col items-center justify-center col-span-1 bg-white rounded-[30px] mt-[10px] mb-[10px] ml-[10px] shadow-md'>
                    <AcademicCapIcon className='text-indigo-700 font-semibold h-20 w-20' />
                    <p className='font-semibold'>Certifique seu conhecimento</p>
                    <p className='font-semibold'>Aulas práticas e teóricas</p>
                </div>
                <div className='flex flex-col items-center justify-center col-span-1 bg-white rounded-[30px] m-[10px] shadow-md'>
                    <CodeBracketIcon className='text-indigo-700 font-semibold h-20 w-20' />
                    <p className='font-semibold'>Programe como Sênior</p>
                    <p className='font-semibold'>Aprenda do básico ao expert</p>
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
            {loading &&
                <div className='flex w-full items-center justify-center py-20'>
                    <Loading />
                </div>
            } : {
                <div className='flex w-full flex-wrap justify-center gap-6 px-8 py-8'>
                    {courses.map(course =>
                        <Link key={course.id} to={`/course/${course.slug}`}>
                            <CardCurso course={course} />
                        </Link>
                    )}
                </div>
            }
            <Footbar/>
        </body>
    )
}

export default Home;