package application;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.output.WaitingConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RunWith(JUnit4.class)
public class TodoResourceLogWaitIT {

    private static final Logger LOG = LoggerFactory.getLogger(TodoResourceLogWaitIT.class);

    private static String API_URL;

    private static Network network = Network.newNetwork();

    private static WaitingConsumer waitingConsumer = new WaitingConsumer();

    @ClassRule
    public static PostgreSQLContainer<?> DATABASE_CONTAINER = new PostgreSQLContainer<>()
            .withExposedPorts(5432)
            .withNetwork(network)
            .withNetworkAliases("database")
            .withUsername("postgres")
            .withPassword("postgres")
            .withDatabaseName("postgres")
            .withInitScript("init/init.sql")
            .withLogConsumer(new Slf4jLogConsumer(LOG))
            .waitingFor(Wait.forLogMessage(".*server started.*", 1));

    @ClassRule
    public static GenericContainer<?> API_CONTAINER = new GenericContainer<>(new ImageFromDockerfile()
            .withDockerfileFromBuilder(builder -> builder
                    .from("openjdk:8-jre-alpine")
                    .add("target/todo-service.jar", "/opt/todo-service.jar")
                    .entryPoint("exec java -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses=true -jar /opt/todo-service.jar")
                    .build())
            .withFileFromFile("target/todo-service.jar", new File("target/todo-service.jar")))
            .withExposedPorts(9080)
            .withNetwork(network)
            .dependsOn(DATABASE_CONTAINER)
            .withLogConsumer(new Slf4jLogConsumer(LOG))
            .waitingFor(Wait.forLogMessage(".*The defaultServer server is ready to run a smarter planet.*", 1));

    @BeforeClass
    public static void getApiUrl() {
        String apiHost = API_CONTAINER.getContainerIpAddress();
        Integer apiPort = API_CONTAINER.getFirstMappedPort();
        API_URL = String.format("http://%s:%s/todo-list-service", apiHost, apiPort);

        API_CONTAINER.followOutput(waitingConsumer, OutputFrame.OutputType.STDOUT);
    }

    @Test
    public void AddTodoReturns201WithExpectedString() throws TimeoutException {
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new BaseTodoDTO("new name", "new description", false, LocalDateTime.MIN))
                .when()
                .post(API_URL + "/api/todos")
                .then()
                .statusCode(201)
                .header("location", Matchers.equalTo(API_URL + "/api/todos/1"));

        waitingConsumer.waitUntil(frame -> frame.getUtf8String().contains("Add todo"), 1, TimeUnit.MINUTES);
    }
}
