import type { Module } from './module'
import type { Category } from './category'
import type { User } from './user'

export type CourseLevel = "BEGINNER" | "INTERMEDIATE" | "ADVANCED";

export type CourseStatus = "DRAFT" | "PUBLISHED" | "ARCHIVED";

export interface Course {
  id: number;
  title: string;
  slug: string;
  description: string;
  price: number;
  level: CourseLevel;
  categories: Category[];
  status: CourseStatus;
  instructor: User;
  modules: Module[];
}