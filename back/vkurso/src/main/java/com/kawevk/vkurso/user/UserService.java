package com.kawevk.vkurso.user;

import com.kawevk.vkurso.course.CourseService;
import com.kawevk.vkurso.course.exceptions.CourseRequestNotAllowed;
import com.kawevk.vkurso.email.EmailService;
import com.kawevk.vkurso.email.EmailVerificationService;
import com.kawevk.vkurso.email.dtos.VerifyEmailRequest;
import com.kawevk.vkurso.email.exceptions.EmailAlreadyExistsException;
import com.kawevk.vkurso.email.exceptions.EmailAlreadyVerifiedException;
import com.kawevk.vkurso.email.exceptions.EmailNotVerifiedException;
import com.kawevk.vkurso.email.exceptions.InvalidVerificationCodeException;
import com.kawevk.vkurso.user.dtos.CreateUserRequest;
import com.kawevk.vkurso.user.dtos.UpdateUserRequest;
import com.kawevk.vkurso.user.dtos.UserResponse;
import com.kawevk.vkurso.user.exceptions.UserNotCreatedWithEmailException;
import com.kawevk.vkurso.user.exceptions.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final CourseService courseService;
    private final EmailService emailService;
    private final EmailVerificationService emailVerificationService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, CourseService courseService, EmailService emailService, EmailVerificationService emailVerificationService, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.courseService = courseService;
        this.emailService = emailService;
        this.emailVerificationService = emailVerificationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> list(Pageable pageable, User userLoged) {
        if (userLoged.getRole() != Role.ADMIN) {
            throw new CourseRequestNotAllowed();
        }
        return repository.findAll(pageable).map(UserResponse::from);
    }

    @Transactional(readOnly = true)
    public UserResponse get(Long userId, User userLoged) {
        ensureCanModify(getUserOrThrow(userId), userLoged);
        return UserResponse.from(getUserOrThrow(userId));
    }

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        User user = new User(
                request.fullName(),
                request.email(),
                passwordEncoder.encode(request.password()),
                Role.USER
        );

        String code = emailVerificationService.generateCode();

        emailVerificationService.saveCode(
                user.getEmail(),
                code
        );

        emailService.sendVerificationCode(
                user.getEmail(),
                code
        );

        repository.save(user);

        return UserResponse.from(user);
    }

    @Transactional
    public void verifyEmail(VerifyEmailRequest request) {
        User user = repository
                .findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException(request.email()));

        if (user.isEmailVerified()) {
            throw new EmailAlreadyVerifiedException(request.email());
        }

        if (emailVerificationService.hasExceededAttempts(request.email())) {
            throw new VerificationCodeExceededAttemptsException();
        }

        String savedCode =
                emailVerificationService.getCode(request.email());

        if (savedCode == null) {
            throw new VerificationCodeExpiredException();
        }

        if (!savedCode.equals(request.code())) {
            int attempts =
                    emailVerificationService.incrementAttempts(
                            request.email()
                    );

            if (attempts >= 5) {
                emailVerificationService.deleteCode(request.email());
                throw new VerificationCodeExceededAttemptsException();
            }

            throw new InvalidVerificationCodeException(request.code());
        }

        user.setEmailVerified(true);

        repository.save(user);

        emailVerificationService.deleteCode(request.email());
    }

    @Transactional
    public void resendVerification(String email) {

        User user = repository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if (user.isEmailVerified()) {
            throw new EmailAlreadyVerifiedException(email);
        }

        if (!emailVerificationService.canResend(email)) {
            throw new VerificationCodeCantResendException();
        }

        String code =
                emailVerificationService.generateCode();

        emailVerificationService.saveCode(
                email,
                code
        );

        emailService.sendVerificationCode(
                email,
                code
        );
    }

    @Transactional
    @CacheEvict(value = "courses", allEntries = true)
    public UserResponse update(Long userId, UpdateUserRequest request, User userLoged) {
        User user = getUserOrThrow(userId);
        ensureCanModify(user, userLoged);

        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        if (userLoged.getRole() == Role.ADMIN) {
            user.setRole(request.role());
        }

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
                .orElseThrow(() -> {
                    log.warn("User not found with email: {}", email);
                    return new UserNotCreatedWithEmailException(email);
                });

        if (!user.isEmailVerified()) {
            throw new EmailNotVerifiedException(email);
        }

        return user;
    }

    private User getUserOrThrow(Long userId) {
        return repository.findById(userId).orElseThrow(() -> {
            log.warn("User not found! {}", userId);
            return new UserNotFoundException(userId);
        });
    }

    private void ensureCanModify(User targetUser, User userLoged) {
        boolean isAdmin = userLoged.getRole() == Role.ADMIN;
        boolean isOwner = targetUser.getId().equals(userLoged.getId());
        if (!isAdmin && !isOwner) {
            log.warn("User {} attempted to modify user {} without permission", userLoged.getId(), targetUser.getId());
            throw new CourseRequestNotAllowed();
        }
    }
}
