package edy.app.sgc.arch.domain.usecase.user;

import edy.app.sgc.arch.AppConstant;
import edy.app.sgc.arch.domain.response.ResultResponse;
import edy.app.sgc.arch.domain.response.user.UserResponse;
import edy.app.sgc.arch.domain.usecase.BaseCase;
import edy.app.sgc.arch.domain.util.LangConfig;
import edy.app.sgc.arch.infrastructure.service.user.GetUserByIdService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/**
 * @author edythawne
 * @created 31/08/2026 15:41
 * @project ut_sgc
 */
@Service
@RequiredArgsConstructor
public class GetUserByIdCase extends BaseCase<Long, UserResponse> {

    private final LangConfig lang;
    private final ModelMapper dto;
    private final GetUserByIdService service;

    @Override
    protected Map<String, Object> onCreate(Long id) {
        if (id == null || id <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, lang.get("user.id.invalid"));
        }

        return Map.of(AppConstant.KEY_ID, id);
    }

    @Override
    protected ResultResponse<UserResponse> run(Map<String, Object> request) {
        var dbResponse = service.invoke(request);

        if (dbResponse == null){
            return new ResultResponse<>(
                HttpStatus.NO_CONTENT,
                lang.get("user.not_found"),
                null
            );
        }

        return new ResultResponse<>(
            HttpStatus.OK,
            HttpStatus.OK.getReasonPhrase(),
            dto.map(dbResponse, UserResponse.class)
        );
    }

}
