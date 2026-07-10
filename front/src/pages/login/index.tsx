import '../../index.css'

function Login() {
    return (
        <div className="flex h-screen w-full">
            {/* Lado Direito - 75% Azul */}
            <div className="w-3/4 bg-blue-600">
                <div className="flex items-center bg-white">
                    <p>Olá</p>
                </div>
            </div>

            {/* Lado Esquerdo - 25% Branco */}
            <div className="flex w-1/4 items-center justify-center bg-white p-8">
                <form className="flex w-full flex-col">
                    <h1 className="mb-6 text-2xl font-bold text-gray-800">Login</h1>
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

export default Login