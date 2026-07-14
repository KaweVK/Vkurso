import type { Module } from './module'

export type CourseLevel = "BEGINNER" | "INTERMEDIATE" | "ADVANCED";

export type CourseStatus = "DRAFT" | "PUBLISHED" | "ARCHIVED";

export interface Course {
  id: number;
  title: string;
  slug: string;
  description: string;
  price: number;
  level: CourseLevel;
  categoryIds: number[];
  status: CourseStatus;
  instructorId: number;
  modules: Module[];
}