import api from './api';
import type { Module } from '../types/module';

const moduleService = {
    async findById(courseId: number, id: number): Promise<Module> {
        const res = await api.get(`/courses/${courseId}/modules/${id}`);
        return res.data;
    },

    async create(courseId: number, data: { title: string; description: string; orderIndex: number }): Promise<Module> {
        const res = await api.post(`/courses/${courseId}/modules`, data);
        return res.data;
    },

    async update(courseId: number, id: number, data: { title: string; description: string; orderIndex: number }): Promise<Module> {
        const res = await api.put(`/courses/${courseId}/modules/${id}`, data);
        return res.data;
    },

    async delete(courseId: number, id: number): Promise<void> {
        await api.delete(`/courses/${courseId}/modules/${id}`);
    },
};

export default moduleService;
