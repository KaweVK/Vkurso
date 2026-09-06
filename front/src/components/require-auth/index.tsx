import { Navigate, Outlet, useLocation } from 'react-router-dom'
import { useAuth } from '../../hooks/useAuth'
import Loading from '../loading'

function RequireAuth() {
    const { user, loading } = useAuth()
    const location = useLocation()

    if (loading) {
        return <Loading />
    }

    if (!user) {
        return (
            <Navigate
                to="/login"
                replace
                state={{ from: location }}
            />
        )
    }

    return <Outlet />
}

export default RequireAuth;