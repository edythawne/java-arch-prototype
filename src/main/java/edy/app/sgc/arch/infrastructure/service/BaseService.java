package edy.app.sgc.arch.infrastructure.service;

/**
 * @author edythawne
 * @created 31/08/2026 13:15
 * @project ut_sgc
 */
public abstract class BaseService<TOutput> {

    public abstract TOutput invoke();

}
