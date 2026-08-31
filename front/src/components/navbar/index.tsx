import '../../index.css'
import { Link } from 'react-router-dom'
import Logo from '../../assets/logo.png'
import { useAuth } from "../../hooks/useAuth";
import { UserIcon } from '@heroicons/react/24/outline';

export default function Navbar() {
    const { isAuthenticated, role } = useAuth();

    return (
        <div className="flex bg-white dark:bg-blue-900 w-full h-[50px] justify-center font-mono">
            <div className='flex justify-center'>
                <img src={Logo} alt="logo" className='bg-blue-400 rounded-xl h-[90%] self-center' />
                <p className='self-center p-2 dark:text-blue-200'>Vkursos</p>
                <div className='flex w-5/6 justify-center space-x-4 '>
                    <Link to='/' className='self-center p-2 dark:text-blue-200 hover:bg-sky-500 rounded-xl'>Início</Link>
                    <Link to='/catalog' className='self-center p-2 dark:text-blue-200 hover:bg-sky-500 rounded-xl'>Catálogo</Link>
                    <Link to='/journey' className='self-center p-2 dark:text-blue-200 hover:bg-sky-500 rounded-xl'>Meu aprendizado</Link>
                    <Link to='/' className='self-center p-2 dark:text-blue-200 hover:bg-sky-500 rounded-xl'>Painel do instrutor</Link>
                </div>
                {role === 'INSTRUCTOR' && (
                    <Link to='/instructor/courses/new' className='self-center p-2 dark:text-white hover:bg-sky-500 rounded-xl'>Criar curso</Link>
                )}
                {isAuthenticated ? (
                    <Link to='/me'>
                        <button className='flex bg-blue-400 dark:bg-white rounded-full m-[9px] p-2'>
                            Perfil <UserIcon className="mt-1 text-black font-semibold h-5 w-8"/>
                        </button>
                    </Link>
                ) : (
                    <Link to='/login'>
                        <button className='flex bg-blue-400 dark:bg-white rounded-2xl m-[9px] p-2'>
                            Login <UserIcon className="mt-1 text-black font-semibold h-5 w-8"/>
                        </button>
                    </Link>
                )}
            </div>
        </div>

    )
}

