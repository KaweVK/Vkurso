-- =====================================================================
-- V13__alter-table-learning-progress-remove-not-null.sql
-- remove not null para criar progressos assim que houver matricula,
-- mesmo sem aula.
-- =====================================================================
ALTER TABLE learning_progress
    ALTER COLUMN module_id DROP NOT NULL;

ALTER TABLE learning_progress
    ALTER COLUMN lesson_id DROP NOT NULL;