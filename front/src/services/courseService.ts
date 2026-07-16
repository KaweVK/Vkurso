import api from './api'
import type { Course } from '../types/course'

const courseService = {
    async findBySlug(slug: string) {
        return api.get(`/courses/${slug}`)
    },

    async findAll(): Promise<Course[]> {
        const res = await api.get('/courses')
        return res.data.content
    }
};

export default courseService;