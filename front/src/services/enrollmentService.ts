import api from './api';
import axios from 'axios';
import type { Enrollment } from '../types';

const enrollmentService = {
    async enroll(courseId: number): Promise<Enrollment> {
        const res = await api.post(`/courses/${courseId}/enrollments`)
        return res.data;
    }, 

    async cancel(courseId: number) {
        await api.delete(`/courses/${courseId}/enrollments`)
    },

    async myEnrollment(courseId: number): Promise<Enrollment | null> {
        try {
            const res = await api.get(`courses/${courseId}/enrollments/me`);
            return res.data;
        } catch (err) {
            if (axios.isAxiosError(err) && err.response?.status === 404) return null;
            throw err;
        }
    }
}

export default enrollmentService;