package com.kawevk.vkurso.user;

import com.kawevk.vkurso.course.CourseService;
import com.kawevk.vkurso.user.dtos.CreateUserRequest;
import com.kawevk.vkurso.user.dtos.UpdateUserRequest;
import com.kawevk.vkurso.user.dtos.UserResponse;
import com.kawevk.vkurso.user.exceptions.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository repository;
    private final CourseService courseService;

    public UserService(UserRepository repository, CourseService courseService) {
        this.repository = repository;
        this.courseService = courseService;
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> list(Pageable pageable) {
        return repository.findAll(pageable).map(UserResponse::from);
    }

    @Transactional(readOnly = true)
    public UserResponse get(Long userId) {
        return UserResponse.from(getUserOrThrow(userId));
    }

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(Long userId) {
        return UserResponse.from(getUserOrThrow(userId));
    }

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        User user = new User(
                request.fullName(),
                request.email(),
                request.password(),
                request.role()
        );
        return UserResponse.from(repository.save(user));
    }

    @Transactional
    public UserResponse update(Long userId, UpdateUserRequest request) {
        User user = getUserOrThrow(userId);
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPasswordHash(request.passwordHash());
        user.setRole(request.role());
        return UserResponse.from(repository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        User user = getUserOrThrow(id);
        courseService.deleteByInstructor(user.getId());
        repository.delete(user);
    }

    private User getUserOrThrow(Long userId) {
        return repository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }
}
