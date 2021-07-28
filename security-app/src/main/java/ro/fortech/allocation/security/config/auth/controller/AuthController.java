package ro.fortech.allocation.security.config.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.fortech.allocation.security.config.auth.service.AuthService;
import ro.fortech.allocation.security.dto.JwtResponse;
import ro.fortech.allocation.security.dto.LogInRequest;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-in")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LogInRequest loginRequest) {
        JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(
                jwtResponse
        );
    }
}