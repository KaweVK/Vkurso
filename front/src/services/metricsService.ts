import type { Metrics } from '../types/metrics';
import api from './api';

const metricsService = {
    async getMetrics(): Promise<Metrics> {
        const res = await api.get("/metrics");
        return res.data;
    }
}

export default metricsService;