package transaction.com.demo.controller;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import transaction.com.demo.service.HealthCheckService;

@RestController
@RequestMapping("api/v1")
public class HealthCheckController {

    private final HealthCheckService healthCheckService;


    public HealthCheckController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        boolean isDatabaseHealthy = healthCheckService.checkDatabaseHealth();

        if (isDatabaseHealthy) {
            return ResponseEntity.ok("API is healthy");
        } else {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("API is not healthy");
        }
    }
}
