package ro.fortech.allocation.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String username;
    private String email;
    private String role;
    private String message;
}
