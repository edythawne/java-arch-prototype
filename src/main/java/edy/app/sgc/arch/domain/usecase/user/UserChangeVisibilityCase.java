package edy.app.sgc.arch.domain.usecase.user;

import edy.app.sgc.arch.domain.response.ResultResponse;
import edy.app.sgc.arch.domain.usecase.BaseCase;
import edy.app.sgc.arch.domain.util.LangConfig;
import edy.app.sgc.arch.infrastructure.service.user.UserChangeVisibilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @author edythawne
 * @created 31/08/2026 17:32
 * @project ut_sgc
 */
@Service
@RequiredArgsConstructor
public class UserChangeVisibilityCase extends BaseCase<Long, Boolean> {

    private final LangConfig lang;
    private final UserChangeVisibilityService service;

    @Override
    public ResultResponse<Boolean> run() {
        if (request == null || request <= 0) {
            return new ResultResponse<>(
                HttpStatus.BAD_REQUEST,
                lang.get("user.id.invalid"),
                false
            );
        }

        service.setId(request);
        Boolean dbResponse = service.invoke();

        return new ResultResponse<>(
            HttpStatus.OK,
            dbResponse ? lang.get("user.change_visibility.success") : lang.get("user.change_visibility.error"),
            dbResponse
        );
    }
}