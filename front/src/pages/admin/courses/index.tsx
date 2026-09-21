import { Link } from "react-router-dom";
import type { Course } from "../../../types";
import { useEffect, useState } from "react";
import CardCurso from "../../../components/card-curso";
import courseService from "../../../services/courseService";

export default function Courses() {
    const [courses, setCourses] = useState<Course[]>([])

    useEffect(() => {
        const loadUsers = async () => {
            try {
                const data = await courseService.findAll();
                setCourses(data)
            } catch (error) {
                console.error('Erro ao carregar progresso:', error);
            }
        }

        loadUsers();
    }, [])

    return (
        <div className="px-10 py-20">
            <div className="flex items-center justify-between">
                <div>
                    <h1 className="text-2xl font-bold">
                        Cursos
                    </h1>

                    <p className="mt-1 text-gray-600">
                        Gerencie os cursos da plataforma.
                    </p>
                </div>
            </div>

            <div className="flex w-full flex-wrap justify-start gap-6 py-8">
                {courses.map(course =>
                    <Link key={course.id} to={`/course/${course.slug}`}>
                        <CardCurso course={course} />
                    </Link>
                )}
            </div>
        </div>
    );
}