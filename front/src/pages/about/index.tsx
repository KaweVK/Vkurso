import '../../index.css'
import Navbar from '../../components/navbar'
import Footbar from '../../components/footbar'
import Me from '../../assets/foto.png'
import Sist1 from '../../assets/sist-1.png'
import Sist2 from '../../assets/sist-2.png'
import Sist3 from '../../assets/sist-3.png'
import { Link } from 'react-router-dom';
import { useState } from 'react';
import { ChevronLeftIcon, ChevronRightIcon, CodeBracketIcon, ArrowLongRightIcon } from "@heroicons/react/24/outline";

function About() {

    const [currentImage, setCurrentImage] = useState(0);

    const images = [
        Sist1,
        Sist2,
        Sist3
    ];

    const nextImage = () => {
        setCurrentImage((currentImage + 1) % images.length);
    };

    const previousImage = () => {
        setCurrentImage((currentImage - 1 + images.length) % images.length);
    };

    return (
        <body className='bg-indigo-100/30'>
            <Navbar />
            <div className="flex h-screen items-center justify-between rounded-b-[30px] bg-blue-950 px-[10%]">

                <div className="flex max-w-[600px] flex-col">
                    <p className="text-6xl font-bold text-white">Sobre nosso projeto.</p>
                    <p className="mt-4 text-5xl font-bold text-white">Por <span className="text-blue-600">Victor Kawê.</span></p>
                    <p className="mt-8 max-w-[500px] text-2xl font-semibold text-white">Esse projeto foi desenvolvido para aprimorar as minhas habilidades pessoais com Desenvolvimento de Software,UI/UX, Sistemas de terceiros, testes e observabilidade.</p>

                    <div className="mt-5 flex items-center gap-3">
                        <Link to="/catalog" className="flex h-10 w-[150px] items-center justify-center rounded-xl bg-blue-500 transition hover:bg-indigo-700">
                            <p className="font-semibold text-white"> Explorar Catálogo</p>
                        </Link>

                        <Link to="/" className="flex h-10 w-[150px] items-center justify-center gap-1 transition duration-300 ease-in-out  hover:-translate-y-1 hover:scale-105 ">
                            <p className="font-semibold text-white">Início</p> <ArrowLongRightIcon className="h-5 w-8 text-white" />
                        </Link>

                    </div>
                </div>

                <div className="flex items-center justify-center">
                    <div className="relative">
                        <div className="absolute inset-0 rounded-2xlbg-blue-500blur-2xlopacity-30" />
                        <img src={Me} alt="Foto de Victor Kawê" className="relative h-[320px] w-[320px] rounded-2xl object-cover shadow-2xl" />
                    </div>
                </div>

            </div>
            <div className='flex min-h-screen flex-col justify-center rounded-[30px] bg-indigo-100/30 px-[10%] py-20 shadow-lg'>

                <div className='flex items-center justify-between'>
                    <div>
                        <p className='text-4xl font-bold text-blue-950'>Sobre o desenvolvimento</p>
                        <p className='mt-3 max-w-[650px] text-lg font-medium text-gray-600'>O Vkurso é um projeto pensado e desenvolvido para colocar em prática conceitos de desenvolvimento de software, arquitetura, UI/UX, integração com serviços externos, testes e observabilidade do sistema.</p>
                    </div>

                    <a href='https://github.com/KaweVK/Vkurso' target='_blank' rel='noopener noreferrer' className='flex h-12 items-center gap-2 rounded-xl bg-blue-950 px-5 font-semibold text-white transition duration-300 hover:-translate-y-1 hover:bg-blue-800'>
                        <CodeBracketIcon className='h-5 w-5' />
                        GitHub
                    </a>
                </div>

                <div className='mt-12 grid grid-cols-1 gap-10 lg:grid-cols-2'>

                    <div className='rounded-2xl bg-white p-8 shadow-md'>
                        <p className='text-2xl font-bold text-blue-950'>Stacks utilizadas</p>
                        <p className='mt-2 text-gray-500'>Tecnologias e bibliotecas utilizadas na construção do sistema.</p>

                        <div className='mt-6 flex flex-wrap gap-3'>
                            <span className='rounded-xl bg-blue-100 px-4 py-2 font-semibold text-blue-800'>React</span>
                            <span className='rounded-xl bg-blue-100 px-4 py-2 font-semibold text-blue-800'>TypeScript</span>
                            <span className='rounded-xl bg-green-100 px-4 py-2 font-semibold text-green-800'>Spring Boot</span>
                            <span className='rounded-xl bg-yellow-100 px-4 py-2 font-semibold text-yellow-800'>Java</span>
                            <span className='rounded-xl bg-indigo-100 px-4 py-2 font-semibold text-indigo-800'>PostgreSQL</span>
                            <span className='rounded-xl bg-cyan-100 px-4 py-2 font-semibold text-cyan-800'>Tailwind CSS</span>
                            <span className='rounded-xl bg-gray-100 px-4 py-2 font-semibold text-gray-800'>Docker</span>
                            <span className='rounded-xl bg-red-100 px-4 py-2 font-semibold text-red-800'>Redis</span>
                            <span className='rounded-xl bg-purple-100 px-4 py-2 font-semibold text-purple-800'>JWT</span>
                            <span className='rounded-xl bg-sky-100 px-4 py-2 font-semibold text-sky-800'>JUnit</span>
                        </div>

                        <div className='mt-8 border-t border-gray-200 pt-6'>
                            <p className='text-lg font-bold text-blue-950'>Principais conceitos</p>
                            <div className='mt-4 space-y-2 text-gray-600'>
                                <p>• Arquitetura de software</p>
                                <p>• Autenticação e autorização</p>
                                <p>• APIs REST</p>
                                <p>• Cache e persistência</p>
                                <p>• Testes e observabilidade</p>
                            </div>
                        </div>
                    </div>

                    <div className='relative overflow-hidden rounded-2xl bg-blue-950 shadow-md'>
                        <img src={images[currentImage]} alt={`Imagem do sistema ${currentImage + 1}`} className='h-[420px] w-full object-cover' />

                        <button type='button' onClick={previousImage} className='absolute left-4 top-1/2 flex h-10 w-10 -translate-y-1/2 items-center justify-center rounded-full bg-white/90 text-blue-950 shadow-md transition hover:scale-110'>
                            <ChevronLeftIcon className='h-6 w-6' />
                        </button>

                        <button type='button' onClick={nextImage} className='absolute right-4 top-1/2 flex h-10 w-10 -translate-y-1/2 items-center justify-center rounded-full bg-white/90 text-blue-950 shadow-md transition hover:scale-110'>
                            <ChevronRightIcon className='h-6 w-6' />
                        </button>

                        <div className='absolute bottom-4 left-1/2 flex -translate-x-1/2 gap-2'>
                            {images.map((_, index) => (
                                <button type="button" key={index} onClick={() => setCurrentImage(index)} className={`h-2.5 w-2.5 rounded-full transition ${currentImage === index ? 'bg-white scale-125' : 'bg-white/50'}`} />
                            ))}
                        </div>
                    </div>

                </div>

            </div>
            <Footbar />
        </body>
    )
}

export default About;