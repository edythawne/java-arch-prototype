package edy.app.sgc.arch.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author edythawne
 * @created 31/08/2026 18:01
 * @project ut_sgc
 */
@Data
@NoArgsConstructor
@SuperBuilder
public abstract class BaseResponse {

    @JsonProperty("esta_activo")
    private Boolean isActive;

}
