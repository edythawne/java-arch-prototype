package edy.app.sgc.arch.domain.response.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author edythawne
 * @created 31/08/2026 11:30
 * @project ut_sgc
 */
@Data
public class MedicalRecordResponse {

    @JsonProperty("tipo_sangre")
    private String bloodType;

    @JsonProperty("alergias")
    private String allergies;

    @JsonProperty("condicion_cronica")
    private String chronicConditions;

    @JsonProperty("contacto_emergencia")
    private String emergencyContactName;

    @JsonProperty("numero_emergencia")
    private String emergencyContactPhone;

}