import '../../index.css'
import { Link, useNavigate } from 'react-router-dom'
import Logo from '../../assets/logo.webp'
import { useAuth } from "../../hooks/useAuth";
import { ArrowRightEndOnRectangleIcon, Bars3Icon, ChevronDownIcon, UserIcon } from '@heroicons/react/24/outline';
import { useState } from 'react';

export default function Navbar() {
    const navigate = useNavigate()
    const { user, logout } = useAuth();

    const [isMenuOpen, setIsMenuOpen] = useState(false);

    function handleLogout() {
        logout();
        navigate('/')
        setIsMenuOpen(false);
    }

    return (
        <div className="flex bg-white dark:bg-blue-900 w-full h-14 justify-center font-mono">
            <div className='flex justify-between w-full px-5'>
                <div className='flex items-center'>
                    <img src={Logo} alt="logo" className='h-10 w-auto' />
                    <p className='p-2 text-blue-200 hidden sm:block'>Vkurso</p>
                </div>
                <div className='flex flex-1 justify-center space-x-4 hidden md:flex'>
                    <Link to='/' className='self-center p-2 dark:text-blue-200 hover:underline'>Início</Link>
                    <Link to='/catalog' className='self-center p-2 dark:text-blue-200 hover:underline'>Catálogo</Link>
                    <Link to='/journey' className='self-center p-2 dark:text-blue-200 hover:underline'>Meu aprendizado</Link>
                    {user?.role === 'INSTRUCTOR' && (
                        <Link to='/painel' className='self-center p-2 dark:text-blue-200 hover:underline'>
                            Painel do instrutor
                        </Link>
                    )}
                    {user?.role === 'ADMIN' && (
                        <Link to='/admin' className='self-center p-2 dark:text-blue-200 hover:underline'>
                            Painel do administrador
                        </Link>
                    )}
                </div>

                {/* Usuário */}
                {user ? (
                    <div className="relative self-center">
                        {/* Botão */}
                        <button
                            onClick={() => setIsMenuOpen(!isMenuOpen)}
                            className='flex items-center bg-blue-400 dark:bg-white rounded-full m-[9px] p-2 hover:bg-blue-400 transition text-black hover:text-white'
                        >
                            <p className='hidden lg:flex'>{user.fullName.split(" ")[0]}</p>
                            
                            <UserIcon className="h-5 w-5 ml-2 hidden lg:flex" />
                            <Bars3Icon className="h-5 w-5 ml-2 lg:hidden" />
                            <ChevronDownIcon
                                className={`h-4 w-4 ml-1 transition-transform ${isMenuOpen ? 'rotate-180' : ''}`}
                            />
                        </button>

                        {/* Dropdown */}
                        {isMenuOpen && (
                            <div className="absolute right-0 top-full w-40 bg-indigo-50 rounded-md shadow-lg overflow-hidden z-50">
                                <Link to='/me' onClick={() => setIsMenuOpen(false)} className="flex items-center gap-2 px-4 py-3 hover:bg-gray-200"
                                >
                                    <UserIcon className="h-5 w-5" />
                                    Perfil
                                </Link>
                                <Link to='/' onClick={() => setIsMenuOpen(false)} className='flex items-center gap-2 px-4 py-3 hover:bg-gray-200 md:hidden'>
                                    Início
                                </Link>
                                <Link to='/catalog' onClick={() => setIsMenuOpen(false)} className='flex items-center gap-2 px-4 py-3 hover:bg-gray-200 md:hidden'>
                                    Catálogo
                                </Link>
                                <Link to='/journey' onClick={() => setIsMenuOpen(false)} className='flex items-center gap-2 px-4 py-3 hover:bg-gray-200 md:hidden'>
                                    Meu aprendizado
                                </Link>
                                {user?.role === 'INSTRUCTOR' && (
                                    <Link to='/painel' onClick={() => setIsMenuOpen(false)} className='flex items-center gap-2 px-4 py-3 hover:bg-gray-200 md:hidden'>
                                        Painel do instrutor
                                    </Link>
                                )}
                                {user?.role === 'ADMIN' && (
                                    <Link to='/admin' onClick={() => setIsMenuOpen(false)} className='flex items-center gap-2 px-4 py-3 hover:bg-gray-200 md:hidden'>
                                        Painel do administrador
                                    </Link>
                                )}
                                <button
                                    onClick={handleLogout}
                                    className="flex items-center gap-2 w-full px-4 py-3 hover:bg-red-100 text-red-500"
                                >
                                    <ArrowRightEndOnRectangleIcon className="h-5 w-5" />
                                    Logout
                                </button>
                            </div>
                        )}
                    </div>
                ) : (
                    <div>
                        <Link to='/login' className='flex items-center bg-white rounded-2xl m-2 p-2'>
                            Login <ArrowRightEndOnRectangleIcon className="ml-2 text-black h-5 w-5" />
                        </Link>
                    </div>

                )}
            </div>
        </div>

    )
}

