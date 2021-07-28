package ro.fortech.allocation.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SignUpRequest {
    private String username;
    private String email;
    private String password;
    private String role;
}
