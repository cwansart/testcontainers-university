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

/**
 * EXERCISE 5: Singleton Container
 * * HOWTO:
 * 4. Make LOG, NETWORK, DATABASE_CONTAINER and API_CONTAINER private.
 * 5. Remove the @ClassRule annotations if still present.
 * 6. Create a static block to start DATABASE_CONTAINER and API_CONTAINER.
 * 7. Create a `protected static String getApiUrl()` method to return the api url of API_CONTAINER.
 * 8. Use `getApiUrl()` in the {@link application.TodoResourceSingletonIT}.
 */
public abstract class AbstractResourceTest {

}
