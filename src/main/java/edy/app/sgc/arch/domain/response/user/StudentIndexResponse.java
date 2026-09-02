package edy.app.sgc.arch.domain.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author edythawne
 * @created 31/08/2026 14:27
 * @project ut_sgc
 */
@Data
public class StudentIndexResponse {

    @JsonProperty("matricula")
    private String studentNumber;

    @JsonProperty("nombre")
    private String name;

    @JsonProperty("apellidos")
    private String lastName;

    @JsonProperty("correo")
    private String email;

    @JsonProperty("tipo_sangre")
    private String bloodType;

    @JsonProperty("contacto_emergencia")
    private String emergencyContactName;

    @JsonProperty("telefono_emergencia")
    private String emergencyContactPhone;

}
