package hu.pe.redmine.repositories;

import hu.pe.redmine.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long> {
}
