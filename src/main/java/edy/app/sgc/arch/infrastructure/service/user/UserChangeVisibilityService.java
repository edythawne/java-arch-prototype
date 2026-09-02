package edy.app.sgc.arch.infrastructure.service.user;

import edy.app.sgc.arch.infrastructure.service.BaseService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author edythawne
 * @created 31/08/2026 17:27
 * @project ut_sgc
 */
@Repository
@RequiredArgsConstructor
public class UserChangeVisibilityService extends BaseService<Boolean> {

    private final JdbcTemplate connection;
    private final TransactionTemplate transaction;

    @Setter
    private Long id;

    @Override
    public Boolean invoke() {
        return transaction.execute(status -> {
            try {
                String sql = "SELECT user_change_visibility(?)";
                return connection.queryForObject(sql, Boolean.class, id);
            } catch (Exception e) {
                status.setRollbackOnly();
                return false;
            }
        });
    }
}
