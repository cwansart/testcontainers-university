package application;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;

import static infrastructure.TestData.INTEGRATION_TEST_URL;

public class TodoResourceIT {

    @Test
    public void getTodosShouldReturn200() {
        RestAssured
                .when()
                .get(INTEGRATION_TEST_URL + "/api/todos/")
                .then()
                .statusCode(200)
                .body("size()", Matchers.is(3))
                .body("[0].id", Matchers.equalTo(1))
                .body("[0].name", Matchers.equalTo("Martin"))
                .body("[0].description", Matchers.equalTo("ABC"))
                .body("[0].status", Matchers.equalTo(true))
            .body("[0].dueDate", Matchers.equalTo("2020-01-10T07:30"));
    }

    @Test
    public void GetTodoByIdReturns200WithExpectedTodo() {
        RestAssured
            .when()
            .get(INTEGRATION_TEST_URL + "/api/todos/{id}", 1)
            .then()
            .statusCode(200)
            .body("id", Matchers.equalTo(1))
            .body("name", Matchers.equalTo("Martin"))
            .body("description", Matchers.equalTo("ABC"))
            .body("status", Matchers.equalTo(true))
            .body("dueDate", Matchers.equalTo("2020-01-10T07:30"));
    }

    @Test
    public void GetTodoByIdReturns404() {
        RestAssured
            .when()
            .get(INTEGRATION_TEST_URL + "/api/todos/{id}", 100)
            .then()
            .statusCode(404);
    }

    @Test
    public void GetTodoByIdReturns400ForNegativeId() {
        RestAssured
            .when()
            .get(INTEGRATION_TEST_URL + "/api/todos/{id}", -1)
            .then()
            .statusCode(400)
            .body(Matchers.equalTo("[{\"errorCode\":\"NEGATIVE_TODO_ID\",\"message\":\"todoId must be greater than or equal to 0\"}]"));
    }

    @Test
    public void UpdateTodoReturns204() {
        RestAssured
            .given()
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new BaseTodoDTO("new name", "new description", false, LocalDateTime.MIN))
            .when()
            .put(INTEGRATION_TEST_URL + "/api/todos/{id}", 2)
            .then()
            .statusCode(204);
    }

    @Test
    public void UpdateTodoReturns404ForNonExistingId() {
        RestAssured
            .given()
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new BaseTodoDTO("new name 2", "new description 2", false, LocalDateTime.MIN))
            .when()
            .put(INTEGRATION_TEST_URL + "/api/todos/{id}", 100)
            .then()
            .statusCode(404);
    }

    @Test
    public void UpdateTodoReturns404ForNegativeId() {
        RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new BaseTodoDTO("new name 2", "new description 2", false, LocalDateTime.MIN))
                .when()
                .put(INTEGRATION_TEST_URL + "/api/todos/{id}", -100)
                .then()
                .statusCode(400)
                .body(Matchers.equalTo("[{\"errorCode\":\"NEGATIVE_TODO_ID\",\"message\":\"todoId must be greater than or equal to 0\"}]"));
    }

    @Test
    public void UpdateTodoReturns400NoBaseTodo() {
        RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON)
            .when()
            .put(INTEGRATION_TEST_URL + "/api/todos/{id}", 1)
            .then()
            .statusCode(400)
            .body(Matchers.equalTo("[{\"errorCode\":\"BASETODO_NULL\",\"message\":\"baseTodo must not be null\"}]"));
    }

  @Test
