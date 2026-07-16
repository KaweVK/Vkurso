import { useEffect, useState } from "react";
import courseService from "../services/courseService";
import type { Course } from "../types/course";

export function useCourses() {
    const [courses, setCourses] = useState<Course[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        async function load() {
            try {
                const data = await courseService.findAll();
                setCourses(data);
            } catch (err){
                console.log(err)
                setCourses([]);
            } finally {
                setLoading(false);
            }
        }

        load();
    }, []);

    return { courses, loading };
}

export function useCourse(slug?: string) {
    const [course, setCourse] = useState<Course>();
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        async function load() {
            try {
                const data = await courseService.findBySlug(slug);
                setCourse(data);
            } catch (err){
                console.log(err)
            } finally {
                setLoading(false);
            }
        }

        load();
    }, [slug]);

    return { course, loading };
}

export default useCourses;