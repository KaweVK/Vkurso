import '../../index.css'
import Logo from '../../assets/logo.png'

function Login() {
    return (
        <div className="flex h-screen w-full">
            {/* Lado Direito - 75% Azul */}
            <div className="flex w-3/4 items-center justify-center bg-blue-600 dark: bg-blue-950">
                <div className="flex w-3/5 h-3/5 bg-indigo-100 rounded-xl dark:bg-indigo-950">
                    <img src={Logo} alt="Logo" className='bg-blue-400 rounded-xl w-[50px] h-[50px] m-[5px]'/>
                </div>
            </div>

            {/* Lado Esquerdo - 25% Branco */}
            <div className="flex w-1/4 items-center justify-center bg-indigo-100 p-8 dark:bg-indigo-950 ">
                <form className="flex w-full flex-col">
                    <h1 className="mb-6 text-2xl font-bold text-gray-800 dark:text-white">Login</h1>
                    <input 
                        className="mb-4 rounded border border-gray-300 p-3 outline-none focus:border-blue-500" 
                        name="email" 
                        type="email" 
                        placeholder="Email" 
                    />
                    <input 
                        className="mb-4 rounded border border-gray-300 p-3 outline-none focus:border-blue-500" 
                        name="senha" 
                        type="password" 
                        placeholder="Senha" 
                    />
                    <button 
                        className="rounded bg-blue-600 p-3 font-semibold text-white transition hover:bg-blue-700" 
                        type="button"
                    >
                        Entrar
                    </button>
                </form>
            </div>
        </div>
    )
}

export default Login;