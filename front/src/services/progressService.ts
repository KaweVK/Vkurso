import api from './api'
import type { CourseProgress, PageResponse } from '../types';

const progressService = {
    async findStudentProgress(): Promise<PageResponse<CourseProgress>> {
        const res = await api.get('/progress');
        return res.data
    }
}

export default progressService;