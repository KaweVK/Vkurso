import { useEffect, useState } from "react";
import courseService from "../services/courseService";
import type { Course } from "../types/course";

function useCourses() {
    const [courses, setCourses] = useState<Course[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        async function load() {
            const data = await courseService.findAll();
            setCourses(data);
            setLoading(false);
        }

        load();
    }, []);

    return { courses, loading };
}

export default useCourses;