package hu.pe.redmine.repositories;

import hu.pe.redmine.entities.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectTypeRepository extends JpaRepository<ProjectType,Long> {
    Optional<ProjectType> findByName(String name);
}
