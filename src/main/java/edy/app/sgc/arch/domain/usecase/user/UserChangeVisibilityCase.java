package edy.app.sgc.arch.domain.usecase.user;

import edy.app.sgc.arch.AppConstant;
import edy.app.sgc.arch.domain.response.ResultResponse;
import edy.app.sgc.arch.domain.usecase.BaseCase;
import edy.app.sgc.arch.domain.util.LangConfig;
import edy.app.sgc.arch.infrastructure.service.user.UserChangeVisibilityService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

/**
 * @author edythawne
 * @created 31/08/2026 17:32
 * @project ut_sgc
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserChangeVisibilityCase extends BaseCase<Long, Boolean> {

    private final LangConfig lang;
    private final UserChangeVisibilityService service;

    @Override
    protected Map<String, Object> onCreate(Long id) {
        if (id == null || id <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, lang.get("user.id.invalid"));
        }

        return Map.of(AppConstant.KEY_ID, id);
    }

    @Override
    protected ResultResponse<Boolean> run(Map<String, Object> request) {
        log.info("Valor del request : {}", request);
        Boolean dbResponse = service.invoke(request);

        return new ResultResponse<>(
            HttpStatus.OK,
            dbResponse ? lang.get("user.change_visibility.success") : lang.get("user.change_visibility.error"),
            dbResponse
        );
    }

}