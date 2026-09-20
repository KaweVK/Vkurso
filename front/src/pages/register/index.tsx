import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import axios from 'axios'
import '../../index.css'
import Logo from '../../assets/logo.png'
import Loading from '../../components/loading'
import authService from '../../services/authService'

function Register() {
    const navigate = useNavigate()
    const [fullName, setFullName] = useState('')
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [passwordConfirmation, setPasswordConfirmation] = useState('')
    const [code, setCode] = useState('')
    const [verificationMode, setVerificationMode] = useState(false)
    const [message, setMessage] = useState('')
    const [error, setError] = useState('')
    const [loading, setLoading] = useState(false)

    async function handleRegister(e: React.FormEvent) {
        e.preventDefault()
        setError('')
        setMessage('')

        if (password !== passwordConfirmation) {
            setError('As senhas não coincidem.')
            return
        }

        setLoading(true)
        try {
            await authService.register(fullName, email, password)
            setVerificationMode(true)
            setMessage('Enviamos um código de verificação para seu email.')
        } catch (error) {
            if (axios.isAxiosError(error) && error.response?.status === 409) {
                setError('Este email já está cadastrado.')
            } else {
                setError('Não foi possível criar sua conta.')
            }
        } finally {
            setLoading(false)
        }
    }

    async function handleVerification(e: React.FormEvent) {
        e.preventDefault()
        setError('')
        setLoading(true)

        try {
            await authService.verifyCode(email, code)
            navigate('/login')
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
            setMessage('Um novo código foi enviado para seu email.')
        } catch {
            setError('Aguarde antes de solicitar um novo código.')
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="flex h-screen w-full">
            <div className="flex w-3/4 items-center justify-center bg-blue-950">
                <img alt="Logo" className="m-[5px] h-[200px] w-[200px] rounded-xl" src={Logo} />
            </div>

            <div className="flex w-1/4 items-center justify-center bg-indigo-100 p-8 dark:bg-indigo-950">
                {verificationMode ? (
                    <form className="flex w-full flex-col" onSubmit={handleVerification}>
                        <h1 className="mb-6 text-2xl font-bold text-gray-800 dark:text-white">Verificar email</h1>
                        <p className="mb-4 text-sm text-gray-600 dark:text-gray-300">Digite o código enviado para {email}.</p>
                        <input
                            className="mb-4 rounded border border-gray-300 p-3 outline-none focus:border-blue-500"
                            type="text"
                            placeholder="Código de verificação"
                            value={code}
                            onChange={(e) => setCode(e.target.value)}
                            maxLength={6}
                            required
                        />
                        {message && <p className="mb-4 text-sm text-blue-600">{message}</p>}
                        {error && <p className="mb-4 text-sm text-red-600">{error}</p>}
                        <button className="rounded bg-blue-600 p-3 font-semibold text-white transition hover:bg-blue-700" type="submit" disabled={loading}>
                            {loading ? <Loading /> : 'Verificar código'}
                        </button>
                        <button className="mt-3 text-sm text-blue-600 hover:underline" type="button" onClick={handleResendCode} disabled={loading}>
                            Reenviar código
                        </button>
                        <Link className="mt-3 text-center text-sm text-blue-600 hover:underline" to="/login">Voltar para o login</Link>
                    </form>
                ) : (
                    <form className="flex w-full flex-col" onSubmit={handleRegister}>
                        <h1 className="mb-6 text-2xl font-bold text-gray-800 dark:text-white">Cadastro</h1>
                        <input
                            className="mb-4 rounded border border-gray-300 p-3 outline-none focus:border-blue-500"
                            type="text"
                            placeholder="Nome completo"
                            value={fullName}
                            onChange={(e) => setFullName(e.target.value)}
                            required
                        />
                        <input
                            className="mb-4 rounded border border-gray-300 p-3 outline-none focus:border-blue-500"
                            type="email"
                            placeholder="Email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                        />
                        <input
                            className="mb-4 rounded border border-gray-300 p-3 outline-none focus:border-blue-500"
                            type="password"
                            placeholder="Senha"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                        <input
                            className="mb-4 rounded border border-gray-300 p-3 outline-none focus:border-blue-500"
                            type="password"
                            placeholder="Confirme sua senha"
                            value={passwordConfirmation}
                            onChange={(e) => setPasswordConfirmation(e.target.value)}
                            required
                        />
                        {error && <p className="mb-4 text-sm text-red-600">{error}</p>}
                        <button className="rounded bg-blue-600 p-3 font-semibold text-white transition hover:bg-blue-700" type="submit" disabled={loading}>
                            {loading ? <Loading /> : 'Criar conta'}
                        </button>
                        <Link className="mt-3 text-center text-sm text-blue-600 hover:underline" to="/login">Já possui uma conta? Entrar</Link>
                    </form>
                )}
            </div>
        </div>
    )
}

export default Register
