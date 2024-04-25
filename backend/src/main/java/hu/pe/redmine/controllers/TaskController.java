package hu.pe.redmine.controllers;

import hu.pe.redmine.entities.Developer;
import hu.pe.redmine.entities.Project;
import hu.pe.redmine.entities.Task;
import hu.pe.redmine.entities.User;
import hu.pe.redmine.repositories.DeveloperRepository;
import hu.pe.redmine.repositories.ProjectRepository;
import hu.pe.redmine.repositories.TaskRepository;
import hu.pe.redmine.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    @Autowired
    private final ProjectRepository projectRepository;

    @Autowired
    private final TaskRepository taskRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final DeveloperRepository developerRepository;

    @PostMapping("/{id}/{devId}")
    public ResponseEntity<Task> createTask(@AuthenticationPrincipal User authenticatedUser, @PathVariable Long id, @PathVariable Long devId, @RequestBody Task task){
        if (task == null || task.getName() == null || task.getDescription() == null || task.getDeadline() == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        Optional<Project> oProject = projectRepository.findById(id);
        Optional<Developer> oDeveloper = developerRepository.findById(id);
        if(oProject.isEmpty() || oDeveloper.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        oProject.get().getDevelopers().add(oDeveloper.get());
        projectRepository.save(oProject.get());

        authenticatedUser = userRepository.save(authenticatedUser);

        Task newTask = Task.builder()
                .name(task.getName())
                .description(task.getDescription())
                .deadline(task.getDeadline())
                .manager(authenticatedUser)
                .project(oProject.get())
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(taskRepository.save(newTask));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Set<Task>> getTasks(@AuthenticationPrincipal User authenticatedUser, @PathVariable Long id){
        if(projectRepository.findById(id).isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taskRepository.findByProjectId(id));

    }

    @GetMapping("/{id}/own")
    public ResponseEntity<Set<Task>> getOwnTasks(@AuthenticationPrincipal User authenticatedUser, @PathVariable Long id){
        if(projectRepository.findById(id).isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(taskRepository.findByManager(authenticatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@AuthenticationPrincipal User authenticatedUser,@PathVariable Long id){
        if(projectRepository.findById(id).isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        taskRepository.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}
