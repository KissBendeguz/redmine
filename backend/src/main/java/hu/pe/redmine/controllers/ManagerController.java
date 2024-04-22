package hu.pe.redmine.controllers;

import hu.pe.redmine.entities.Manager;
import hu.pe.redmine.repositories.ManagerRepository;
import hu.pe.redmine.security.AuthenticationRequest;
import hu.pe.redmine.security.AuthenticationResponse;
import hu.pe.redmine.security.JwtService;
import hu.pe.redmine.security.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@RestController
@CrossOrigin
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {
    @Autowired
    private final ManagerRepository managerRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){

        if(managerRepository.findByEmail(request.getEmail()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Manager user = Manager.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        managerRepository.save(user);

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(
                AuthenticationResponse.builder()
                        .token(token)
                        .build()
        );
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Manager user = managerRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(
                AuthenticationResponse.builder()
                        .token(token)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<Manager> getAuthenticatedManager(@AuthenticationPrincipal Manager authenticatedUser){
        return ResponseEntity.ok(authenticatedUser);
    }
}
