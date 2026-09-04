package edy.app.sgc.arch.domain.usecase;

import edy.app.sgc.arch.domain.response.ResultResponse;
import org.springframework.validation.BindException;

import java.util.Map;

/**
 * @author edythawne
 * @created 31/08/2026 14:23
 * @project ut_sgc
 */
public abstract class BaseCase<TRequest, TResponse>{

    public ResultResponse<TResponse> execute(TRequest request){
        var dataRequest = this.onCreate(request);
        return this.run(dataRequest);
    }

    protected abstract Map<String, Object> onCreate(TRequest request);

    protected abstract ResultResponse<TResponse> run(Map<String, Object> request);

}
