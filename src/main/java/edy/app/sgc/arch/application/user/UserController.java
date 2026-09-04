package edy.app.sgc.arch.application.user;

import edy.app.sgc.arch.application.BaseController;
import edy.app.sgc.arch.domain.usecase.user.GetAllUserCase;
import edy.app.sgc.arch.domain.usecase.user.GetUserByIdCase;
import edy.app.sgc.arch.domain.usecase.user.UserChangeVisibilityCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author edythawne
 * @created 31/08/2026 11:22
 * @project ut_sgc
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("api/arch/user")
public class UserController extends BaseController {

    private final GetAllUserCase getAllCase;
    private final GetUserByIdCase getUserByIdCase;
    private final UserChangeVisibilityCase changeVisibilityCase;

    @GetMapping("/get/all")
    public ResponseEntity<Object> getAll(){
        return toResponse(getAllCase.run(null));
    }

    @GetMapping("/get/by/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
        return toResponse(getUserByIdCase.run(id));
    }

    @PutMapping("/change/visibility/{id}")
    public ResponseEntity<Object> changeVisibility(@PathVariable("id") Long id) {
        return toResponse(changeVisibilityCase.run(id));
    }

}
