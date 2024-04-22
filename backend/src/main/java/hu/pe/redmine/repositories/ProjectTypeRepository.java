package hu.pe.redmine.repositories;

import hu.pe.redmine.entities.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectTypeRepository extends JpaRepository<ProjectType,Long> {
}
