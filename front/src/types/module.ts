import type { Course } from "./course";

export interface Module {
  id: number;
  title: string;
  description: string;
  orderIndex: number;
  course: Course;
  lessons: Lesson[];
}
