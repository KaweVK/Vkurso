import "../../index.css";
import { Link, useParams } from "react-router-dom";
import { Menu, MenuButton, MenuItem, MenuItems } from "@headlessui/react";
import { ChevronDownIcon } from "@heroicons/react/20/solid";
import { ArrowLeftIcon } from "@heroicons/react/24/outline";
import { useCourse } from "../../hooks/useCourses";
import { useEffect, useState } from "react";
import api from '../../services/api';

type VideoResponse = {
    videoUrl: string;
};

function Lesson() {
    const { slug, moduleId, lessonId } = useParams();
    const { course, loading } = useCourse(slug);
    const [url, setUrl] = useState("")

    useEffect(() => {
        async function getVideoUrl() {
            try {
                if (!course?.id) return;
                const res = await api.get<VideoResponse>(`/courses/${course.id}/modules/${moduleId}/lessons/${lessonId}/video-url`)
                setUrl(res.data.videoUrl)
            } catch (err) {
                console.log(err)
                setUrl('')
            }
        }

        getVideoUrl();
    }, [course?.id, moduleId, lessonId]);

    if (loading) return <p>Carregando...</p>;

    const modules = course?.modules ?? [];

    const lessons = modules.flatMap((module) =>
        module.lessons.map((lesson) => ({
            ...lesson,
            moduleTitle: module.title,
        }))
    );

    const currentIndex = lessons.findIndex(
        (lesson) => lesson.id === Number(lessonId)
    );

    const previousLesson =
        currentIndex > 0 ? lessons[currentIndex - 1] : null;

    const nextLesson =
        currentIndex < lessons.length - 1
            ? lessons[currentIndex + 1]
            : null;

    const currentLesson = lessons[currentIndex];

    return (
        <div className="min-h-screen bg-indigo-100 dark:bg-indigo-950 p-8">
            <Link
                to={`/course/${slug}`}
                className="flex items-center gap-2 mb-5 text-white"
            >
                <ArrowLeftIcon className="h-6 w-6" />
                Voltar para o curso
            </Link>

            <div className="flex gap-6">
                <div className="w-3/4">
                    <div className="aspect-video rounded-lg bg-black flex items-center justify-center text-white text-2xl">
                        <iframe
                            src={url}
                            className="aspect-video w-full rounded-lg"
                            allowFullScreen
                        />
                    </div>
                    <div className="flex justify-between mt-5">
                        {previousLesson ? (
                            <Link
                                to={`/courses/${slug}/modules/${moduleId}/lessons/${previousLesson.id}`}
                                className="bg-indigo-600 px-5 py-3 rounded text-white"
                            >
                                ← {previousLesson.title}
                            </Link>
                        ) : (
                            <div />
                        )}
                        {nextLesson && (
                            <Link
                                to={`/courses/${slug}/module/${moduleId}/lesson/${nextLesson.id}`}
                                className="bg-indigo-600 px-5 py-3 rounded text-white"
                            >
                                {nextLesson.title} →
                            </Link>
                        )}
                    </div>
                </div>
                <div className="w-1/4 bg-indigo-100 rounded-lg p-4 overflow-y-auto max-h-[80vh]">
                    {modules.map((module) => (
                        <Menu
                            as="div"
                            key={module.id}
                            className="mb-2"
                        >
                            <MenuButton className="flex w-full justify-between bg-indigo-600 text-white rounded p-3">
                                {module.title}
                                <ChevronDownIcon className="w-5 h-5" />
                            </MenuButton>
                            <MenuItems className="bg-gray-100">
                                {module.lessons.map((lesson) => (
                                    <MenuItem key={lesson.id}>
                                        <Link
                                            to={`/courses/${slug}/module/${moduleId}/lesson/${lesson.id}`}
                                            className={`block p-3 ${lesson.id === currentLesson.id
                                                ? "bg-indigo-300"
                                                : ""
                                                }`}
                                        >
                                            {lesson.title}
                                        </Link>
                                    </MenuItem>
                                ))}
                            </MenuItems>
                        </Menu>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default Lesson;