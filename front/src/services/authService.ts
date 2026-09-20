import type { User } from "../types/user";
import api from "./api";

const authService = {
    async login(email: string, password: string) {
        await api.post('/auth/login', { email, password })
    },

    async register(fullName: string, email: string, password: string) {
        await api.post('/users', { fullName, email, password })
    },

    async logout() {
        await api.post('/auth/logout')
    },

    async get(): Promise<User> {
        const res = await api.get(`/users/me`)
        return res.data
    },

    async verifyCode(email: string, code: string): Promise<void> {
        await api.post('/users/verify-code', {email, code})
    },

    async resendVerification(email: string): Promise<void> {
        await api.post('/users/resend-verification', { email })
    },
}

export default authService;
