package ro.fortech.allocation.security.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ro.fortech.allocation.security.role.model.ERole;
import ro.fortech.allocation.security.role.model.Role;
import ro.fortech.allocation.security.user.api.dto.UserDto;
import ro.fortech.allocation.security.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", source = "role", qualifiedByName = "roleToString")
    UserDto toDTO(User user);

    @Mapping(target = "role", source = "role", qualifiedByName = "stringToRole")
    User toUser(UserDto userDTO);

    @Named("roleToString")
    static String mapRoleToString(Role role) {
        return role.getName().name();
    }

    @Named("stringToRole")
    static Role mapStringToRole(String roleStr) {
        Role role = Role.builder().name(ERole.valueOf(roleStr)).build();
        return role;
    }

}
