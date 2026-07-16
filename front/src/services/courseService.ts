import api from './api'
import type { Course } from '../types/course'

const courseService = {
    findBySlug(slug: string) {
        return api.get(`/courses/${slug}`)
    },

    findAll(): Promise<Course[]> {
        return api.get('/courses')
    }
};

export default courseService;