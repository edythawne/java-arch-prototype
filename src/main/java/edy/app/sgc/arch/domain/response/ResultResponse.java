package edy.app.sgc.arch.domain.response;

import lombok.*;
import org.springframework.http.HttpStatus;

/**
 * @author edythawne
 * @created 31/08/2026 11:18
 * @project ut_sgc
 */
@Data
@AllArgsConstructor
public class ResultResponse<TResponse> {

    private HttpStatus code = HttpStatus.BAD_REQUEST;

    private String message;

    private TResponse data;

}
