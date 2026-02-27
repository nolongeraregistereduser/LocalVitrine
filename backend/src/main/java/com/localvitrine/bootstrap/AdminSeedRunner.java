package com.localvitrine.bootstrap;

import com.localvitrine.config.AdminSeedProperties;
import com.localvitrine.entity.Role;
import com.localvitrine.entity.RoleName;
import com.localvitrine.entity.User;
import com.localvitrine.entity.UserStatus;
import com.localvitrine.repository.RoleRepository;
import com.localvitrine.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Profile("!test")
@Order(10)
public class AdminSeedRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminSeedProperties adminSeedProperties;

    public AdminSeedRunner(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AdminSeedProperties adminSeedProperties) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminSeedProperties = adminSeedProperties;
    }

    @Override
    public void run(String... args) {
        if (!adminSeedProperties.enabled()) {
            return;
        }
        String email = normalize(adminSeedProperties.email());
        String password = adminSeedProperties.password();
        String fullName = adminSeedProperties.fullName() == null || adminSeedProperties.fullName().isBlank()
                ? "LocalVitrine Admin"
                : adminSeedProperties.fullName().trim();

        if (email == null || password == null || password.isBlank()) {
            return;
        }

        Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                .orElseGet(() -> roleRepository.save(Role.builder().name(RoleName.ADMIN).build()));

        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(adminRole);
        userRepository.save(user);
    }

    private static String normalize(String email) {
        if (email == null) {
            return null;
        }
        String value = email.trim().toLowerCase(Locale.ROOT);
        return value.isBlank() ? null : value;
    }
}
