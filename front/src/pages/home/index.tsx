import '../../index.css'
import Navbar from '../../components/navbar'
import CardCurso from '../../components/card-curso';
import { Link } from 'react-router-dom';
import useCourses from '../../hooks/useCourse';


function Home() {
    const { courses, loading } = useCourses();

    if (loading) {
        return (
            <>
                <Navbar />
                <div className='flex bg-indigo-100 dark:bg-indigo-950 h-screen w-full items-center justify-center gap-8'>
                    <svg
                        xmlns="http://www.w3.org/2000/svg"
                        className="w-6 h-6 animate-spin"
                        viewBox="0 0 24 24"
                        fill="none"
                    >
                        <circle
                            cx="12"
                            cy="12"
                            r="10"
                            color='white'
                            stroke="currentColor"
                            strokeWidth="3"
                            strokeLinecap="round"
                            strokeDasharray="60"
                            strokeDashoffset="20"
                        />
                    </svg>
                </div>
            </>
        );
    }

    return (
        <>
            <Navbar />
            <div className='flex bg-indigo-100 dark:bg-indigo-950 h-screen w-full items-center justify-center gap-8'>
                {courses.map(course =>
                    <Link to={`/course/${course.id}`}>
                        <CardCurso course={course} />
                    </Link>
                )}
            </div>
        </>
    )
}

export default Home;