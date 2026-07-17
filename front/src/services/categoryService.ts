import api from './api';
import type { Categoria } from '../types/category';

const CategoryService = {
    async findById(categoryId?: number, courseId?: number): Promise<Categoria> {
        const res = await api.get(`/courses/${courseId}/categories/${categoryId}`)
        return res.data
    },

    async findAll(): Promise<Categoria[]> {
        const res = await api.get(`/course-category`)
        return res.data.content
    }
};

export default CategoryService;