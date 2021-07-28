package ro.fortech.allocation.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ro.fortech.allocation.security.role.model.ERole;
import ro.fortech.allocation.security.role.model.Role;
import ro.fortech.allocation.security.role.repository.RoleRepository;
import ro.fortech.allocation.security.user.model.User;
import ro.fortech.allocation.security.user.repository.UserRepository;

import javax.persistence.EntityNotFoundException;

@Component
@RequiredArgsConstructor
@Profile("dev")
public class Bootstrapper implements ApplicationListener<ApplicationReadyEvent> {

    @Value("${app.bootstrap}")
    private boolean bootstrap;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (bootstrap) {

            userRepository.deleteAll();
            roleRepository.deleteAll();

            for (ERole role : ERole.values()) {
                Role toBeSavedRole = Role.builder().name(role).build();
                roleRepository.save(toBeSavedRole);
            }

            Role administratorRole = roleRepository.findByName(ERole.ADMINISTRATOR)
                    .orElseThrow(() -> new EntityNotFoundException("Couldn't find ADMINISTRATOR role!"));
            Role userRole = roleRepository.findByName(ERole.NORMAL_USER)
                    .orElseThrow(() -> new EntityNotFoundException("Couldn't find NORMAL_USER role!"));
            Role managerRole = roleRepository.findByName(ERole.MANAGER)
                    .orElseThrow(() -> new EntityNotFoundException("Couldn't find MANAGER role!"));


            User bootstrappedAdmin = User.builder()
                    .username("AdminOne")
                    .email("Email@Admin.com")
                    .password(passwordEncoder.encode("PasswordOne"))
                    .role(administratorRole).build();

            User bootstrappedManager = User.builder()
                    .username("ManagerOne")
                    .email("Email@Manager.com")
                    .password(passwordEncoder.encode("PasswordOne"))
                    .role(managerRole).build();

            User bootstrappedUser = User.builder()
                    .username("NormalUserOne")
                    .email("Email@User.com")
                    .password(passwordEncoder.encode("PasswordOne"))
                    .role(userRole).build();

            userRepository.save(bootstrappedAdmin);
            userRepository.save(bootstrappedUser);
            userRepository.save(bootstrappedManager);
        }
    }
}