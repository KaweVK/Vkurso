import api from './api';
import type { Category } from '../types/category';

const CategoryService = {
    async findById(categoryId?: number, courseId?: number): Promise<Category> {
        const res = await api.get(`/courses/${courseId}/categories/${categoryId}`)
        return res.data
    },

    async findAll(): Promise<Category[]> {
        const res = await api.get(`/course-category`)
        return res.data.content
    },

    async get(categoryId?: number): Promise<Category> {
        const res = await api.get(`/course-category/${categoryId}`)
        return res.data
    },

    async create(data: {name: string}): Promise<Category> {
        const res = await api.post(`/course-category`, data)
        return res.data
    },

    async update(categoryId: number, data: {name: string}): Promise<Category> {
        const res = await api.put(`/course-category/${categoryId}`, data)
        return res.data
    },

    async delete(categoryId: number) {
        await api.delete(`/course-category/${categoryId}`)
    },
};

export default CategoryService;