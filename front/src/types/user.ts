export type Role = 'ADMIN' | 'USER' | 'INSTRUCTOR'

export interface User {
    id: number, 
    fullName: string,
    email: string,
    role: Role
}