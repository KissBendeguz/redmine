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

import java.util.Date;
import java.util.HashSet;
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

    @PostMapping("/{id}")
    public ResponseEntity<Task> createTask(@AuthenticationPrincipal User authenticatedUser, @PathVariable Long id, @RequestBody Task task){
        if (task == null || task.getName() == null || task.getDescription() == null || task.getDeadline() == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        Optional<Project> oProject = projectRepository.findById(id);
        if(oProject.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }

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

    @GetMapping("/deadline")
    public ResponseEntity<Set<Task>> getDeadlineTasks(@AuthenticationPrincipal User authenticatedUser) {

        Date currentTime = new Date();
        Date alertTime = new Date(currentTime.getTime() + 3 * 24 * 60 * 60 * 1000);

        Set<Task> tasks = taskRepository.findByManager(authenticatedUser);
        Set<Task> tasksCloseToDeadline = new HashSet<>();

        for (Task task : tasks) {
            if (task.getDeadline().before(alertTime)) {
                tasksCloseToDeadline.add(task);
            }
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tasksCloseToDeadline);
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
