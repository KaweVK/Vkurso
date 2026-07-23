export interface Lesson {
    id: number;
    title: string;
    description: string;
    orderIndex: number;
    videoUrl: string;
    lessons: Lesson[];
    freePreview: boolean;
    moduleId: number;
}