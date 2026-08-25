import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api from '../../services/api'
import '../../index.css'
import Logo from '../../assets/logo.png'
import Loading from '../../components/loading'

function Login() {
    const navigate = useNavigate()
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState('')
    const [loading, setLoading] = useState(false)

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault()
        setError('')
        setLoading(true)
        try {
            await api.post('/auth/login', { email, password })
            navigate('/')
        } catch {
            setError('Email ou senha inválidos.')
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="flex h-screen w-full">
            <div className="flex w-3/4 items-center justify-center bg-blue-950">
                <div>
                    <img src={Logo} alt="Logo" className='rounded-xl w-[200px] h-[200px] m-[5px]' />
                </div>
            </div>

            <div className="flex w-1/4 items-center justify-center bg-indigo-100 p-8 dark:bg-indigo-950 ">
                <form className="flex w-full flex-col" onSubmit={handleSubmit}>
                    <h1 className="mb-6 text-2xl font-bold text-gray-800 dark:text-white">Login</h1>
                    <input
                        className="mb-4 rounded border border-gray-300 p-3 outline-none focus:border-blue-500"
                        name="email"
                        type="email"
                        placeholder="Email"
                        value={email} onChange={(e) => setEmail(e.target.value)}
                    />
                    <input
                        className="mb-4 rounded border border-gray-300 p-3 outline-none focus:border-blue-500"
                        name="senha"
                        type="password"
                        placeholder="Senha"
                        value={password} onChange={(e) => setPassword(e.target.value)}
                    />
                    {error && <p className="mb-4 text-sm text-red-600">{error}</p>}
                    <button
                        className="rounded bg-blue-600 p-3 font-semibold text-white transition hover:bg-blue-700"
                        type="submit"
                        disabled={loading}
                    >
                        {loading ? <Loading /> : 'Entrar'}
                    </button>
                </form>
            </div>
        </div>
    )
}

export default Login;