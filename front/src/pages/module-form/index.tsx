import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { FieldLabel, FormShell, inputClassName } from '../../components/form';
import courseService from '../../services/courseService';
import moduleService from '../../services/moduleService';

function ModuleForm() {
    const { courseId, moduleId } = useParams();
    const navigate = useNavigate();
    const editing = Boolean(moduleId);
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [orderIndex, setOrderIndex] = useState('0');
    const [loading, setLoading] = useState(editing);
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState('');

    useEffect(() => {
        if (!courseId || !moduleId) return;
        moduleService.findById(Number(courseId), Number(moduleId)).then((module) => {
            setTitle(module.title);
            setDescription(module.description ?? '');
            setOrderIndex(String(module.orderIndex));
        }).catch(() => setError('Não foi possível carregar o módulo.')).finally(() => setLoading(false));
    }, [courseId, moduleId]);

    async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault();
        setError('');
        setSaving(true);
        try {
            const course = await courseService.findById(Number(courseId));
            const data = { title, description, orderIndex: Number(orderIndex) };
            if (editing) await moduleService.update(Number(courseId), Number(moduleId), data);
            else await moduleService.create(Number(courseId), data);
            navigate(`/course/${course.slug}`);
        } catch { setError('Não foi possível salvar o módulo. Confira os dados e tente novamente.'); }
        finally { setSaving(false); }
    }

    if (loading) return <FormShell title="Carregando módulo" description="" onSubmit={(event) => event.preventDefault()} onCancel={() => navigate(-1)} loading error="" submitLabel="Salvar"><></></FormShell>;

    return <FormShell title={editing ? 'Editar módulo' : 'Criar módulo'} description="Organize o conteúdo do curso em uma sequência fácil de acompanhar." onSubmit={handleSubmit} onCancel={() => navigate(-1)} loading={saving} error={error} submitLabel={editing ? 'Salvar alterações' : 'Criar módulo'}>
        <div><FieldLabel htmlFor="title">Título</FieldLabel><input id="title" required maxLength={150} className={inputClassName} value={title} onChange={(event) => setTitle(event.target.value)} /></div>
        <div><FieldLabel htmlFor="description">Descrição</FieldLabel><textarea id="description" rows={5} className={inputClassName} value={description} onChange={(event) => setDescription(event.target.value)} /></div>
        <div><FieldLabel htmlFor="orderIndex">Ordem</FieldLabel><input id="orderIndex" required min="0" type="number" className={inputClassName} value={orderIndex} onChange={(event) => setOrderIndex(event.target.value)} /></div>
    </FormShell>;
}

export default ModuleForm;
