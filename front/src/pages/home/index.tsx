import '../../index.css'
import Navbar from '../../components/navbar'
import CardCurso from '../../components/card-curso';
import { Link } from 'react-router-dom';
import useCourses from '../../hooks/useCourses';
import Loading from '../../components/loading';

function Home() {
    const { courses, loading } = useCourses();

    if (loading) {
        return (
            <>
                <Navbar />
                <div className='flex bg-indigo-100 dark:bg-indigo-950 h-screen w-full items-center justify-center gap-8'>
                    <Loading/>
                </div>
            </>
        );
    }

    return (
        <>
            <Navbar />
            <div className='flex bg-indigo-100 dark:bg-indigo-950 h-screen w-full items-center justify-center gap-8'>
                {courses.map(course =>
                    <Link to={`/course/${course.slug}`}>
                        <CardCurso course={course} />
                    </Link>
                )}
            </div>
        </>
    )
}

export default Home;