package hu.pe.redmine.controllers;

import hu.pe.redmine.entities.Manager;
import hu.pe.redmine.entities.Project;
import hu.pe.redmine.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    @Autowired
    private final ProjectRepository projectRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@AuthenticationPrincipal Manager authenticatedUser, @PathVariable Long id){
        Optional<Project> oProject = projectRepository.findById(id);
        if(oProject.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(oProject.get());
    }

    @GetMapping
    public ResponseEntity<Set<Project>> getProjects(@AuthenticationPrincipal Manager authenticatedUser){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new HashSet<>(projectRepository.findAll()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@AuthenticationPrincipal Manager authenticatedUser,Long id){
        Optional<Project> oProject = projectRepository.findById(id);
        if(oProject.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        projectRepository.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}
