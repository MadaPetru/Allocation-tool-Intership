package ro.fortech.allocation.security.user.mapper;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.junit4.SpringRunner;
import ro.fortech.allocation.security.role.model.ERole;
import ro.fortech.allocation.security.role.model.Role;
import ro.fortech.allocation.security.user.api.dto.UserDto;
import ro.fortech.allocation.security.user.model.User;

@RunWith(SpringRunner.class)
public class UserMapperTest {

    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    public void shouldReturnTheHardCodedDTOTest() {
        Role administratorUSER = Role.builder().name(ERole.ADMINISTRATOR).build();

        User testUser = User.builder()
                .id(5L)
                .email("testfoo@bar.com")
                .username("testfoo")
                .role(administratorUSER)
                .build();

        UserDto testUserDto = UserDto.builder()
                .id(testUser.getId())
                .email(testUser.getEmail())
                .username(testUser.getUsername())
                .role("ADMINISTRATOR")
                .build();

        UserDto resultedValue = userMapper.toDTO(testUser);

        Assertions.assertEquals(testUserDto, resultedValue);
    }

    @Test
    public void shouldReturnTheHardCodedUserTest() {
        Role administratorUSER = Role.builder().name(ERole.ADMINISTRATOR).build();

        UserDto testUserDto = UserDto.builder()
                .id(5L)
                .email("testfoo@bar.com")
                .username("testfoo")
                .role("ADMINISTRATOR")
                .build();

        User testUser = User.builder()
                .id(testUserDto.getId())
                .email(testUserDto.getEmail())
                .username(testUserDto.getUsername())
                .role(administratorUSER)
                .build();

        User resultedValue = userMapper.toUser(testUserDto);

        Assertions.assertEquals(testUser, resultedValue);
    }

    @Test
    public void shouldReturnTheStringOfTheProvidedRole() {
        Role userRole = Role.builder().name(ERole.NORMAL_USER).build();
        String roleStr = "NORMAL_USER";

        String resultedValue = UserMapper.mapRoleToString(userRole);

        Assertions.assertEquals(roleStr, resultedValue);
    }

    @Test
    public void shouldReturnTheRoleOfTheProvidedString() {
        Role userRole = Role.builder().name(ERole.NORMAL_USER).build();
        String roleStr = "NORMAL_USER";

        Role resultedValue = UserMapper.mapStringToRole(roleStr);

        Assertions.assertEquals(userRole, resultedValue);
    }
}