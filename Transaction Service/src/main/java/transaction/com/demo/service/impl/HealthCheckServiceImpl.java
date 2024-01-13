package transaction.com.demo.service.impl;


import org.springframework.stereotype.Service;
import transaction.com.demo.configuration.DataSourceConfig;
import transaction.com.demo.service.HealthCheckService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


@Service
public class HealthCheckServiceImpl implements HealthCheckService {
    private final DataSource dataSource;

    public HealthCheckServiceImpl(DataSource dataSource) { this.dataSource = dataSource; }

    public boolean checkDatabaseHealth() {
        try (Connection connection = dataSource.getConnection()) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}