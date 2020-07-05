# Exercises for the _Testing with Containers_ workshop

**Please switch to branch 'workshop'**

This is an overview of the exercises for the workshop. There are several exercises spread across the source files.
You can find all exercises when you do a fulltext search for `EXERCISE`. There is also an overview below.

## Overview

1. [EXERCISE 1: TodoRepository integration test with database testcontainer](src/test/java/application/TodoResourceIT.java)
1. [EXERCISE 2: TodoResource integration test with database and api testcontainers](src/test/java/application/TodoResourceIT.java)
3. [EXERCISE 3: DbUnit persistence test with Postgres DB](todo-list/todo-list-service/src/test/java/de/openknowledge/projects/todolist/service/domain/TodoRepositoryIT.java)
4. [EXERCISE 4: MockServer integration](src/test/java/domain/GithubResourceIT.java)

### Exercise 1

The exercise can be found in [TodoRepositoryIT](src/test/java/domain/TodoRepositoryIT.java).

Todos:
1. Create a `PostgreSQLContainer` instance
2. Instantiate the EntityManager in the correct way
3. Run the test

### Exercise 2

The exercise can be found in [TodoResourceIT](src/test/java/application/TodoResourceIT.java).

Todos:
1. Create a testcontainers network.
2. Create a `PostgreSQLContainer` instance like in exercise 1. Use the network alias "database" for this container.
3. Create another container with the API as image. Use ImageFromDockerfile().withDockerfileFromBuilder(...) with data 
provided in [TodoResourceIT](src/test/java/application/TodoResourceIT.java).
4. Run the test

### Exercise 3

The exercise can be found in [TodoRepositoryIT.java](todo-list/todo-list-service/src/test/java/de/openknowledge/projects/todolist/service/domain/TodoRepositoryIT.java).
The [schema for the tables](todo-list/todo-list-service/src/main/resources/docker/init.sql) can be found in in the 
[resources/docker](todo-list/todo-list-service/src/main/resources/docker/) folder of the _todo-list-service_. 

Todos:
1. add `FixedHostPortGenericContainer` with postgres image
2. set environment variables for database configuration (database, user, password)
3. add DDL script
4. replace `FixedHostPortGenericContainer` by `GenericContainer`
5. override JDBC Url

### Exercise 4

The exercise can be found in [GithubResourceIT.java](src/test/java/domain/GithubResourceIT.java).

Todos:
1. Add a MockServerContainer with @ClassRule toe the test class.
2. Add a "Network", assign both containers to it and make sure to add a network alias "mockserver" to the MockServerContainer.
3. Let the TODO_SERVICE wait for the MockServerContainer.
4. Configure the MockServerClient and thus the MockServer expectation for a call to the route "/repos/cwansart/testcontainers-university".
