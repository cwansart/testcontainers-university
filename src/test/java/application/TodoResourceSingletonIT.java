package application;

import infrastructure.AbstractResourceTest;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.contains;

/**
 * EXERCISE 5: Singleton Container
 * * HOWTO:
 * 1. Move LOG, NETWORK, DATABASE_CONTAINER and API_CONTAINER to {@link AbstractResourceTest}.
 * 2. Extend this class with {@link AbstractResourceTest}.
 * 3. Continute in {@link AbstractResourceTest}.
 */
@RunWith(JUnit4.class)
public class TodoResourceSingletonIT {

  private static final Logger LOG = LoggerFactory.getLogger(TodoResourceIT.class);

  private static final Network NETWORK = Network.newNetwork();

  @ClassRule
  public static final PostgreSQLContainer<?> DATABASE_CONTAINER = new PostgreSQLContainer<>()
      .withExposedPorts(5432)
      .withNetwork(NETWORK)
      .withNetworkAliases("database")
      .withUsername("postgres")
      .withPassword("postgres")
      .withDatabaseName("postgres")
      .withInitScript("init/first.sql")
      .withLogConsumer(new Slf4jLogConsumer(LOG))
      .waitingFor(
          Wait.forLogMessage(".*server started.*", 1)
      );

  @ClassRule
  public static final GenericContainer<?> API_CONTAINER = new GenericContainer<>(
      new ImageFromDockerfile()
          .withDockerfileFromBuilder(builder -> builder
              .from("openjdk:8-jre-alpine")
              .add("target/todo-service.jar", "/opt/todo-service.jar")
              .entryPoint("exec java -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses=true -jar /opt/todo-service.jar")
              .build())
          .withFileFromFile("target/todo-service.jar", new File("target/todo-service.jar")))
      .withExposedPorts(9080)
      .withNetwork(NETWORK)
      .dependsOn(DATABASE_CONTAINER)
      .withLogConsumer(new Slf4jLogConsumer(LOG));

  public static String getApiUrl() {
    String apiHost = API_CONTAINER.getContainerIpAddress();
    Integer apiPort = API_CONTAINER.getFirstMappedPort();
    return String.format("http://%s:%s/todo-list-service", apiHost, apiPort);
  }

  @Test
  public void shouldReturnInitializedTodo() {
    RestAssured.given()
        .contentType(MediaType.APPLICATION_JSON)
        .when()
        .get(getApiUrl() + "/api/todos")
        .then()
        .statusCode(200)
        .body("name", contains("First"));
  }

  @Test
  public void shouldReturn404ForUnknownTodo() {
    RestAssured.given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(new BaseTodoDTO("new name", "new description", false, LocalDateTime.MIN))
        .when()
        .get(getApiUrl() + "/api/todos/999")
        .then()
        .statusCode(404);
  }

  @Test
  public void addTodoReturns201WithExpectedString() {
    RestAssured.given()
        .contentType(MediaType.APPLICATION_JSON)
        .body(new BaseTodoDTO("new name", "new description", false, LocalDateTime.MIN))
        .when()
        .post(getApiUrl() + "/api/todos")
        .then()
        .statusCode(201)
        .header("location", Matchers.startsWith(getApiUrl() + "/api/todos/"));
  }
}
