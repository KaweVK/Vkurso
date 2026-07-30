import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { FieldLabel, FormShell, inputClassName } from '../../components/form';
import courseService from '../../services/courseService';
import type { CourseLevel } from '../../types/course';

function CourseForm() {
    const { courseId } = useParams();
    const navigate = useNavigate();
    const editing = Boolean(courseId);
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [level, setLevel] = useState<CourseLevel>('BEGINNER');
    const [price, setPrice] = useState('0');
    const [loading, setLoading] = useState(editing);
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState('');

    useEffect(() => {
        if (!courseId) return;
        courseService.findById(Number(courseId)).then((course) => {
            setTitle(course.title);
            setDescription(course.description ?? '');
            setLevel(course.level);
            setPrice(String(course.price));
        }).catch(() => setError('Não foi possível carregar o curso.')).finally(() => setLoading(false));
    }, [courseId]);

    async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault();
        setError('');
        setSaving(true);
        try {
            const data = { title, description, level, price: Number(price) };
            const course = editing ? await courseService.update(Number(courseId), data) : await courseService.create(data);
            navigate(`/course/${course.slug}`);
        } catch {
            setError('Não foi possível salvar o curso. Confira os dados e tente novamente.');
        } finally { setSaving(false); }
    }

    if (loading) return <FormShell title="Carregando curso" description="" onSubmit={(event) => event.preventDefault()} onCancel={() => navigate(-1)} loading error="" submitLabel="Salvar"><></></FormShell>;

    return <FormShell title={editing ? 'Editar curso' : 'Criar curso'} description="Apresente o curso com informações claras para seus alunos." onSubmit={handleSubmit} onCancel={() => navigate(-1)} loading={saving} error={error} submitLabel={editing ? 'Salvar alterações' : 'Criar curso'}>
        <div>
            <FieldLabel htmlFor="title">Título</FieldLabel><input id="title" required maxLength={150} className={inputClassName} value={title} onChange={(event) => setTitle(event.target.value)} />
        </div>
        <div>
            <FieldLabel htmlFor="description">Descrição</FieldLabel>
            <textarea id="description" rows={5} className={inputClassName} value={description} onChange={(event) => setDescription(event.target.value)} />
        </div>
        <div className="grid gap-5 sm:grid-cols-2">
            <div>
                <FieldLabel htmlFor="level">Nível</FieldLabel><select id="level" className={inputClassName} value={level} onChange={(event) => setLevel(event.target.value as CourseLevel)}>
                    <option value="BEGINNER">Iniciante</option>
                    <option value="INTERMEDIATE">Intermediário</option>
                    <option value="ADVANCED">Avançado</option>
                </select>
            </div>
            <div>
                <FieldLabel htmlFor="price">Preço</FieldLabel>
                <input id="price" required min="0" step="0.01" type="number" className={inputClassName} value={price} onChange={(event) => setPrice(event.target.value)} />
            </div>
        </div>
    </FormShell>;
}

export default CourseForm;
