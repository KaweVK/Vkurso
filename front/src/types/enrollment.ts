export type enrollmentStatus = "ACTIVE" | "CANCELLED";

export interface Enrollment {
    id: number;
    studentId: number;
    courseId: number;
    enrollmentStatus: enrollmentStatus;
}