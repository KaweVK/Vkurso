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
    },

    async uploadPhoto(userId: number, file: File): Promise<void> {
        const formData = new FormData();
        formData.append('file', file);
        const res = await api.post(`/users/${userId}/upload`, formData, {
            headers: { 'Content-Type': 'multipart/form-data' },
        });
        return res.data;
    }

}

export default UserService;