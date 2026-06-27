-- =====================================================================
-- V5__alter-table-lessons-and-modules-order-index-type.sql
-- Altera o tipo da coluna "order_index" das tabelas "lessons" e "modules" para BIGINT.
-- =====================================================================
ALTER TABLE lessons ALTER COLUMN order_index TYPE BIGINT;
ALTER TABLE modules ALTER COLUMN order_index TYPE BIGINT;