package hu.pe.redmine.controllers;

import hu.pe.redmine.entities.Developer;
import hu.pe.redmine.entities.Project;
import hu.pe.redmine.entities.User;
import hu.pe.redmine.repositories.DeveloperRepository;
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
@RequestMapping("/developer")
@RequiredArgsConstructor
public class DeveloperController {
    @Autowired
    private final DeveloperRepository developerRepository;

    @PostMapping
    public ResponseEntity<Developer> addDeveloper(@AuthenticationPrincipal User authenticatedUser, @RequestBody Developer developer){
        Developer newDeveloper = Developer.builder()
                .name(developer.getName())
                .email(developer.getEmail())
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(developerRepository.save(newDeveloper));
    }

    @GetMapping
    public ResponseEntity<Set<Developer>> getDevelopers(@AuthenticationPrincipal User authenticatedUser){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new HashSet<>(developerRepository.findAll()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDeveloper(@AuthenticationPrincipal User authenticatedUser, @PathVariable Long id){
        Optional<Developer> oDev = developerRepository.findById(id);
        if(oDev.isEmpty()){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
        developerRepository.delete(oDev.get());
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
