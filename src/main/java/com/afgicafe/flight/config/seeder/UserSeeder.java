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
        String phone = "09023456789";

        Role adminRole = roleRepository.findByName(RoleEnum.ADMIN)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        userRepository.findByEmail(adminEmail)
                .orElseGet(() -> {

                    if (userRepository.existsByPhoneNumber(phone)) {
                        log.warn("Phone number already exists.");
                        return null;
                    }

                    User admin = new User();
                    admin.setFirstName("Lawal");
                    admin.setLastName("Aji");
                    admin.setEmail(adminEmail);
                    admin.setPhoneNumber(phone);
                    admin.setPassword(passwordEncoder.encode("password"));
                    admin.setRole(adminRole);
                    admin.setStatus(Status.ACTIVE);

                    log.info("User created successfully");
                    return userRepository.save(admin);
                });
    }
}