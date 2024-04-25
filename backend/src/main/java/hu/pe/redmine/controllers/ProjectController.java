package hu.pe.redmine.controllers;

import hu.pe.redmine.entities.ProjectType;
import hu.pe.redmine.entities.User;
import hu.pe.redmine.entities.Project;
import hu.pe.redmine.repositories.ProjectRepository;
import hu.pe.redmine.repositories.ProjectTypeRepository;
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
    @Autowired
    private final ProjectTypeRepository projectTypeRepository;

    @PostMapping
    public ResponseEntity<Project> createProject(@AuthenticationPrincipal User authenticatedUser, @RequestBody Project project) {
        if (project == null || project.getName() == null || project.getDescription() == null || project.getType() == null) {
            return ResponseEntity.badRequest().build();
        }

        ProjectType projectType = project.getType();
        Optional<ProjectType> existingProjectType = projectTypeRepository.findByName(projectType.getName());
        if (existingProjectType.isEmpty()) {
            projectTypeRepository.save(projectType);
        } else {
            project.setType(existingProjectType.get());
        }

        Project newProject = Project.builder()
                .name(project.getName())
                .description(project.getDescription())
                .developers(new HashSet<>())
                .type(project.getType())
                .build();

        Project savedProject = projectRepository.save(newProject);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProject);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@AuthenticationPrincipal User authenticatedUser, @PathVariable Long id){
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
    public ResponseEntity<Set<Project>> getProjects(@AuthenticationPrincipal User authenticatedUser){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new HashSet<>(projectRepository.findAll()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@AuthenticationPrincipal User authenticatedUser, @PathVariable Long id){
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
