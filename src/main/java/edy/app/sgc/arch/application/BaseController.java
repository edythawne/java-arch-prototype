package edy.app.sgc.arch.application;

import edy.app.sgc.arch.domain.response.ResultResponse;
import org.springframework.http.ResponseEntity;

/**
 * @author edythawne
 * @created 31/08/2026 11:16
 * @project ut_sgc
 */
public abstract class BaseController {

    /**
     * Este metodo permite regresar una respuesta de tipo ResponseEntity al front que realiza la solicitud
     * @param response
     * @return
     * @param <T>
     */
    protected <T> ResponseEntity<Object> toResponse(ResultResponse<T> response) {
        return ResponseEntity.status(response.getCode().value()).body(response);
    }

}
