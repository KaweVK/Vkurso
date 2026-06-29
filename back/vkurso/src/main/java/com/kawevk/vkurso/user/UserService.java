package com.kawevk.vkurso.user;

import com.kawevk.vkurso.course.Course;
import com.kawevk.vkurso.course.CourseService;
import com.kawevk.vkurso.course.exceptions.CourseRequestNotAllowed;
import com.kawevk.vkurso.user.dtos.CreateUserRequest;
import com.kawevk.vkurso.user.dtos.UpdateUserRequest;
import com.kawevk.vkurso.user.dtos.UserResponse;
import com.kawevk.vkurso.user.exceptions.UserNotCreatedWithEmailException;
import com.kawevk.vkurso.user.exceptions.UserNotFoundException;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final CourseService courseService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, CourseService courseService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.courseService = courseService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> list(Pageable pageable) {
        return repository.findAll(pageable).map(UserResponse::from);
    }

    @Transactional(readOnly = true)
    public UserResponse get(Long userId) {
        return UserResponse.from(getUserOrThrow(userId));
    }

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        User user = new User(
                request.fullName(),
                request.email(),
                passwordEncoder.encode(request.password()),
                request.role()
        );
        return UserResponse.from(repository.save(user));
    }

    @Transactional
    public UserResponse update(Long userId, UpdateUserRequest request, User userLoged) {
        User user = getUserOrThrow(userId);
        ensureCanModify(user, userLoged);

        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.passwordHash()));
        user.setRole(request.role());

        return UserResponse.from(repository.save(user));
    }

    @Transactional
    public void delete(Long id, User userLoged) {
        User user = getUserOrThrow(id);
        ensureCanModify(user, userLoged);
        courseService.deleteByInstructor(user.getId());
        repository.delete(user);
    }

    @Override
    @NullMarked
    public User loadUserByUsername(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotCreatedWithEmailException(email));

        return user;
    }

    private User getUserOrThrow(Long userId) {
        return repository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    private void ensureCanModify(User targetUser, User userLoged) {
        boolean isAdmin = userLoged.getRole() == Role.ADMIN;
        boolean isOwner = targetUser.getId().equals(userLoged.getId());
        if (!isAdmin && !isOwner) {
            throw new CourseRequestNotAllowed();
        }
    }
}
