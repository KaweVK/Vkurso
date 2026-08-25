import '../../index.css'
import { Link } from 'react-router-dom'
import Logo from '../../assets/logo.png'

export default function Footbar() {

    return (
        <div className="flex bg-blue-900 w-full h-[50px] justify-between items-center px-[10%] font-mono">
            <div className='flex items-center'>
                <img src={Logo} alt="logo" className='bg-blue-400 rounded-xl size-12' />
                <p className='p-2 text-blue-200'>Vkursos 2026</p>
            </div>
            <div className='flex items-center space-x-4'>
                <Link to='/' className='p-2 text-white hover:bg-sky-500 rounded-xl'>Início</Link>
                <p className='p-2 text-white'>|</p>
                <Link to='/catalog' className='p-2 text-white hover:bg-sky-500 rounded-xl'>Termos de Uso</Link>
                <p className='p-2 text-white'>|</p>
                <Link to='/catalog' className='p-2 text-white hover:bg-sky-500 rounded-xl'>Política de privacidade</Link>
                <p className='p-2 text-white'>|</p>
                <Link to='/about' className='p-2 text-white hover:bg-sky-500 rounded-xl'>Sobre</Link>
            </div>
        </div>

    )
}

