package edy.app.sgc.arch.domain.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import edy.app.sgc.arch.domain.response.BaseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author edythawne
 * @created 31/08/2026 11:30
 * @project ut_sgc
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserResponse extends BaseResponse {

    @JsonProperty("correo")
    private String email;

    @JsonProperty("nombre")
    private String firstName;

    @JsonProperty("apellido_paterno")
    private String paternalLastName;

    @JsonProperty("apellido_materno")
    private String maternalLastName;

    @JsonProperty("matricula")
    private String studentNumber;

    @JsonProperty("registro_medico")
    private MedicalRecordResponse medicalRecord;

}