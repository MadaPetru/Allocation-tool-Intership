package ro.fortech.allocation.security.config.auth.service;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ro.fortech.allocation.security.config.jwt.JwtUtils;
import ro.fortech.allocation.security.config.user.UserDetailsImpl;
import ro.fortech.allocation.security.dto.JwtResponse;
import ro.fortech.allocation.security.dto.LogInRequest;

@Service
@AllArgsConstructor
public class AuthService {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public JwtResponse authenticateUser(LogInRequest logInRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                logInRequest.getUsername(), logInRequest.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl toBeLoggedInUser = (UserDetailsImpl) authentication.getPrincipal();

        String role = toBeLoggedInUser.getAuthorities().stream()
                .map(r -> r.getAuthority())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Couldn't get authority!"));

        JwtResponse response = JwtResponse.builder()
                .token(token)
                .username(toBeLoggedInUser.getUsername())
                .email(toBeLoggedInUser.getEmail())
                .role(role)
                .message("User successfully authenticated!")
                .build();
        
        return response;
    }
}