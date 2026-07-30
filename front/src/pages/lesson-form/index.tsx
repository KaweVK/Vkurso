import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { FieldLabel, FormShell, inputClassName } from '../../components/form';
import courseService from '../../services/courseService';
import lessonService from '../../services/lessonService';

function LessonForm() {
    const { courseId, moduleId, lessonId } = useParams();
    const navigate = useNavigate();
    const editing = Boolean(lessonId);
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [orderIndex, setOrderIndex] = useState('0');
    const [video, setVideo] = useState<File>();
    const [loading, setLoading] = useState(editing);
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState('');

    useEffect(() => {
        if (!courseId || !moduleId || !lessonId) return;
        lessonService.findById(Number(courseId), Number(moduleId), Number(lessonId)).then((lesson) => {
            setTitle(lesson.title);
            setDescription(lesson.description ?? '');
            setOrderIndex(String(lesson.orderIndex));
        }).catch(() => setError('Não foi possível carregar a aula.')).finally(() => setLoading(false));
    }, [courseId, moduleId, lessonId]);

    async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault();
        setError('');
        setSaving(true);
        try {
            const course = await courseService.findById(Number(courseId));
            const data = { title, description, orderIndex: Number(orderIndex) };
            const lesson = editing
                ? await lessonService.update(Number(courseId), Number(moduleId), Number(lessonId), data)
                : await lessonService.create(Number(courseId), Number(moduleId), data);
            if (video) await lessonService.uploadVideo(Number(courseId), Number(moduleId), lesson.id, video);
            navigate(`/course/${course.slug}`);
        } catch { setError('Não foi possível salvar a aula. Confira os dados e tente novamente.'); }
        finally { setSaving(false); }
    }

    if (loading) return <FormShell title="Carregando aula" description="" onSubmit={(event) => event.preventDefault()} onCancel={() => navigate(-1)} loading error="" submitLabel="Salvar"><></></FormShell>;

    return <FormShell title={editing ? 'Editar aula' : 'Criar aula'} description="Adicione uma aula objetiva e, se quiser, seu vídeo." onSubmit={handleSubmit} onCancel={() => navigate(-1)} loading={saving} error={error} submitLabel={editing ? 'Salvar alterações' : 'Criar aula'}>
        <div><FieldLabel htmlFor="title">Título</FieldLabel><input id="title" required maxLength={150} className={inputClassName} value={title} onChange={(event) => setTitle(event.target.value)} /></div>
        <div><FieldLabel htmlFor="description">Descrição</FieldLabel><textarea id="description" rows={5} className={inputClassName} value={description} onChange={(event) => setDescription(event.target.value)} /></div>
        <div><FieldLabel htmlFor="orderIndex">Ordem</FieldLabel><input id="orderIndex" required min="0" type="number" className={inputClassName} value={orderIndex} onChange={(event) => setOrderIndex(event.target.value)} /></div>
        <div><FieldLabel htmlFor="video">Vídeo (opcional)</FieldLabel><input id="video" type="file" accept="video/*" className="mt-2 block w-full rounded-lg border border-indigo-200 p-3 text-sm" onChange={(event) => setVideo(event.target.files?.[0])} /></div>
    </FormShell>;
}

export default LessonForm;
