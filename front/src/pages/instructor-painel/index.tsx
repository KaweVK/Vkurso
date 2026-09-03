import { ArchiveBoxIcon, LockClosedIcon, PlusIcon, PresentationChartLineIcon, RectangleStackIcon } from "@heroicons/react/24/outline";
import Footbar from "../../components/footbar";
import Navbar from "../../components/navbar";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useCoursesByInstructor } from "../../hooks/useCourses";
import Loading from "../../components/loading";
import CardCurso from "../../components/card-curso";

function InstructorPainel() {
    const { publishedCourses: published, draftCourses: drafts, archivedCourses: archiveds, loading } = useCoursesByInstructor();
    const [activeSection, setActiveSection] = useState(
        window.location.hash.replace("#", "") || ""
    );

    useEffect(() => {
        const handleHashChange = () => {
            const hash = window.location.hash.replace("#", "");

            if (hash) {
                setActiveSection(hash);
            } else {
                setActiveSection("")
            }
        };

        window.addEventListener("hashchange", handleHashChange);

        return () => {
            window.removeEventListener("hashchange", handleHashChange);
        };
    }, []);

    return (
        <main className='bg-indigo-100/30'>
            <Navbar />

            {/* Painel do topo */}
            <div className='flex flex-col bg-blue-950 h-[300px] px-[10%] justify-center rounded-b-[30px]'>
                <p className='font-bold text-6xl text-white'>Painel do Instrutor</p>
                <p className='text-3xl text-white mt-4'>Administre aqui seus cursos.</p>
                <Link
                    to={'/instructor/courses/new'}
                    className='flex justify-center items-center mt-5 bg-blue-500 w-[150px] h-10 rounded-xl hover:bg-indigo-700'
                >
                    <p className='text-white font-semibold'>Criar curso</p>
                    <PlusIcon className='text-white font-semibold mt-1 ml-2 h-5 w-5' />
                </Link>
            </div>

            {/* Painel lateral */}
            <div className="flex px-[3%] min-h-screen items-start">
                <div className='flex flex-col w-[300px] mt-20 h-[620px] rounded-xl shadow-md bg-blue-400/10 p-8'>
                    <div className='flex flex-col'>
                        <p className='text-2xl font-semibold'>
                            Meu painel
                        </p>
                        <p className='text-md mt-4 max-w-[220px]'>
                            Acompanhe, veja quantas pessoas assistem e crie novos cursos a qualquer momento.
                        </p>
                    </div>
                    <div className='flex flex-col mt-20'>
                        <a href="#Metrics" className={`flex mb-5 p-2 rounded-md items-center hover:text-blue-900 hover:bg-blue-700/20 ${activeSection === "Metrics" ? 'text-blue-900 bg-blue-700/20' : 'text-gray-500'}`}>
                            <PresentationChartLineIcon className={`font-semibold h-10 w-10 `} />
                            <p className={`m-2 text-gray-700`}>
                                Métricas
                            </p>
                        </a>
                        <a href="#publisheds" className={`flex mb-5 p-2 rounded-md items-center hover:text-blue-900 hover:bg-blue-700/20 ${activeSection === "MyCourses" ? 'text-blue-900 bg-blue-700/20' : 'text-gray-500'}`}>
                            <RectangleStackIcon className='font-semibold h-10 w-10' />
                            <p className='m-2 text-gray-700'>
                                Meus cursos
                            </p>
                        </a>
                        <a href="#drafts" className={`flex mb-5 p-2 rounded-md items-center hover:text-blue-900 hover:bg-blue-700/20 ${activeSection === "Drafts" ? 'text-blue-900 bg-blue-700/20' : 'text-gray-500'}`}>
                            <LockClosedIcon className='font-semibold h-10 w-10' />
                            <p className='m-2 text-gray-700'>
                                Rascunhos
                            </p>
                        </a>
                        <a href="#archiveds" className={`flex mb-5 p-2 rounded-md items-center hover:text-blue-900 hover:bg-blue-700/20 ${activeSection === "Archiveds" ? 'text-blue-900 bg-blue-700/20' : 'text-gray-500'}`}>
                            <ArchiveBoxIcon className='font-semibold h-10 w-10' />
                            <p className='m-2 text-gray-700'>
                                Arquivados
                            </p>
                        </a>
                    </div>
                </div>

                <div className="flex flex-col">

                    {/* Seção de Cursos Publicados */}
                    <section id="publisheds" className="flex flex-col ml-20 mt-20">
                        <div className='flex justify-between items-center'>
                            <p className='font-bold text-3xl text-black'>Publicados</p>
                        </div>
                        {loading ?
                            (
                                <div className='flex w-full items-center justify-center py-20'>
                                    <Loading />
                                </div>
                            ) : published.length == 0 ?
                                (
                                    <div className="flex w-full items-center justify-center py-10">
                                        <p className="text-gray-500">Nenhum curso encontrado.</p>
                                    </div>
                                ) : (
                                    <div className='flex w-full flex-wrap justify-center gap-6 py-8'>
                                        {published.map(course =>
                                            <Link key={course.id} to={`/course/${course.slug}`}>
                                                <CardCurso course={course} />
                                            </Link>
                                        )
                                        }
                                    </div>
                                )
                        }
                    </section>

                    {/* Seção de Cursos Rascunhos */}
                    <section id="drafts" className="flex flex-col ml-20 mt-20">
                        <div className='flex justify-between items-center'>
                            <p className='font-bold text-3xl text-black'>Rascunhos</p>
                        </div>
                        {loading ?
                            (
                                <div className='flex w-full items-center justify-center py-20'>
                                    <Loading />
                                </div>
                            ) : drafts.length == 0 ?
                                (
                                    <div className="flex w-full items-center justify-center py-10">
                                        <p className="text-gray-500">Nenhum rascunho encontrado.</p>
                                    </div>
                                ) : (
                                    <div className='flex w-full flex-wrap justify-center gap-6 py-8'>
                                        {drafts.map(course =>
                                            <Link key={course.id} to={`/course/${course.slug}`}>
                                                <CardCurso course={course} />
                                            </Link>
                                        )
                                        }
                                    </div>
                                )
                        }
                    </section>

                    {/* Seção de Cursos Arquivados */}
                    <section id="archiveds" className="flex flex-col ml-20 mt-20">
                        <div className='flex justify-between items-center'>
                            <p className='font-bold text-3xl text-black'>Arquivados</p>
                        </div>
                        {loading ?
                            (
                                <div className='flex w-full items-center justify-center py-20'>
                                    <Loading />
                                </div>
                            ) : archiveds.length == 0 ?
                                (
                                    <div className="flex w-full items-center justify-center py-10">
                                        <p className="text-gray-500">Nenhum rascunho encontrado.</p>
                                    </div>
                                ) : (
                                    <div className='flex w-full flex-wrap justify-center gap-6 py-8'>
                                        {archiveds.map(course =>
                                            <Link key={course.id} to={`/course/${course.slug}`}>
                                                <CardCurso course={course} />
                                            </Link>
                                        )
                                        }
                                    </div>
                                )
                        }
                    </section>
                </div>
            </div>
            <Footbar />
        </main>
    )
}

export default InstructorPainel;