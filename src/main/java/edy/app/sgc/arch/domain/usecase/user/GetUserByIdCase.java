package edy.app.sgc.arch.domain.usecase.user;

import edy.app.sgc.arch.domain.response.ResultResponse;
import edy.app.sgc.arch.domain.response.user.UserResponse;
import edy.app.sgc.arch.domain.usecase.BaseCase;
import edy.app.sgc.arch.domain.util.LangConfig;
import edy.app.sgc.arch.infrastructure.service.user.GetUserByIdService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
    public ResultResponse<UserResponse> run(Long request) {
        if (request == null || request <= 0){
            return new ResultResponse<>(
                HttpStatus.BAD_REQUEST,
                lang.get("user.id.invalid"),
                null
            );
        }

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
