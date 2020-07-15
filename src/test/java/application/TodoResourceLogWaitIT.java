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

/**
 * EXERCISE 3.2: Use log wait strategy to wait for the service
 * HOWTO:
 * 1. Find the log message which indicates that the services are running
 * 2. Add the waitingFor() method to both container builder
 * 3. Inside the waitingFor() method for both services add the Wait object with the correct wait strategies
 *
 * *** tips ***
 * - you should wait for a log message
 * - log messages are compared with regex
 */
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

    /**
     * EXERCISE 3.3: Use log consumer to wait for a log after the stsart
     * HOWTO:
     * 1. Find the log message which indicates that the service added a todo
     * 2. declare a waitingconsumer
     * 3. before all tests follow the stdout output of the API_CONTAINER with the waitingConsumer
     * 4. after the http request was send check if the 'Add todo' log was send to stdout
     *   4.1. the waitconsumer has to waitUntil the Utf8String of the frame contains the correct log
     *   4.2 use a appropriate timeunit
     *
     * *** tips ***
     * - the OutputFrame class has multiple output types
     */
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
