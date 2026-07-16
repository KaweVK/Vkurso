-- =====================================================================
-- V10__create-table-course-categories.sql
-- Tabela de junção entre cursos e categorias.
-- =====================================================================
CREATE TABLE course_categories (
   course_id BIGINT NOT NULL,
   category_id BIGINT NOT NULL,

   CONSTRAINT pk_course_categories PRIMARY KEY (course_id, category_id),
   CONSTRAINT fk_course_categories_course
       FOREIGN KEY (course_id) REFERENCES courses (id)
);

CREATE INDEX idx_course_categories_category_id ON course_categories (category_id);