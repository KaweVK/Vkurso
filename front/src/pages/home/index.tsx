import '../../index.css'
import Navbar from '../../components/navbar'
import type { Course } from '../../types/course'
import CardCurso from '../../components/card-curso';

const course: Course = {
  id: 1,
  title: "Java Spring Boot",
  description: "Curso completo",
  slug: "java-spring-boot",
  price: 99.9,
  level: "BEGINNER",
  categoryIds: [1, 2],
  status: "DRAFT",
  instructorId: 10,
  modules: [],
};

function Home() {
    return (
        <>
            <Navbar/>
            <div className='flex bg-indigo-100 dark:bg-indigo-950 h-screen w-full items-center justify-center'>
                <CardCurso course={course}/>
            </div>
        </>
    
    )
}

export default Home;