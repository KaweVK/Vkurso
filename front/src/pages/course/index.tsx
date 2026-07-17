import '../../index.css'
import Navbar from '../../components/navbar'
import { Menu, MenuButton, MenuItem, MenuItems } from '@headlessui/react'
import { ChevronDownIcon } from '@heroicons/react/20/solid'
import { useCourse } from '../../hooks/useCourses';
import Loading from '../../components/loading';
import { useParams } from 'react-router-dom';
import Thumb from '../../assets/image.png'
import { useCategories } from '../../hooks/useCategories';
import { Link } from 'react-router-dom';

function Course() {
    const { slug } = useParams<{ slug: string }>();
    const { course, loading: loadingCourse } = useCourse(slug);
    const { categories, loading: loadingCategories } = useCategories()

    const courseCategories = categories.filter((c) =>
        course?.categoryIds?.includes(c.id)
    );

    if (loadingCourse) {
        return (
            <>
                <Navbar />
                <div className='flex bg-indigo-100 dark:bg-indigo-950 h-screen w-full items-center justify-center gap-8'>
                    <Loading />
                </div>
            </>
        );
    }

    return (
        <>
            <Navbar />
            <div className="min-h-screen bg-indigo-100 dark:bg-indigo-950 px-10 py-8">
                <div className="flex min-h-screen bg-indigo-950 dark:bg-indigo-200 px-10 py-8 rounded-xl">
                    <div className='w-3/5'>
                        <img src={Thumb} alt="Thumnail" className='ml-[6%] w-[90%] rounded-lg' />

                        {course?.modules?.map(module =>
                            <Menu as="div" className="ml-[6%] mt-[10px] w-[90%]">
                                <MenuButton className="inline-flex w-full justify-start rounded-sm bg-indigo-600/30 px-3 py-2 text-sm font-semibold text-white inset-ring-1 inset-ring-white/5 hover:bg-white/20">
                                    <p>{module.title}</p>
                                    <ChevronDownIcon aria-hidden="true" className="right-0 size-5 text-white/80" />
                                </MenuButton>
                                <MenuItems
                                    transition
                                    className="divide-y divide-white/10 rounded-md bg-gray-800 outline-1 -outline-offset-1 outline-white/10 transition data-closed:scale-95 data-closed:transform data-closed:opacity-0 data-enter:duration-100 data-enter:ease-out data-leave:duration-75 data-leave:ease-in"
                                >
                                    {module?.lessons?.map(lesson =>
                                        <div className="py-1">
                                            <MenuItem>
                                                <Link to={`/course/${slug}/module/${module.id}/lesson/${lesson.id}`} className="block px-4 py-2 text-sm text-gray-300 data-focus:bg-white/5 data-focus:text-white data-focus:outline-hidden">{lesson.title}</Link>
                                            </MenuItem>
                                        </div>
                                    )}
                                </MenuItems>
                            </Menu>
                        )}
                    </div>

                    <div className="bg-white dark:bg-white px-10 py-8 ml-[3%] w-2/5 rounded-xl">
                        <h1 className="text-5xl font-bold ml-[6%] text-white dark:text-black">{course?.title}</h1>
                        <div className="text-2xl text-white ml-[6%] mt-[15px] dark:text-black">
                            {!loadingCategories && courseCategories.map((category) => category.name).join(", ")}
                        </div>
                        <div className="text-2xl text-white ml-[6%] mt-[15px] dark:text-black rounded-xl border-[2px]">
                            {course?.modules?.length} Módulos
                        </div>
                        <div className="text-2xl text-white ml-[6%] mt-[15px] dark:text-black">
                            {course?.description}
                        </div>
                    </div>
                </div>
            </div>
        </>
    )
}

export default Course;