package com.kawevk.vkurso.enrollment.unit;

import com.kawevk.vkurso.enrollment.Enrollment;
import com.kawevk.vkurso.enrollment.EnrollmentStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class EnrollmentTest {
    @Nested
    @DisplayName("Create cancel and reactive enroll")
    class Enroll {
        @Test
        @DisplayName("Enroll")
        void createEnroll() {
            Enrollment enrollment = new Enrollment(1L, 1L);

            Assertions.assertEquals(EnrollmentStatus.ACTIVE, enrollment.getStatus());
            assertNotNull(enrollment.getEnrolledAt());
            assertNull(enrollment.getCancelledAt());
        }

        @Test
        @DisplayName("Cancel enroll")
        void cancel() {
            Enrollment enrollment = new Enrollment(1L, 1L);

            enrollment.cancel();

            assertEquals(EnrollmentStatus.CANCELLED, enrollment.getStatus());
            assertNotNull(enrollment.getCancelledAt());
        }

        @Test
        @DisplayName("Reactive enroll")
        void reactive() {
            Enrollment enrollment = new Enrollment(1L, 1L);

            enrollment.cancel();
            enrollment.reactivate();

            assertEquals(EnrollmentStatus.ACTIVE, enrollment.getStatus());
            assertNotNull(enrollment.getEnrolledAt());
            assertNull(enrollment.getCancelledAt());
        }
    }

    @Nested
    @DisplayName("Verifica se está ativo")
    class active{
        @Test
        @DisplayName("Ativo")
        void isActive() {
            Enrollment enrollment = new Enrollment(1L, 1L);

            assertTrue(enrollment.isActive());
        }

        @Test
        @DisplayName("Inativo")
        void isInactive() {
            Enrollment enrollment = new Enrollment(1L, 1L);
            enrollment.cancel();

            assertFalse(enrollment.isActive());
        }
    }
}
