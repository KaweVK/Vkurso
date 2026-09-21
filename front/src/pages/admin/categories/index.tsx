import { Link, useNavigate } from "react-router-dom";
import type { Category } from "../../../types";
import { PencilIcon, TrashIcon } from "@heroicons/react/24/outline";
import { useEffect, useState } from "react";
import CategoryService from "../../../services/categoryService";

export default function Categories() {
    const [categories, setCategories] = useState<Category[]>([])
    const navigate = useNavigate()
    const [error, setError] = useState('');

    useEffect(() => {
        const loadCategories = async () => {
            try {
                const data = await CategoryService.findAll();
                setCategories(data)
            } catch (error) {
                console.error('Erro ao carregar progresso:', error);
            }
        }

        loadCategories();
    }, [])

    async function requestDeleteCategory(userId: number) {
        try {
            await CategoryService.delete(userId)
            navigate(0)
        } catch {
            setError('Não foi possível excluir o usuário!')
        }
    }

    return (
        <div className="px-10 py-20">
            <div className="flex items-center justify-between">
                <div>
                    <h1 className="text-2xl font-bold">
                        Categorias
                    </h1>

                    <p className="mt-1 text-gray-600">
                        Gerencie as categorias dos cursos disponíveis na plataforma.
                    </p>
                </div>

                <Link className="rounded-lg bg-blue-500 px-4 py-2 text-white" to={`/admin/categories/create`}>
                    Nova categoria
                </Link>
            </div>

            <div className="mt-6 overflow-hidden rounded-lg border bg-white">
                <table className="w-full">
                    <thead className="border-b bg-blue-100">
                        <tr>
                            <th className="px-6 py-3 text-left text-sm font-medium">
                                Nome
                            </th>

                            <th className="px-6 py-3 text-right text-sm font-medium">
                                Ações
                            </th>
                        </tr>
                    </thead>

                    <tbody>
                        {categories.map((category) => (
                            <tr
                                key={category.id}
                                className="border-b last:border-b-0"
                            >
                                <td className="px-6 py-4">
                                    {category.name}
                                </td>

                                <td className="px-6 py-4 text-right">
                                    <div className="flex justify-end gap-2">
                                        <Link
                                            to={`/admin/categories/${category.id}/edit`}
                                            className="rounded-md text-indigo-600 hover:bg-indigo-50"
                                        >
                                            <PencilIcon className="h-5 w-5" />
                                        </Link>

                                        <button
                                            type="button"
                                            onClick={() => requestDeleteCategory(category.id)}
                                            className="rounded-md text-red-600 hover:bg-red-50"
                                        >
                                            <TrashIcon className="h-5 w-5" />
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}