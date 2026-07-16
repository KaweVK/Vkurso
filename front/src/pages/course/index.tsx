import '../../index.css'
import Navbar from '../../components/navbar'
import { useCourse } from '../../hooks/useCourses';
import Loading from '../../components/loading';
import { useParams } from 'react-router-dom';

function Course() {
    const { slug } = useParams<{ slug: string }>();
    const { course, loading } = useCourse(slug);

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
            <div className="min-h-screen bg-indigo-100 dark:bg-indigo-950 px-10 py-8">
                <h1 className="text-5xl font-bold text-white">{course?.title}</h1>

                <div className="mt-10 text-white">
                    {course?.description}
                </div>
            </div>
        </>
    )
}

export default Course;