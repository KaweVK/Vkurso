package com.kawevk.vkurso.shared.persistence;

import com.kawevk.vkurso.course.Course;
import com.kawevk.vkurso.course.exceptions.CourseRequestNotAllowed;
import com.kawevk.vkurso.user.Role;
import com.kawevk.vkurso.user.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Modifiable {
    protected void ensureCanModify(Course course, User user) {
        boolean isAdmin = user.getRole() == Role.ADMIN;
        boolean isOwner = course.getInstructorId().equals(user.getId());
        if (!isAdmin && !isOwner) {
            log.warn("User {} attempted to modify course {} without permission", user.getId(), course.getId());
            throw new CourseRequestNotAllowed();
        }
    }
}
