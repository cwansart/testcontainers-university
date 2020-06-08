package domain;

import com.github.database.rider.core.util.EntityManagerProvider;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class TodoRepositoryIT {

    private static final Logger LOG = LoggerFactory.getLogger(TodoRepositoryIT.class);

    @ClassRule
    public static PostgreSQLContainer<?> database = new PostgreSQLContainer<>()
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("postgres")
            .withInitScript("container/init.sql")
            .withLogConsumer(new Slf4jLogConsumer(LOG));

    private static final Map<String, String> entityManagerProviderProperties = new HashMap<>();

    @BeforeClass
    public static void setUpDatabaseAndUri() {
        entityManagerProviderProperties.put("javax.persistence.jdbc.url", database.getJdbcUrl());
        entityManagerProviderProperties.put("javax.persistence.jdbc.driver", database.getDriverClassName());
        entityManagerProviderProperties.put("javax.persistence.jdbc.user", database.getUsername());
        entityManagerProviderProperties.put("javax.persistence.jdbc.password", database.getPassword());
    }

    @Rule
    public EntityManagerProvider entityManagerProvider =
            EntityManagerProvider.instance("postgres-db", entityManagerProviderProperties);

    private TodoRepository todoRepository;

    @Before
    public void setUp() {
        todoRepository = new TodoRepository(entityManagerProvider.getEm());
    }

    @Test
    public void addNewTodoToDatabase() {

        entityManagerProvider.getEm().getTransaction().begin();

        Todo todo = new Todo();

        todo.setName("foo");
        todo.setDueDate(LocalDateTime.now());
        todo.setDescription("foo");

        todoRepository.addTodo(todo);

        entityManagerProvider.getEm().getTransaction().commit();
    }
}
