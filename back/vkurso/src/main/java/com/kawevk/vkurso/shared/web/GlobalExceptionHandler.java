package com.kawevk.vkurso.shared.web;

import com.kawevk.vkurso.course.exceptions.CourseNotFoundException;
import com.kawevk.vkurso.course.exceptions.CourseRequestNotAllowed;
import com.kawevk.vkurso.course.exceptions.DuplicateSlugException;
import com.kawevk.vkurso.enrollment.exceptions.AlreadyEnrolledException;
import com.kawevk.vkurso.enrollment.exceptions.CourseNotPublishedException;
import com.kawevk.vkurso.enrollment.exceptions.NotEnrolledException;
import com.kawevk.vkurso.lesson.exceptions.LessonNotFoundException;
import com.kawevk.vkurso.lesson.exceptions.LessonWithoutVideoException;
import com.kawevk.vkurso.lesson.exceptions.VideoAccessDeniedException;
import com.kawevk.vkurso.module.exceptions.ModuleNotFoundException;
import com.kawevk.vkurso.shared.storage.StorageException;
import com.kawevk.vkurso.user.exceptions.UserNotCreatedWithEmailException;
import com.kawevk.vkurso.user.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Courses Exceptions
    @ExceptionHandler(CourseNotFoundException.class)
    public ProblemDetail handleNotFound(CourseNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DuplicateSlugException.class)
    public ProblemDetail handleDuplicate(DuplicateSlugException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(CourseNotPublishedException.class)
    public ProblemDetail handleCourseNotPublished(CourseNotPublishedException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage());
    }

    @ExceptionHandler(CourseRequestNotAllowed.class)
    public ProblemDetail handleCourseRequestNotAllowed(CourseRequestNotAllowed ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    //Modules Exceptions
    @ExceptionHandler(ModuleNotFoundException.class)
    public ProblemDetail handleModuleNotFound(ModuleNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    //Lessons Exceptions
    @ExceptionHandler(LessonNotFoundException.class)
    public ProblemDetail handleLessonNotFound(LessonNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(VideoAccessDeniedException.class)
    public ProblemDetail handleVideoAccessDenied(VideoAccessDeniedException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(LessonWithoutVideoException.class)
    public ProblemDetail handleLessonWithoutVideo(LessonWithoutVideoException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    //Users Exceptions
    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFound(UserNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(UserNotCreatedWithEmailException.class)
    public ProblemDetail handleUserNotCreatedWithEmail(UserNotCreatedWithEmailException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    //Login
    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    //Storage Exceptions
    @ExceptionHandler(StorageException.class)
    public ProblemDetail handleStorageException(StorageException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    //Enrollment Exceptions
    @ExceptionHandler(AlreadyEnrolledException.class)
    public ProblemDetail handleAlreadyEnrolled(AlreadyEnrolledException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(NotEnrolledException.class)
    public ProblemDetail handleNotEnrolled(NotEnrolledException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    //Others Exceptions
    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalState(IllegalStateException ex) {
        // ex.: publicar curso sem módulos
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage());
    }
}