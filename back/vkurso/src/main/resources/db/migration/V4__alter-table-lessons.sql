-- =====================================================================
-- V4__alter-table-lessons.sql
-- Altera a tabela "lessons".
-- =====================================================================
ALTER TABLE lessons RENAME COLUMN video_url TO video_key;
ALTER TABLE lessons ALTER COLUMN video_key DROP NOT NULL;
ALTER TABLE lessons ALTER COLUMN duration_seconds DROP NOT NULL;