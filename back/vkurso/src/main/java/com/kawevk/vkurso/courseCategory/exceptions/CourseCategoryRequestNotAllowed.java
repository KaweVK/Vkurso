package com.kawevk.vkurso.courseCategory.exceptions;

public class CourseCategoryRequestNotAllowed extends RuntimeException {
    public CourseCategoryRequestNotAllowed() {
        super("User is not instructor or ADMIN");
    }
}
