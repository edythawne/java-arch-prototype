package edy.app.sgc.arch.infrastructure.entity.user;

import edy.app.sgc.arch.infrastructure.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author edythawne
 * @created 31/08/2026 11:30
 * @project ut_sgc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "paternal_last_name", nullable = false, length = 100)
    private String paternalLastName;

    @Column(name = "maternal_last_name", length = 100)
    private String maternalLastName;

    @Column(name = "student_number", unique = true, length = 20)
    private String studentNumber;

    @Column(name = "classroom_location", length = 50)
    private String classroomLocation;

    // Relación ORM: Mapea directamente el expediente médico embebido
    @OneToOne
    @JoinColumn(name = "id", referencedColumnName = "student_id", insertable = false, updatable = false)
    private MedicalRecordEntity medicalRecord;

}