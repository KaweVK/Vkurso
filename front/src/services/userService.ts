import type { User } from "../types"
import api from "./api"

const UserService = {
    async findAll(): Promise<User[]> {
        const res = await api.get(`/users`)
        return res.data.content
    }

}

export default UserService;