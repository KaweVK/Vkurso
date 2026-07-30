import api from './api';
import type { Lesson } from '../types/lesson';

const lessonService = {
    async findById(courseId: number, moduleId: number, id: number): Promise<Lesson> {
        const res = await api.get(`/courses/${courseId}/modules/${moduleId}/lessons/${id}`);
        return res.data;
    },

    async create(courseId: number, moduleId: number, data: { title: string; description: string; orderIndex: number }): Promise<Lesson> {
        const res = await api.post(`/courses/${courseId}/modules/${moduleId}/lessons`, data);
        return res.data;
    },

    async update(courseId: number, moduleId: number, id: number, data: { title: string; description: string; orderIndex: number }): Promise<Lesson> {
        const res = await api.put(`/courses/${courseId}/modules/${moduleId}/lessons/${id}`, data);
        return res.data;
    },

    async uploadVideo(courseId: number, moduleId: number, id: number, file: File): Promise<Lesson> {
        const formData = new FormData();
        formData.append('file', file);
        const res = await api.post(`/courses/${courseId}/modules/${moduleId}/lessons/${id}/video`, formData, {
            headers: { 'Content-Type': 'multipart/form-data' },
        });
        return res.data;
    },
};

export default lessonService;
