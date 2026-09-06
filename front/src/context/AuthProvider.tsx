import {
    useCallback,
    useEffect,
    useMemo,
    useState,
    type ReactNode,
} from 'react'
import axios from 'axios'

import authService from '../services/authService'
import type { User } from '../types/user'
import { AuthContext } from './AuthContext'

interface AuthProviderProps {
    children: ReactNode
}

export function AuthProvider({ children }: AuthProviderProps) {
    const [user, setUser] = useState<User | null>(null)
    const [loading, setLoading] = useState(true)

    const refreshUser = useCallback(async () => {
        try {
            const currentUser = await authService.get()
            setUser(currentUser)
        } catch (error) {
            if (
                axios.isAxiosError(error) &&
                error.response?.status === 401
            ) {
                setUser(null)
            } else {
                console.error('Erro ao carregar usuário:', error)
                setUser(null)
            }
        } finally {
            setLoading(false)
        }
    }, [])

    const login = useCallback(async (email: string, password: string) => {
        await authService.login(email, password)

        const currentUser = await authService.get()
        setUser(currentUser)
    }, [])

    const logout = useCallback(async () => {
        try {
            await authService.logout()
        } finally {
            setUser(null)
        }
    }, [])

    useEffect(() => {
        let active = true

        authService
            .get()
            .then((currentUser) => {
                if (active) {
                    setUser(currentUser)
                }
            })
            .catch((error: unknown) => {
                if (
                    axios.isAxiosError(error) &&
                    error.response?.status === 401
                ) {
                    if (active) {
                        setUser(null)
                    }

                    return
                }

                console.error('Erro ao carregar usuário:', error)

                if (active) {
                    setUser(null)
                }
            })
            .finally(() => {
                if (active) {
                    setLoading(false)
                }
            })

        return () => {
            active = false
        }
    }, [])

    const value = useMemo(
        () => ({
            user,
            loading,
            login,
            logout,
            refreshUser,
        }),
        [user, loading, login, logout, refreshUser],
    )

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    )
}