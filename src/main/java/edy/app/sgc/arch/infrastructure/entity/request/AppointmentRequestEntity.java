package edy.app.sgc.arch.infrastructure.entity.request;

import edy.app.sgc.arch.infrastructure.entity.BaseEntity;
import edy.app.sgc.arch.infrastructure.entity.catalog.AppointmentStatusEntity;
import edy.app.sgc.arch.infrastructure.entity.catalog.AppointmentTypeEntity;
import edy.app.sgc.arch.infrastructure.entity.user.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @author edythawne
 * @created 31/08/2026 11:31
 * @project ut_sgc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "appointment_request")
public class AppointmentRequestEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Relación con el estudiante (NOT NULL)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private UserEntity student;

    // Relación con el proveedor de salud (NULLABLE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "health_provider_id", nullable = true)
    private UserEntity healthProvider;

    // Relación con el tipo de cita (NOT NULL)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_type_id", nullable = false)
    private AppointmentTypeEntity appointmentType;

    // Relación con el estatus de la cita (NOT NULL)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_status_id", nullable = false)
    private AppointmentStatusEntity appointmentStatus;

    @Column(name = "requested_date")
    private LocalDateTime requestedDate;

    @Column(name = "classroom_location", length = 100)
    private String classroomLocation;

    @Column(name = "location_details", length = 100)
    private String locationDetails;

    @Column(name = "reason", nullable = false, columnDefinition = "TEXT")
    private String reason;

}