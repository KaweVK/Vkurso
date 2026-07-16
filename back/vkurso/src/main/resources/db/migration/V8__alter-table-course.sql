-- =====================================================================
-- V8__alter-table-course.sql
-- Adiciona a coluna "price" à tabela "courses" com tipo DECIMAL(10, 2).
-- =====================================================================
ALTER TABLE courses ADD COLUMN price DECIMAL(10, 2);
