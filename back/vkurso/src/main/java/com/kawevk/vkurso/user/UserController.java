package com.kawevk.vkurso.user;

import com.kawevk.vkurso.email.dtos.ResendVerificationRequest;
import com.kawevk.vkurso.email.dtos.VerifyEmailRequest;
import com.kawevk.vkurso.user.dtos.CreateUserRequest;
import com.kawevk.vkurso.user.dtos.UpdateUserRequest;
import com.kawevk.vkurso.user.dtos.UserResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> list(Pageable pageable, @AuthenticationPrincipal User user) {
        log.debug("Listing users with pagination: page {}, size {}", pageable.getPageNumber(), pageable.getPageSize());
        return ResponseEntity.ok().body(service.list(pageable, user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(@PathVariable Long id, @AuthenticationPrincipal User user) {
        log.debug("Getting user with id: {}", id);
        return ResponseEntity.ok().body(service.get(id, user));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal User user) {
        log.debug("Getting actual user with id: {}", user.getId());
        return ResponseEntity.ok().body(service.get(user.getId(), user));
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid CreateUserRequest request, UriComponentsBuilder uriBuilder) {
        log.debug("Creating user: {}", request.email());
        UserResponse created = service.create(request);
        log.debug("Created user: {}", created.id());
        URI location = uriBuilder.path("/api/users/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Void> verifyEmail(@RequestBody @Valid VerifyEmailRequest request) {
        log.debug("Verify email: {}", request.email());
        service.verifyEmail(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<Void> resendVerification(@RequestBody ResendVerificationRequest request) {
        log.debug("Resending email to: {}", request.email());
        service.resendVerification(request.email());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest request, @AuthenticationPrincipal User user) {
        log.debug("Updating user with id: {}", id);
        UserResponse userResponse = service.update(id, request, user);
        return ResponseEntity.ok().body(userResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        log.debug("Deleting user with id: {}", id);
        service.delete(id, user);
        return ResponseEntity.noContent().build();
    }
}
