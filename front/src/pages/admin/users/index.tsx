import { Link } from "react-router-dom";
import type { User } from "../../../types";
import { PencilIcon, TrashIcon } from "@heroicons/react/24/outline";
import { useEffect, useState } from "react";
import UserService from "../../../services/userService";

export default function Users() {
    const [users, setUsers] = useState<User[]>([])

    useEffect(() => {
        const loadUsers = async () => {
            try {
                const data = await UserService.findAll();
                setUsers(data)
            } catch (error) {
                console.error('Erro ao carregar progresso:', error);
            }
        }

        loadUsers();
    }, [])

    return (
        <div className="px-10 py-20">
            <div className="flex items-center justify-between">
                <div>
                    <h1 className="text-2xl font-bold">
                        Usuários
                    </h1>

                    <p className="mt-1 text-gray-600">
                        Gerencie os usuários da plataforma.
                    </p>
                </div>
            </div>

            <div className="mt-6 overflow-hidden rounded-lg border bg-white">
                <table className="w-full">
                    <thead className="border-b bg-blue-100">
                        <tr>
                            <th className="px-6 py-3 text-left text-sm font-medium">
                                Nome
                            </th>

                            <th className="px-6 py-3 text-left text-sm font-medium">
                                Email
                            </th>

                            <th className="px-6 py-3 text-left text-sm font-medium">
                                Função
                            </th>

                            <th className="px-6 py-3 text-right text-sm font-medium">
                                Ações
                            </th>
                        </tr>
                    </thead>

                    <tbody>
                        {users.map((user) => (
                            <tr
                                key={user.id}
                                className="border-b last:border-b-0"
                            >
                                <td className="px-6 py-4">
                                    {user.fullName}
                                </td>

                                <td className="px-6 py-4 text-gray-600">
                                    {user.email}
                                </td>

                                <td className="px-6 py-4">
                                    <span className="rounded-full bg-gray-100 px-3 py-1 text-sm">
                                        {user.role}
                                    </span>
                                </td>

                                <td className="px-6 py-4 text-right">
                                    <div className="flex justify-end gap-2">
                                        <Link
                                            to={`/admin/users/${user.id}/edit`}
                                            className="rounded-md text-indigo-600 hover:bg-indigo-50"
                                        >
                                            <PencilIcon className="h-5 w-5" />
                                        </Link>

                                        <button
                                            type="button"
                                            onClick={() => requestDeleteUser(user.id)}
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