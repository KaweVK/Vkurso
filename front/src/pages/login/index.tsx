import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import '../../index.css'
import Logo from '../../assets/logo.png'
import Loading from '../../components/loading'
import { useAuth } from '../../hooks/useAuth'
import axios from 'axios'
import authService from '../../services/authService'

function Login() {
    const navigate = useNavigate()
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState('')
    const [loading, setLoading] = useState(false)
    const { login } = useAuth()
    const [verificationMode, setVerificationMode] = useState(false)
    const [code, setCode] = useState('')
    const [verificationMessage, setVerificationMessage] = useState('')

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault()
        setError('')
        setLoading(true)
        try {
            await login(email, password)
            navigate('/')
        } catch (error) {
            if (
                axios.isAxiosError(error) &&
                (error.response?.status === 403 ||
                    error.response?.data?.detail?.startsWith('Email não verificado'))
            ) {
                setVerificationMode(true)
                setVerificationMessage(
                    'Seu email ainda não foi verificado. Verifique sua caixa de entrada e spam.'
                )
            } else {
                setError('Email ou senha inválidos.')
            }
        } finally {
            setLoading(false)
        }
    }

    async function handleVerificationSubmit(e: React.FormEvent) {
        e.preventDefault()
        setError('')
        setLoading(true)

        try {
            await authService.verifyCode(email, code)

            setVerificationMode(false)
            setVerificationMessage(
                'Email verificado. Agora você pode fazer login.'
            )
        } catch {
            setError('Código inválido ou expirado.')
        } finally {
            setLoading(false)
        }
    }

    async function handleResendCode() {
        setError('')
        setLoading(true)

        try {
            await authService.resendVerification(email)
            setVerificationMessage('Um novo código foi enviado para seu email.')
        } catch {
            setError('Não foi possível reenviar o código agora.')
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
                {verificationMode ? (
                    <form className="flex w-full flex-col" onSubmit={handleVerificationSubmit}>
                        <h1 className="mb-6 text-2xl font-bold text-gray-800 dark:text-white">
                            Verificar email
                        </h1>
                        <p className="mb-4 text-sm text-gray-600 dark:text-gray-300">
                            Digite o código enviado para {email}.
                        </p>
                        <input
                            className="mb-4 rounded border border-gray-300 p-3 outline-none focus:border-blue-500"
                            type="text"
                            placeholder="Código de verificação"
                            value={code}
                            onChange={(e) => setCode(e.target.value)}
                            maxLength={6}
                        />
                        {verificationMessage && (
                            <p className="mb-4 text-sm text-blue-600">
                                {verificationMessage}
                            </p>
                        )}
                        {error && (
                            <p className="mb-4 text-sm text-red-600">
                                {error}
                            </p>
                        )}
                        <button
                            className="rounded bg-blue-600 p-3 font-semibold text-white transition hover:bg-blue-700"
                            type="submit"
                            disabled={loading}
                        >
                            {loading ? <Loading /> : 'Verificar código'}
                        </button>
                        <div className='flex justify-between'>
                            <button
                                type="button"
                                className="mt-3 text-sm text-white hover:underline"
                                onClick={() => setVerificationMode(false)}
                            >
                                Voltar para o login
                            </button>
                            <button
                                type="button"
                                className="mt-3 text-sm text-white hover:underline"
                                onClick={handleResendCode}
                                disabled={loading}
                            >
                                Reenviar código
                            </button>
                        </div>
                    </form>
                ) : (
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
                        <p className="mt-3 text-center text-sm text-white" >
                            Ainda não possui uma conta? <Link className='text-blue-600 hover:underline' to="/register">Cadastre-se</Link>
                        </p>

                    </form>
                )}
            </div>
        </div>
    )
}

export default Login;
