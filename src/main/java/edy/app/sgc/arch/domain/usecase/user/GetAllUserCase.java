package edy.app.sgc.arch.domain.usecase.user;

import edy.app.sgc.arch.domain.response.ResultResponse;
import edy.app.sgc.arch.domain.response.user.StudentIndexResponse;
import edy.app.sgc.arch.domain.usecase.BaseCase;
import edy.app.sgc.arch.domain.util.LangConfig;
import edy.app.sgc.arch.infrastructure.entity.user.UserEntity;
import edy.app.sgc.arch.infrastructure.service.user.GetAllStudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author edythawne
 * @created 31/08/2026 14:22
 * @project ut_sgc
 */
@Service
@RequiredArgsConstructor
public class GetAllUserCase extends BaseCase<Object, List<StudentIndexResponse>>  {

    private final LangConfig lang;
    private final GetAllStudentService service;

    @Override
    protected Map<String, Object> onCreate(Object o) {
        return Map.of();
    }

    @Override
    protected ResultResponse<List<StudentIndexResponse>> run(Map<String, Object> request) {
        try {
            var dbResponse = service.invoke(request);

            if (dbResponse == null || dbResponse.isEmpty()){
                return new ResultResponse<>(
                    HttpStatus.NO_CONTENT,
                    lang.get("user.get_all.no_data"),
                    new ArrayList<>()
                );
            }

            return new ResultResponse<>(
                HttpStatus.OK,
                lang.get("user.get_all.success"),
                toResponseList(dbResponse)
            );
        } catch (Exception e) {
            return new ResultResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR,
                lang.get("database.error"),
                new ArrayList<>()
            );
        }
    }

    private List<StudentIndexResponse> toResponseList(List<UserEntity> entities){
        return entities.stream().map(this::toResponse).toList();
    }

    private StudentIndexResponse toResponse(UserEntity entity) {
        var student = new StudentIndexResponse();
        var medicalRecord = entity.getMedicalRecord();

        student.setStudentNumber(entity.getStudentNumber());
        student.setName(entity.getFirstName());
        student.setEmail(entity.getEmail());
        student.setLastName(
            (entity.getPaternalLastName() == null ? "" : entity.getPaternalLastName())
                .concat(" ")
                .concat(entity.getMaternalLastName() == null ? "" : entity.getMaternalLastName())
        );

        if (medicalRecord != null) {
            student.setBloodType(medicalRecord.getBloodType());
            student.setEmergencyContactName(medicalRecord.getEmergencyContactName());
            student.setEmergencyContactPhone(medicalRecord.getEmergencyContactPhone());
        }

        if (medicalRecord == null){
            student.setBloodType("");
            student.setEmergencyContactPhone("");
            student.setEmergencyContactName("");
        }

        return student;
    }

}
