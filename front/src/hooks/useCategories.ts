import { useEffect, useState } from "react";
import CategoryService from "../services/categoryService";
import type { Category } from "../types/category";

export function useCategories(id?: number) {
    const [categories, setCategories] = useState<Category[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        async function load() {
            try {
                const data = await CategoryService.findAll();
                setCategories(data);
            } catch (err){
                console.log(err)
                setCategories([]);
            } finally {
                setLoading(false);
            }
        }

        load();
    }, [id]);

    return {categories, loading };
}