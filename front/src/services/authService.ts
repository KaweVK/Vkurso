import type { User } from "../types/user";
import api from "./api";

const authService = {
    async login(email: string, password: string) {
        await api.post('/auth/login', { email, password })
    },

    async logout() {
        await api.post('/auth/logout')
    },

    async get(): Promise<User> {
        const res = await api.get(`/users/me`)
        return res.data
    }
}

export default authService;