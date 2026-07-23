import { useEffect, useState } from 'react';
import enrollmentService from '../services/enrollmentService';

export function useEnrollment(courseId: number | undefined) {
    const [enrolled, setEnrolled] = useState(false);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (!courseId) return;

        let cancelled = false;

        enrollmentService.myEnrollment(courseId)
            .then((e) => {
                if (!cancelled) setEnrolled(e !== null);
            })
            .finally(() => {
                if (!cancelled) setLoading(false);
            });

        return () => {
            cancelled = true;
        };
    }, [courseId]);

    async function enroll() {
        if (!courseId) return;
        await enrollmentService.enroll(courseId);
        setEnrolled(true);
    }

    async function cancel() {
        if (!courseId) return;
        await enrollmentService.cancel(courseId);
        setEnrolled(false);
    }

    return { enrolled, loading, enroll, cancel };
}