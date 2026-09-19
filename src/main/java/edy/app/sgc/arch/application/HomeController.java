package edy.app.sgc.arch.application;

import edy.app.sgc.arch.domain.response.ResultResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.SpringVersion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author edythawne
 * created 19/09/2026 14:01
 * project ut_sgc
 */
@RestController
public class HomeController extends BaseController {

    @Value("${springdoc.swagger-ui.path:/swagger-ui.html}")
    private String swaggerPath;

    @GetMapping("/")
    public ResponseEntity<Object> index() {
        String springVersion = SpringVersion.getVersion();

        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        String cleanSwaggerPath = swaggerPath.startsWith("/") ? swaggerPath : "/" + swaggerPath;
        String fullSwaggerUrl = baseUrl + cleanSwaggerPath;

        Map<String, String> map = Map.of(
            "service", "API",
            "timestamp", LocalDateTime.now().toString(),
            "version", "Spring Framework Version: " + springVersion,
            "java", System.getProperty("java.version"),
            "swagger", fullSwaggerUrl
        );

        var response = new ResultResponse<Map<String, String>>();
        response.setData(map);
        response.setCode(HttpStatus.OK);
        response.setMessage(HttpStatus.OK.getReasonPhrase());

        return toResponse(response);
    }


}