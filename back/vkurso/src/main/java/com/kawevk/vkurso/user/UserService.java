package com.kawevk.vkurso.user;

import com.kawevk.vkurso.course.CourseService;
import com.kawevk.vkurso.user.dtos.CreateUserRequest;
import com.kawevk.vkurso.user.dtos.UpdateUserRequest;
import com.kawevk.vkurso.user.dtos.UserResponse;
import com.kawevk.vkurso.user.exceptions.UserNotCreatedException;
import com.kawevk.vkurso.user.exceptions.UserNotFoundException;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
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
    public UserResponse update(Long userId, UpdateUserRequest request) {
        User user = getUserOrThrow(userId);
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.passwordHash()));
        user.setRole(request.role());
        return UserResponse.from(repository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        User user = getUserOrThrow(id);
        courseService.deleteByInstructor(user.getId());
        repository.delete(user);
    }

    @Override
    @NullMarked
    public UserDetails loadUserByUsername(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotCreatedException(email));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPasswordHash())
                .authorities("ROLE_" + user.getRole().name())
                .build();
    }

    private User getUserOrThrow(Long userId) {
        return repository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }
}
