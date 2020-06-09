package domain;

import infrastructure.stereotypes.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

@Repository
public class TodoRepository implements Serializable {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger LOG = LoggerFactory.getLogger(TodoRepository.class);

    public TodoRepository() {
        super();
    }

    TodoRepository(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Todo> getTodos() {
        LOG.info("Get all todos");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Todo> cq = cb.createQuery(Todo.class);
        Root<Todo> root = cq.from(Todo.class);
        CriteriaQuery<Todo> all = cq.select(root);
        TypedQuery<Todo> allQuery = entityManager.createQuery(all);
        return allQuery.getResultList();
    }

    public Todo findById(final long todoId) {
        LOG.info("Get todo by id: {}", todoId);
        return entityManager.find(Todo.class, todoId);
    }

    public Long addTodo(final Todo todo) {
        LOG.info("Add todo");
        entityManager.persist(todo);
        entityManager.flush();
        return todo.getId();
    }

    public void deleteTodo(final long todoId) {
        LOG.info("Delete todo by id: {}", todoId);
        Todo todo = entityManager.find(Todo.class, todoId);
        entityManager.remove(todo);
    }

    public void updateTodo(final Todo todo) {
        LOG.info("Update todo");
        entityManager.merge(todo);
    }
}
