package ro.fortech.allocation.security.user.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ro.fortech.allocation.security.user.api.dto.UserDto;

import java.util.List;

@RequestMapping("/users")
public interface UserApi {

    @GetMapping
    ResponseEntity<List<UserDto>> getAllUsers();

    @GetMapping("/{id}")
    ResponseEntity<UserDto> getUserAtId(@PathVariable Long id);
}
