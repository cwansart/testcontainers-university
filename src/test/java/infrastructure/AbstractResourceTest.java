package infrastructure;

import application.TodoResourceIT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.io.File;

/**
 * EXERCISE 5: Singleton Container
 * * HOWTO:
 * 4. Make LOG, NETWORK, DATABASE_CONTAINER and API_CONTAINER private.
 * 5. Remove the @ClassRule annotations if still present.
 * 6. Create a static block to start DATABASE_CONTAINER and API_CONTAINER.
 * 7. Create a `protected static String getApiUrl()` method to return the api url of API_CONTAINER.
 * 8. Use `getApiUrl()` in the {@link application.TodoResourceSingletonIT}.
 */
public abstract class AbstractResourceTest {

  private static final Logger LOG = LoggerFactory.getLogger(TodoResourceIT.class);

  private static final Network NETWORK = Network.newNetwork();

  private static final PostgreSQLContainer<?> DATABASE_CONTAINER = new PostgreSQLContainer<>()
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

  private static final GenericContainer<?> API_CONTAINER = new GenericContainer<>(
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

  static {
    DATABASE_CONTAINER.start();
    API_CONTAINER.start();
  }

  protected static String getApiUrl() {
    String apiHost = API_CONTAINER.getContainerIpAddress();
    Integer apiPort = API_CONTAINER.getFirstMappedPort();
    return String.format("http://%s:%s/todo-list-service", apiHost, apiPort);
  }
}
