package com.kawevk.vkurso.course.exceptions;

public class CourseWithoutModulesException extends RuntimeException {
    public CourseWithoutModulesException(String msg) {
        super(msg);
    }
}
