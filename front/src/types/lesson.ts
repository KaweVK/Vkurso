export interface Lesson {
    id: number;
    title: string;
    description: string;
    orderIndex: number;
    videoUrl: string | null;
    durationSeconds: number,
    lessons: Lesson[];
    freePreview: boolean;
    moduleId: number;
}