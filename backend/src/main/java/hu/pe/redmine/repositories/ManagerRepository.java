package hu.pe.redmine.repositories;

import hu.pe.redmine.entities.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager,Long> {
    Optional<Manager> findByEmail(String email);
}
