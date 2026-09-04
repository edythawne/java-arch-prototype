package edy.app.sgc.arch.domain.usecase;

import edy.app.sgc.arch.domain.response.ResultResponse;

/**
 * @author edythawne
 * @created 31/08/2026 14:23
 * @project ut_sgc
 */
public abstract class BaseCase<TInput, TOutput> {

    public abstract ResultResponse<TOutput> run(TInput request);

}
