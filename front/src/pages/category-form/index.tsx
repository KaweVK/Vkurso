import { useNavigate, useParams } from "react-router-dom";
import { FieldLabel, FormShell, inputClassName } from "../../components/form";
import { useEffect, useState } from "react";
import CategoryService from "../../services/categoryService";

function CategoryForm() {
    const { categoryId } = useParams();
    const editing = Boolean(categoryId);
    const navigate = useNavigate();
    const [loading, setLoading] = useState<boolean>(editing)
    const [name, setName] = useState('');
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState('');

    useEffect(() => {
        if (!editing) return;
        CategoryService.get(Number(categoryId), ).then((category) => {
            setName(category.name);
        }).catch(() => setError('Não foi possível carregar a Categoria'))
            .finally(() => setLoading(false));
    }, [editing, categoryId])

    async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault();
        setError('');
        setSaving(true);
        try {
            const data = { name };
            if(editing) {
                await CategoryService.update(Number(categoryId), data) 
            } else {
                await CategoryService.create(data)
            }
            navigate(`/admin/categories`);
        } catch {
            setError('Não foi possível salvar a categoria. Confira os dados e tente novamente.');
        } finally { setSaving(false); }
    }

    if (loading) return (
        <FormShell
            title="Carregando categoria"
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
            title={editing ? "Editar Categoria" : "Salvar Curso"}
            description="Modifique as informações do usuário"
            onSubmit={handleSubmit}
            onCancel={() => navigate(-1)}
            loading={saving}
            error={error}   
            submitLabel={'Salvar alterações'}
        >
            <div>
                <FieldLabel htmlFor="name">Nome</FieldLabel>
                <input
                    id="name"
                    required
                    className={inputClassName}
                    value={name}
                    onChange={(event) => setName(event.target.value)}
                />
            </div>
        </FormShell>
    );
}

export default CategoryForm;