import '../../index.css'
import { Link } from 'react-router-dom'
import Logo from '../../assets/logo.png'

export default function Navbar() {
    return (
        <div className="flex bg-white dark:bg-blue-900 w-full h-[50px] justify-center font-mono">
            <div className='flex justify-center'>
                <img src={Logo} alt="logo" className='bg-blue-400 rounded-xl h-[90%] self-center'/>
                <p className='self-center p-2 dark:text-blue-200'>Vkursos</p>
                <div className='flex w-5/6 justify-center space-x-4 '>
                    <Link to='/' className='self-center p-2 dark:text-blue-200 hover:bg-sky-500 rounded-xl'>Início</Link>
                    <Link to='/catalog' className='self-center p-2 dark:text-blue-200 hover:bg-sky-500 rounded-xl'>Catálogo</Link>
                    <Link to='/me' className='self-center p-2 dark:text-blue-200 hover:bg-sky-500 rounded-xl'>Meu aprendizado</Link>
                    <Link to='/' className='self-center p-2 dark:text-blue-200 hover:bg-sky-500 rounded-xl'>Painel do instrutor</Link>
                </div>
                <Link to='/' className='self-center p-2 dark:text-white  hover:bg-sky-500 rounded-xl'>Ensinar</Link>
                <Link to='/login'>
                    <button className='bg-blue-400 dark:bg-white rounded-full m-[9px] p-2'>
                        login
                    </button>
                </Link>
            </div>
        </div>

    )
}

