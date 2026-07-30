import { useEffect, useState } from 'react'
import api from '../services/api'

export type UserRole = 'ADMIN' | 'USER' | 'INSTRUCTOR';

type CurrentUser = {
    id: number;
    role: UserRole;
};

export const useAuth = () => {
    const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null)
    const [role, setRole] = useState<UserRole | null>(null)
    const [userId, setUserId] = useState<number | null>(null)

    useEffect(() => {
        api.get<CurrentUser>('/users/me')
            .then(({ data }) => {
                setIsAuthenticated(true);
                setUserId(data.id);
                setRole(data.role);
            })
            .catch(() => {
                setIsAuthenticated(false);
                setUserId(null);
                setRole(null);
            })
    }, [])

    return { isAuthenticated, role, userId }
};
