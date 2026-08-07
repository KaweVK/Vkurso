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

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            CourseNotFoundException.class, ModuleNotFoundException.class,
            LessonNotFoundException.class, LessonWithoutVideoException.class,
            UserNotFoundException.class, UserNotCreatedWithEmailException.class,
            NotEnrolledException.class
    })
    public ProblemDetail handleNotFound(RuntimeException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setType(URI.create("https://api.vkurso.com/errors/not-found"));
        problem.setTitle("Recurso não encontrado");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler({DuplicateSlugException.class, AlreadyEnrolledException.class})
    public ProblemDetail handleConflict(RuntimeException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setType(URI.create("https://api.vkurso.com/errors/conflict"));
        problem.setTitle("Conflito de estado");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(CourseNotPublishedException.class)
    public ProblemDetail handleCourseNotPublished(CourseNotPublishedException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage());
        problem.setType(URI.create("https://api.vkurso.com/errors/course-not-published"));
        problem.setTitle("Curso não publicado");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler({VideoAccessDeniedException.class, CourseRequestNotAllowed.class})
    public ProblemDetail handleForbidden(RuntimeException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
        problem.setType(URI.create("https://api.vkurso.com/errors/forbidden"));
        problem.setTitle("Requisição não permitida");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthentication(AuthenticationException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        problem.setType(URI.create("https://api.vkurso.com/errors/unauthorized"));
        problem.setTitle("Não autorizado");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    // Storage (AWS S3) Exceptions
    @ExceptionHandler(StorageException.class)
    public ProblemDetail handleStorageException(StorageException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um problema inesperado ao acessar o serviço de armazenamento."
        );
        problem.setType(URI.create("https://api.vkurso.com/errors/storage-failure"));
        problem.setTitle("Falha no Armazenamento de Arquivos");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    // Erros de estado/regras de negócio
    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalState(IllegalStateException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage()
        );
        problem.setType(URI.create("https://api.vkurso.com/errors/illegal-state"));
        problem.setTitle("Estado inválido para a operação");
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}