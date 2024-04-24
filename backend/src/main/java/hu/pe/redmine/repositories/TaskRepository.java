package hu.pe.redmine.repositories;

import hu.pe.redmine.entities.Task;
import hu.pe.redmine.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface TaskRepository extends JpaRepository<Task,Long> {
    Set<Task> findByProjectId(Long projectId);
    Set<Task> findByManager(User manager);
}
