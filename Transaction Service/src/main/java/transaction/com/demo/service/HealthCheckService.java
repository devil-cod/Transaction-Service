package transaction.com.demo.service;

import org.springframework.stereotype.Service;

@Service
public interface HealthCheckService {

    boolean checkDatabaseHealth();
}
