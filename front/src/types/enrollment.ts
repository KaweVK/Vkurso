export type Status = "ACTIVE" | "CANCELLED";

export interface Enrollment {
    id: number;
    studentId: number;
    courseId: number;
    enrollmentStatus: Status;
}