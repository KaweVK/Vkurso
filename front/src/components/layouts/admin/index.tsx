import { LockClosedIcon, PresentationChartLineIcon, RectangleStackIcon, UsersIcon } from '@heroicons/react/24/outline';
import { Outlet } from 'react-router-dom';
import Sidebar from '../../sidebar';

function AdminLayout() {
    const adminItems = [
        {
            label: "Dashboard",
            to: "/admin",
            icon: <PresentationChartLineIcon className='h-10 w-10' />

        },
        {
            label: "Usuários",
            to: "/admin/users",
            icon: <UsersIcon className="h-10 w-10" />,
        },
        {
            label: "Cursos",
            to: "/admin/courses",
            icon: <RectangleStackIcon className="h-10 w-10" />,
        },
        {
            label: "Categorias",
            to: "/admin/categories",
            icon: <LockClosedIcon className="h-10 w-10" />,
        },
    ];

    return (
        <div>
            <div className='flex flex-col bg-blue-950 h-[320px] px-[10%] justify-center rounded-b-[30px]'>
                <p className='font-bold text-5xl lg:text-6xl text-white'>Administração</p>
                <p className='text-2xl lg:text-3xl text-white mt-4'>Administre todo o sistema Vkurso.</p>
            </div>
            <div className="flex flex-col lg:flex-row min-h-screen px-10">
                <Sidebar
                    title='Painel Admin'
                    description='Administre aqui os principais recursos do Vkurso'
                    items={adminItems}
                />

                <main className="flex-1">
                    <Outlet />
                </main>
            </div>
        </div>
    );
}

export default AdminLayout;