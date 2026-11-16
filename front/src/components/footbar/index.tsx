import '../../index.css'
import { Link } from 'react-router-dom'
import Logo from '../../assets/logo.webp'

export default function Footbar() {

    return (
        <div className="flex flex-row md:flex-row bg-blue-900 w-full h-full justify-between items-center px-[10%] font-mono">
            <div className='flex row-1 items-center justify-center'>
                <img src={Logo} alt="logo" className='rounded-xl size-12' />
                <p className='p-2 text-blue-200'>Vkurso @2026</p>
            </div>
            <div className='flex flex-col md:flex-row row-2 justify-between p-2'>
                    <Link to='/' className='p-2 text-white hover:underline rounded-xl'>Início</Link>
                    <Link to='/catalog' className='p-2 text-white hover:underline rounded-xl'>Termos de Uso</Link>
                    <Link to='/catalog' className='p-2 text-white hover:underline rounded-xl'>Política de privacidade</Link>
                    <Link to='/about' className='p-2 text-white hover:underline rounded-xl'>Sobre</Link>
            </div>
        </div>

    )
}

