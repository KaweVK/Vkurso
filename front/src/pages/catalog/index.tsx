import '../../index.css'
import Navbar from '../../components/navbar'
import CardCurso from '../../components/card-curso';
import { Link } from 'react-router-dom';
import useCourses from '../../hooks/useCourses';
import Loading from '../../components/loading';
import Footbar from '../../components/footbar';

function Catalog() {
    const { courses, loading } = useCourses();

    return (
        <body>
            <Navbar />
            <div className='flex flex-col bg-blue-950 h-[320px] px-[10%] justify-center'>
                <p className='font-bold text-6xl text-white'>Catálogo de cursos</p>
                <p className='text-3xl text-white mt-4'>Escolha o curso ideal para o seu momento.</p>
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

export default Catalog;