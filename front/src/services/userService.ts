import type { Role, User } from "../types"
import api from "./api"

const UserService = {
    async findById(id: number): Promise<User> {
        const res = await api.get(`/users/${id}`)
        return res.data
    },

    async findAll(): Promise<User[]> {
        const res = await api.get(`/users`)
        return res.data.content
    }, 

    async update(id: number, data: { fullName: string, email: string, role: Role}): Promise<User> {
        const res = await api.put(`/users/${id}`, data);
        return res.data;
    },

    async delete(id: number): Promise<void> {
        await api.delete(`/users/${id}`)
    }

}

export default UserService;