public void UpdateTodoReturns400ForNameIsNull() {
    RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(new BaseTodoDTO(null, "new description", false, LocalDateTime.MIN))
            .when()
            .put(INTEGRATION_TEST_URL + "/api/todos/{id}", 1)
            .then()
            .statusCode(400)
            .body(Matchers.equalTo("[{\"errorCode\":\"TITLE_NULL\",\"message\":\"title must not be null\"}]"));
  }

    @Test
    public void UpdateTodoReturns400ForNameIsTooShort() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new BaseTodoDTO("", "new description", false, LocalDateTime.MIN))
                .when()
                .put(INTEGRATION_TEST_URL + "/api/todos/{id}", 1)
                .then()
                .statusCode(400)
                .body(Matchers.equalTo("[{\"errorCode\":\"TITLE_SIZE\",\"message\":\"title size must be between 1 and 30\"}]"));
    }

    @Test
    public void UpdateTodoReturns400ForDueDateIsNull() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new BaseTodoDTO("new name", "new description", false, null))
                .when()
                .put(INTEGRATION_TEST_URL + "/api/todos/{id}", 1)
                .then()
                .statusCode(400)
                .body(Matchers.equalTo("[{\"errorCode\":\"DUEDATE_NULL\",\"message\":\"dueDate must not be null\"}]"));
    }

    @Test
    public void UpdateTodoReturns400ForDescriptionIsTooLong() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new BaseTodoDTO("new name", "new descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew description", false, LocalDateTime.MIN))
                .when()
                .put(INTEGRATION_TEST_URL + "/api/todos/{id}", 1)
                .then()
                .statusCode(400)
                .body(Matchers.equalTo("[{\"errorCode\":\"DESCRIPTION_SIZE\",\"message\":\"description size must be between 0 and 500\"}]"));
    }

    @Test
    public void UpdateTodoReturns400ForMultipleErrors() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new BaseTodoDTO("", "new descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew description", false, null))
                .when()
                .put(INTEGRATION_TEST_URL + "/api/todos/{id}", 1)
                .then()
                .statusCode(400)
                .body(Matchers.containsString("{\"errorCode\":\"TITLE_SIZE\",\"message\":\"title size must be between 1 and 30\"}"))
                .body(Matchers.containsString("{\"errorCode\":\"DESCRIPTION_SIZE\",\"message\":\"description size must be between 0 and 500\"}"))
                .body(Matchers.containsString("{\"errorCode\":\"DUEDATE_NULL\",\"message\":\"dueDate must not be null\"}"));
    }

    @Test
    public void AddTodoReturns201WithExpectedString() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new BaseTodoDTO("new name", "new description", false, LocalDateTime.MIN))
                .when()
                .post(INTEGRATION_TEST_URL + "/api/todos")
                .then()
                .statusCode(201)
                .header("location", Matchers.equalTo(INTEGRATION_TEST_URL + "/api/todos/4"));
    }

    @Test
    public void AddTodoReturns400ForNoBaseTodo() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(INTEGRATION_TEST_URL + "/api/todos")
                .then()
                .statusCode(400)
                .body(Matchers.equalTo("[{\"errorCode\":\"BASETODO_NULL\",\"message\":\"baseTodo must not be null\"}]"));
    }

    @Test
    public void AddTodoReturns400ForNameIsNull() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new BaseTodoDTO(null, "new description", false, LocalDateTime.MIN))
                .when()
                .post(INTEGRATION_TEST_URL + "/api/todos")
                .then()
                .statusCode(400)
                .body(Matchers.equalTo("[{\"errorCode\":\"TITLE_NULL\",\"message\":\"title must not be null\"}]"));
    }

    @Test
    public void AddTodoReturns400ForNameIsTooShort() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new BaseTodoDTO("", "new description", false, LocalDateTime.MIN))
                .when()
                .post(INTEGRATION_TEST_URL + "/api/todos")
                .then()
                .statusCode(400)
                .body(Matchers.equalTo("[{\"errorCode\":\"TITLE_SIZE\",\"message\":\"title size must be between 1 and 30\"}]"));
    }

    @Test
    public void AddTodoReturns400ForDueDateIsNull() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new BaseTodoDTO("new name", "new description", false, null))
                .when()
                .post(INTEGRATION_TEST_URL + "/api/todos")
                .then()
                .statusCode(400)
                .body(Matchers.equalTo("[{\"errorCode\":\"DUEDATE_NULL\",\"message\":\"dueDate must not be null\"}]"));
    }

    @Test
    public void AddTodoReturns400ForDescriptionIsTooLong() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new BaseTodoDTO("new name", "new descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew description", false, LocalDateTime.MIN))
                .when()
                .post(INTEGRATION_TEST_URL + "/api/todos")
                .then()
                .statusCode(400)
                .body(Matchers.equalTo("[{\"errorCode\":\"DESCRIPTION_SIZE\",\"message\":\"description size must be between 0 and 500\"}]"));
    }

    @Test
    public void AddTodoReturns400ForMultipleErrors() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new BaseTodoDTO("", "new descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew descriptionnew description", false, null))
                .when()
                .post(INTEGRATION_TEST_URL + "/api/todos")
                .then()
                .statusCode(400)
                .body(Matchers.containsString("{\"errorCode\":\"TITLE_SIZE\",\"message\":\"title size must be between 1 and 30\"}"))
                .body(Matchers.containsString("{\"errorCode\":\"DESCRIPTION_SIZE\",\"message\":\"description size must be between 0 and 500\"}"))
                .body(Matchers.containsString("{\"errorCode\":\"DUEDATE_NULL\",\"message\":\"dueDate must not be null\"}"));
    }

    @Test
    public void DeleteTodoReturns204() {
        RestAssured
            .given()
            .when()
            .delete(INTEGRATION_TEST_URL + "/api/todos/{id}", 1)
            .then()
            .statusCode(204);
    }

    @Test
    public void DeleteTodoReturns404() {
        RestAssured
            .given()
            .when()
            .delete(INTEGRATION_TEST_URL + "/api/todos/{id}", 100)
            .then()
            .statusCode(404);
    }

    @Test
    public void DeleteTodoReturns404ForNegativeId() {
        RestAssured
                .given()
                .when()
                .delete(INTEGRATION_TEST_URL + "/api/todos/{id}", -1)
                .then()
                .statusCode(400)
                .body(Matchers.equalTo("[{\"errorCode\":\"NEGATIVE_TODO_ID\",\"message\":\"todoId must be greater than or equal to 0\"}]"));
    }

}
