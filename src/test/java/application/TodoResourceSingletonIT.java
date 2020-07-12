package application;

import infrastructure.AbstractResourceTest;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.contains;

/**
 * EXERCISE 5: Singleton Container
 * * HOWTO:
 * to be done
 */
@RunWith(JUnit4.class)
public class TodoResourceSingletonIT extends AbstractResourceTest {

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
