export type StatusCourse = "IN_PROGRESS" | "COMPLETED";

export interface CourseProgress {
    courseId: number,
    title: string,
    slug: string,
    description: string,
    completedLessons: number,
    totalLessons: number,
    progressPercentage: number,
    status: StatusCourse
}