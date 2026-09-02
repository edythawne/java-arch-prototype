package edy.app.sgc.simple;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author edythawne
 * @created 31/08/2026 10:19
 * @project ut_sgc
 */
@RestController
@RequestMapping("api/simple/user")
public class UserBasicController {

    private final JdbcTemplate connection;

    public UserBasicController(JdbcTemplate connection){
        this.connection = connection;
    }

    @GetMapping("/get/all")
    public ResponseEntity<Object> getAll(){
        try {
            String sql = """
                SELECT 
                    u.id,
                    u.student_number,
                    u.first_name AS nombre, 
                    u.paternal_last_name AS apellido_paterno, 
                    u.maternal_last_name AS apellido_materno,
                    u.email AS correo,
                    COALESCE(mr.blood_type, 'Sin registrar') AS tipo_sangre,
                    COALESCE(mr.emergency_contact_name, 'Sin registrar') AS contacto_emergencia,
                    COALESCE(mr.emergency_contact_phone, 'Sin registrar') AS telefono_emergencia
                FROM user u
                INNER JOIN user_has_role uhr ON u.id = uhr.user_id
                INNER JOIN role r ON uhr.role_id = r.id
                LEFT JOIN medical_record mr ON u.id = mr.student_id
                WHERE r.name = 'ESTUDIANTE' AND u.is_active = TRUE
            """;

            List<Map<String, Object>> students = this.connection.queryForList(sql);

            return new ResponseEntity<>(students, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al consultar estudiantes: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/by/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
        try {
            String sql = """
                SELECT 
                    u.id,
                    u.student_number,
                    CONCAT(u.first_name, ' ', u.paternal_last_name, ' ', COALESCE(u.maternal_last_name, '')) AS nombre_completo,
                    u.email,
                    u.classroom_location AS aula_habitual,
                    u.is_active AS estado_cuenta,
                    u.created_at AS fecha_registro,
                    COALESCE(mr.blood_type, 'Sin registrar') AS tipo_sangre,
                    COALESCE(mr.allergies, 'Sin alergias registradas') AS alergias,
                    COALESCE(mr.chronic_conditions, 'Sin condiciones crónicas') AS condiciones_cronicas,
                    COALESCE(mr.emergency_contact_name, 'Sin registrar') AS contacto_emergencia,
                    COALESCE(mr.emergency_contact_phone, 'Sin registrar') AS telefono_emergencia
                FROM user u
                INNER JOIN user_has_role uhr ON u.id = uhr.user_id
                INNER JOIN role r ON uhr.role_id = r.id
                LEFT JOIN medical_record mr ON u.id = mr.student_id
                WHERE u.id = ? AND r.name = 'ESTUDIANTE'
            """;

            List<Map<String, Object>> result = this.connection.queryForList(sql, id);

            if (result.isEmpty()) {
                return new ResponseEntity<>("El estudiante con el ID proporcionado no existe.", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(result.get(0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al obtener los datos del estudiante: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/change/visibility/{id}")
    public ResponseEntity<Object> toggleStatus(@PathVariable("id") Long id, @RequestParam("active") boolean active) {
        try {
            String sql = """
                UPDATE user u
                INNER JOIN user_has_role uhr ON u.id = uhr.user_id
                INNER JOIN role r ON uhr.role_id = r.id
                SET u.is_active = ?
                WHERE u.id = ? AND r.name = 'ESTUDIANTE'
            """;

            int rowAffected = this.connection.update(sql, active, id);

            if (rowAffected == 0) {
                return new ResponseEntity<>("No se pudo actualizar. El ID no pertenece a ningún estudiante.", HttpStatus.NOT_FOUND);
            }

            String message = active ? "Estudiante activado correctamente." : "Estudiante desactivado correctamente.";
            return new ResponseEntity<>(Map.of("message", message, "student_id", id, "is_active", active), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al cambiar el estatus del estudiante: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
