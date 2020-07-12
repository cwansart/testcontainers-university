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

public abstract class AbstractResourceTest {

  private static final Logger LOG = LoggerFactory.getLogger(TodoResourceIT.class);

  public static final Network network = Network.newNetwork();

  private static final PostgreSQLContainer<?> DATABASE_CONTAINER = new PostgreSQLContainer<>()
      .withExposedPorts(5432)
      .withNetwork(network)
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
      .withNetwork(network)
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
