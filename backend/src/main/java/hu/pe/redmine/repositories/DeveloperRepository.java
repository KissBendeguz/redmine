package hu.pe.redmine.repositories;

import hu.pe.redmine.entities.Developer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeveloperRepository extends JpaRepository<Developer,Long> {
}
