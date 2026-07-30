import { useEffect, useState } from 'react'
import api from '../services/api'

export const useAuth = () => {
    const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null)

    useEffect(() => {
        api.get('/auth/me')
            .then(() => setIsAuthenticated(true))
            .catch(() => setIsAuthenticated(false))
    }, [])

    return { isAuthenticated }
};