package com.kawevk.vkurso.courseCategory;

import com.kawevk.vkurso.courseCategory.dtos.CourseCategoryResponse;
import com.kawevk.vkurso.courseCategory.dtos.CreateCourseCategoryRequest;
import com.kawevk.vkurso.courseCategory.dtos.UpdateCourseCategoryRequest;
import com.kawevk.vkurso.courseCategory.exceptions.CourseCategoryRequestNotAllowed;
import com.kawevk.vkurso.user.Role;
import com.kawevk.vkurso.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseCategoryService {

    private final CourseCategoryRepository repository;

    public CourseCategoryService(CourseCategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<CourseCategoryResponse> list(Pageable pageable) {
        return repository.findAll(pageable).map(CourseCategoryResponse::from);
    }

    @Transactional(readOnly = true)
    public CourseCategoryResponse get(Long id) {
        return CourseCategoryResponse.from(getCourseCategoryOrThrow(id));
    }

    @Transactional
    public CourseCategoryResponse create(CreateCourseCategoryRequest courseCategory) {
        CourseCategory courseCategoryEntity = new CourseCategory(
                courseCategory.name()
        );
        return CourseCategoryResponse.from(repository.save(courseCategoryEntity));
    }

    @Transactional
    public CourseCategoryResponse update(UpdateCourseCategoryRequest courseCategory, Long id, User user) {
        CourseCategory courseCategoryEntity = getCourseCategoryOrThrow(id);

        ensureCanModify(user);

        courseCategoryEntity.setName(courseCategory.name());

        return CourseCategoryResponse.from(repository.save(courseCategoryEntity));
    }

    @Transactional
    public void delete(Long id, User user) {
        ensureCanModify(user);
        repository.deleteById(id);
    }

    private CourseCategory getCourseCategoryOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course category not found"));
    }

    private void ensureCanModify(User user) {
        boolean isAdmin = user.getRole() == Role.ADMIN;
        boolean isInstructor = user.getRole() == Role.INSTRUCTOR;
        if (!isAdmin && !isInstructor) {
            throw new CourseCategoryRequestNotAllowed();
        }
    }
}
