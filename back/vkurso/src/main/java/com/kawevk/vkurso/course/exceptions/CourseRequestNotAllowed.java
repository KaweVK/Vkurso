package com.kawevk.vkurso.course.exceptions;

public class CourseRequestNotAllowed extends RuntimeException {
    public CourseRequestNotAllowed() {
        super("User is not the instructor of this course");
    }
}
