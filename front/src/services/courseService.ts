import api from './api'
import type { Course, CourseStatus } from '../types/course'

const courseService = {
    async findById(id: number): Promise<Course> {
        const res = await api.get(`/courses/id/${id}`)
        return res.data
    },

    async findBySlug(slug?: string): Promise<Course> {
        const res = await api.get(`/courses/${slug}`)
        return res.data
    },

    async findByInstructor(status?: CourseStatus): Promise<Course[]> {
        const res = await api.get(`/courses/by-instructor`, {
            params: {
                status
            }
        });
        return res.data.content
    },

    async findAll(): Promise<Course[]> {
        const res = await api.get('/courses')
        return res.data.content
    },

    async search(search = '', categoryId?: number): Promise<Course[]> {
        const res = await api.get('/courses', {
            params: {
                search: search || undefined,
                categoryId
            }
        })
        return res.data.content
    },

    async featured(): Promise<Course[]> {
        const res = await api.get('courses/featured')
        return res.data
    },

    async create(data: { title: string; description: string; level: Course['level']; price: number }): Promise<Course> {
        const res = await api.post('/courses', data)
        return res.data
    },

    async update(id: number, data: { title: string; description: string; level: Course['level']; price: number }): Promise<Course> {
        const res = await api.put(`/courses/${id}`, data)
        return res.data
    },

    async publish(id: number): Promise<Course> {
        const res = await api.post(`/courses/${id}/publish`)
        return res.data
    },

    async delete(id: number): Promise<void> {
        await api.delete(`/courses/${id}`)
    },
};

export default courseService;
