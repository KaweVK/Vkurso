import { useNavigate, useParams } from "react-router-dom";
import { FieldLabel, FormShell, inputClassName } from "../../components/form";
import { useEffect, useState } from "react";
import UserService from "../../services/userService";
import type { Role } from "../../types";

function UserForm() {
    const { userId } = useParams();
    const navigate = useNavigate();
    const [loading, setLoading] = useState<boolean>(true)
    const [fullName, setFullName] = useState('');
    const [email, setEmail] = useState('');
    const [role, setRole] = useState<Role>('USER');
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState('');

    useEffect(() => {
        UserService.findById(Number(userId)).then((user) => {
            setFullName(user.fullName);
            setEmail(user.email);
            setRole(user.role);
        }).catch(() => setError('Não foi possível carregar o curso'))
            .finally(() => setLoading(false));
    }, [userId])

    async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault();
        setError('');
        setSaving(true);
        try {
            const data = { fullName, email, role };
            await UserService.update(Number(userId), data);
            navigate(`/admin/users`);
        } catch {
            setError('Não foi possível salvar o curso. Confira os dados e tente novamente.');
        } finally { setSaving(false); }
    }

    if (loading) return (
        <FormShell
            title="Carregando curso"
            description=""
            onSubmit={(event) => event.preventDefault()}
            onCancel={() => navigate(-1)}
            loading
            error=""
            submitLabel="Salvar">
            <></>
        </FormShell>
    );

    return (
        <FormShell
            title="Editar Usuário"
            description="Modifique as informações do usuário"
            onSubmit={handleSubmit}
            onCancel={() => navigate(-1)}
            loading={saving}
            error={error}   
            submitLabel={'Salvar alterações'}
        >
            <div>
                <FieldLabel htmlFor="title">Título</FieldLabel>
                <input
                    id="fullname"
                    required
                    maxLength={150}
                    className={inputClassName}
                    value={fullName}
                    onChange={(event) => setFullName(event.target.value)}
                />
            </div>
            <div>
                <FieldLabel htmlFor="description">Descrição</FieldLabel>
                <input
                    id="email"
                    required
                    className={inputClassName}
                    value={email}
                    onChange={(event) => setEmail(event.target.value)}
                />
            </div>
            <div className="grid gap-5 sm:grid-cols-2">
                <div>
                    <FieldLabel htmlFor="level">Nível</FieldLabel>
                    <select
                        id="role"
                        className={inputClassName}
                        value={role}
                        onChange={(event) => setRole(event.target.value as Role)}
                    >
                        <option value="USER">Aluno</option>
                        <option value="INSTRUCTOR">Instrutor</option>
                        <option value="ADMIN">Administrador</option>
                    </select>
                </div>
            </div>
        </FormShell>
    );
}

export default UserForm;