package application;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;

/**
 * EXERCISE 2: API test with Postgres DB and API testcontainers (JUnit 4)
 * <p>
 * HOWTO:
 * 1. add `@RunWith(JUnit4.class)` annotation to test class
 * 2. create a new network instance
 * 3. create a `PostgreSQLContainer` with database name `postgres`, username `postgres` and password `postgres`
 * 4. add init script with path `container/init.sql` and to the database testcontainer
 * 5. add the network instance to the database testcontainer
 * 6. add Slf4jLogConsumer to the database testcontainer
 * 7. create a new generic container and provide a custom image as constructor argument
 * 8. use the ImageFromDockerfile class with the Dockerfile builder to create the custom image
 * 8. use the builder methods like:
 *      from("openjdk:8-jre-alpine")
 *      add("target/todo-service.jar", "/opt/todo-service.jar")
 *      entryPoint("exec java -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses=true -jar /opt/todo-service.jar")
 * 9. add an exposed port: 9080
 * 10. add a depends on method to the API testcontainer. guess which other testcontainer the API testcontainer depends on.
 * 11. add Slf4jLogConsumer to the database testcontainer.
 * 12. fill the getApiUrl method -> get the API testcontainer IP address and port and construct a valid API url. the API uri is '/todo-list-service'.
 * 13. run the test.
 */

public class TodoResourceIT {

    private static String API_URL;

    private static final Logger LOG = LoggerFactory.getLogger(TodoResourceIT.class);

    //public static Network network =

    /*@ClassRule
    public static PostgreSQLContainer<?> DATABASE_CONTAINER = */

    /*@ClassRule
    public static GenericContainer<?> API_CONTAINER = new GenericContainer<>(...);*/

    @BeforeClass
    public static void getApiUrl() {
        //API_URL =
    }

    @Test
    public void addTodoReturns201WithExpectedString() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new BaseTodoDTO("new name", "new description", false, LocalDateTime.MIN))
                .when()
                .post(API_URL + "/api/todos")
                .then()
                .statusCode(201)
                .header("location", Matchers.equalTo(API_URL + "/api/todos/1"));
    }
}
