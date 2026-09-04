import '../../index.css'
import Navbar from '../../components/navbar'
import CardCurso from '../../components/card-curso';
import { Link } from 'react-router-dom';
import useCourses from '../../hooks/useCourses';
import Loading from '../../components/loading';
import Footbar from '../../components/footbar';
import { MagnifyingGlassIcon } from '@heroicons/react/24/outline';
import { useEffect, useState } from 'react';
import categoryService from '../../services/categoryService';
import type { Category } from '../../types/category';

function Catalog() {
    const [search, setSearch] = useState('');
    const [categories, setCategories] = useState<Category[]>([]);
    const [selectedCategory, setSelectedCategory] = useState('');
    const { courses, loading } = useCourses(search, selectedCategory ? Number(selectedCategory) : undefined);


    useEffect(() => {
        const loadCategories = async () => {
            try {
                const data = await categoryService.findAll();
                setCategories(data);
            } catch (error) {
                console.error('Erro ao carregar categorias:', error);
            }
        };

        loadCategories();
    }, []);

    return (
        <div>
            <Navbar />
            <div className='flex flex-col bg-blue-950 h-[320px] px-[10%] justify-center rounded-b-[30px]'>
                <p className='font-bold text-6xl text-white'>Catálogo de cursos</p>
                <p className='text-3xl text-white mt-4'>Escolha o curso ideal para o seu momento.</p>
            </div>
            <div className="flex px-[7%] mt-20 justify-between items-center">
                <div className="relative">
                    <MagnifyingGlassIcon className="absolute left-3 top-1/2 h-5 w-5 -translate-y-1/2 text-black-700" />

                    <input
                        type="text"
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                        className="h-[40px] w-[250px] rounded-xl border border-gray-200 bg-white p-2 pl-10 shadow-md outline-none focus:border-indigo-500"
                        placeholder="Buscar cursos..."
                    />
                </div>
                <div>
                    <select
                        value={selectedCategory}
                        onChange={(e) => setSelectedCategory(e.target.value)}
                        className="h-[40px] w-[200px] rounded-xl border border-gray-200 bg-white px-3 shadow-md outline-none focus:border-indigo-500"
                    >
                        <option value="">
                            Todas as categorias
                        </option>

                        {categories.map((category) => (
                            <option key={category.id} value={category.id}>
                                {category.name}
                            </option>
                        ))}
                    </select>
                </div>
            </div>
            <div className='flex px-[7%] mt-10 justify-between items-center'>
                <p className='font-bold text-3xl text-black'>{courses.length} cursos encontrados</p>
            </div>
            {loading &&
                <div className='flex w-full items-center justify-center py-20'>
                    <Loading />
                </div>
            } : {
                <div className='flex w-full flex-wrap justify-center gap-6 px-8 py-8'>
                    {courses.map(course =>
                        course.status === 'PUBLISHED' && (
                            <Link key={course.id} to={`/course/${course.slug}`}>
                                <CardCurso course={course} />
                            </Link>
                        )
                    )}
                </div>
            }
            <Footbar />
        </div>
    )
}

export default Catalog;