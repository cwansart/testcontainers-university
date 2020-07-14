package application;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;

@Readiness
@ApplicationScoped
public class ServiceReadyCheck implements HealthCheck {

  @Resource(name = "jdbc/TodoListDS")
  private DataSource datasource;

  // checks if the database connection is valid every 10 seconds
  public boolean isHealthy() {
    try {
      return datasource
          .getConnection()
          .isValid(10);
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public HealthCheckResponse call() {
    return HealthCheckResponse
        .named(ServiceReadyCheck.class.getSimpleName())
        .withData("ready", isHealthy())
        .up()
        .build();
  }
}
