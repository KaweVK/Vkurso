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
    }
};

export default CategoryService;