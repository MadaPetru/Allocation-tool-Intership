package ro.fortech.allocation.security.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ro.fortech.allocation.security.user.api.dto.UserDto;
import ro.fortech.allocation.security.user.mapper.UserMapper;
import ro.fortech.allocation.security.user.model.User;
import ro.fortech.allocation.security.user.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserDto getUserAtId(Long id) {
        User foundUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find user at id: " + id));

        return userMapper.toDTO(foundUser);
    }
}
