package domain;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.images.builder.ImageFromDockerfile;

import java.io.File;

/**
 * EXERCISE 4: Use MockServer to mock external servers.
 * <p>
 * HOWTO:
 * TO BE DONE @cwansart
 */
@RunWith(JUnit4.class)
public class GithubResourceIT {

    private static final Logger LOG = LoggerFactory.getLogger(GithubResourceIT.class);

    private static final Network NETWORK = Network.newNetwork();

    @ClassRule
    public static final MockServerContainer MOCK_SERVER_CONTAINER = new MockServerContainer()
        .withNetwork(NETWORK)
        .withNetworkAliases("mockserver")
        .withLogConsumer(new Slf4jLogConsumer(LOG));

    @ClassRule
    public static final GenericContainer<?> TODO_SERVICE = new GenericContainer<>(
        new ImageFromDockerfile()
            .withDockerfileFromBuilder(builder -> builder
                .from("openjdk:8-jre-alpine")
                .add("target/todo-service.jar", "/opt/todo-service.jar")
                .entryPoint("exec java -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses=true -jar /opt/todo-service.jar")
                .build())
            .withFileFromFile("target/todo-service.jar", new File("target/todo-service.jar")))
        .withExposedPorts(9080)
        .withNetwork(NETWORK)
        .withEnv("IS_CONTAINERIZED", "true")
        .dependsOn(MOCK_SERVER_CONTAINER)
        .withLogConsumer(new Slf4jLogConsumer(LOG));

    private static String serviceEndpoint;

    @BeforeClass
    public static void setupMockServer() {
        String response = "{\n" +
            "  \"id\": 269433808,\n" +
            "  \"node_id\": \"MDEwOlJlcG9zaXRvcnkyNjk0MzM4MDg=\",\n" +
            "  \"name\": \"testcontainers-university\",\n" +
            "  \"full_name\": \"cwansart/testcontainers-university\"" +
            "}";
        new MockServerClient(MOCK_SERVER_CONTAINER.getContainerIpAddress(), MOCK_SERVER_CONTAINER.getServerPort())
            .when(HttpRequest.request().withPath("/repos/cwansart/testcontainers-university"))
            .respond(HttpResponse.response().withBody(response));

        String host = TODO_SERVICE.getContainerIpAddress();
        Integer port = TODO_SERVICE.getMappedPort(9080);
        serviceEndpoint = String.format("http://%s:%s/todo-list-service", host, port);
    }

    @Test
    public void shouldSuccessfullyReturnMockData() {

        RestAssured.given()
            .accept(ContentType.JSON)
            .when()
            .get(serviceEndpoint + "/api/github")
            .andReturn().then()
            .body("id", CoreMatchers.is(269433808))
            .body("node_id", CoreMatchers.is("MDEwOlJlcG9zaXRvcnkyNjk0MzM4MDg="))
            .body("name", CoreMatchers.is("testcontainers-university"))
            .body("full_name", CoreMatchers.is("cwansart/testcontainers-university"));
    }
}
