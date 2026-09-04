package edy.app.sgc.arch.infrastructure.service.user;

import edy.app.sgc.arch.infrastructure.entity.user.UserEntity;
import edy.app.sgc.arch.infrastructure.service.BaseService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author edythawne
 * @created 31/08/2026 13:13
 * @project ut_sgc
 */
@Repository
public class GetAllStudentService extends BaseService<List<UserEntity>>{

    @PersistenceContext
    private EntityManager connection;

    @Override
    public List<UserEntity> invoke(Map<String, Object> data) {
        try {
            String sql = """
                SELECT u 
                FROM UserEntity u 
                LEFT JOIN FETCH u.medicalRecord 
                WHERE u.isActive = true
            """;

            return connection.createQuery(sql, UserEntity.class).getResultList();
        } catch (Exception exception){
            return null;
        }
    }

}
