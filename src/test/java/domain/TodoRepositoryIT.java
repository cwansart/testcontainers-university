package domain;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * EXERCISE 1: Persistence test with a Postgres DB testcontainer (JUnit 4)
 * <p>
 * HOWTO:
 * 1. add `@RunWith(JUnit4.class)` annotation to test class
 * 2. create a `PostgreSQLContainer` with database name `postgres`, username `postgres` and password `postgres`
 * 3. add init script with path `container/init.sql` to the database testcontainer
 * 4. add Slf4jLogConsumer to the database testcontainer
 * 4. add JdbcUrl, DriverClassName, Username and Password to EntityManagerProviderProperties
 * 5. create an EntityManagerProvider instance with unitName `postgres-db` and add the EntityManagerProviderProperties
 * 6. fill the setUp method and instantiate TodoRepository with EntityManager
 * <p>
 * Hint: For step 4 use the default persistence.xml property names as keys and check the getter methods on the
 * container object.
 * <p>
 * Hint: For step 6 check the getEm() method on the EntityManagerProvider instance.
 */

public class TodoRepositoryIT {

    private static final Logger LOG = LoggerFactory.getLogger(TodoRepositoryIT.class);

    @ClassRule
    //public static PostgreSQLContainer<?> database =

    private static final Map<String, String> entityManagerProviderProperties = new HashMap<>();

    @BeforeClass
    public static void setUpDatabaseAndUri() {

    }

    //@Rule
    // public EntityManagerProvider entityManagerProvider =

    private TodoRepository todoRepository;

    @Before
    public void setUp() {

    }

    /*@Test
    public void addNewTodoToDatabase() {

        entityManagerProvider.getEm().getTransaction().begin();

        Todo todo = new Todo();

        todo.setName("foo");
        todo.setDueDate(LocalDateTime.now());
        todo.setDescription("foo");

        todoRepository.addTodo(todo);

        entityManagerProvider.getEm().getTransaction().commit();
    }*/
}
