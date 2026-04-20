package com.afgicafe.flight.config.seeder;

import com.afgicafe.flight.domain.entity.Role;
import com.afgicafe.flight.domain.entity.User;
import com.afgicafe.flight.domain.enums.RoleEnum;
import com.afgicafe.flight.domain.enums.Status;
import com.afgicafe.flight.repository.RoleRepository;
import com.afgicafe.flight.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

@Order(2)
@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("Seeding user...");

        String adminEmail = "lawalaji@email.com";

        if (userRepository.findByEmail(adminEmail).isPresent()) {
            log.info("User already exists");
            return;
        }

        Role adminRole = roleRepository.findByName(RoleEnum.ADMIN)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User admin = new User();
        admin.setFirstName("Lawal");
        admin.setLastName("Aji");
        admin.setEmail(adminEmail);
        admin.setPhoneNumber("09023456789");
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setRole(adminRole);
        admin.setStatus(Status.ACTIVE);

        userRepository.save(admin);

        log.info("User created successfully");
    }
}