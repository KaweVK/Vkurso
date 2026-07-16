import type { Course } from "./course";
import type { Lesson } from "./lesson"
 
export interface Module {
  id: number;
  title: string;
  description: string;
  orderIndex: number;
  course: Course;
  lessons: Lesson[];
}
