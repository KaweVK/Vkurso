import api from './api'
import type { Course } from '../types/course'

const courseService = {
    async findBySlug(slug?: string): Promise<Course> {
        const res = await api.get(`/courses/${slug}`)
        return res.data
    },

    async findAll(): Promise<Course[]> {
        const res = await api.get('/courses')
        return res.data.content
    }
};

export default courseService;