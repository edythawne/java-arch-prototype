package edy.app.sgc.arch.infrastructure.service.user;

import edy.app.sgc.arch.infrastructure.entity.user.UserEntity;
import edy.app.sgc.arch.infrastructure.service.BaseService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Setter;
import org.springframework.stereotype.Repository;

/**
 * @author edythawne
 * @created 31/08/2026 15:43
 * @project ut_sgc
 */
@Repository
public class GetUserByIdService extends BaseService<Long, UserEntity> {

    @PersistenceContext
    private EntityManager connection;

    @Override
    public UserEntity invoke(Long id) {
        try {
            String jpql = """
                SELECT u 
                FROM UserEntity u 
                LEFT JOIN FETCH u.medicalRecord 
                WHERE u.id = :id
            """;

            var record = connection.createQuery(jpql, UserEntity.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst();

            return record.orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

}
