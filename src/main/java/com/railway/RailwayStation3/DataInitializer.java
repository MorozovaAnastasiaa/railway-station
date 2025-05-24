package com.railway.RailwayStation3;

import com.railway.RailwayStation3.repository.User;
import com.railway.RailwayStation3.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;

    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        createUserIfNotExists(
                "admin",
                "admin",
                "admin@railway.com",
                "+79991112233",
                "ROLE_ADMIN"
        );

        createUserIfNotExists(
                "user1",
                "user1",
                "user@railway.com",
                "+79997778899",
                "ROLE_USER"
        );
    }

    private void createUserIfNotExists(String username, String password,
                                       String email, String phone, String role) {
        if (!userService.userExists(username)) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setPhone(phone);
            user.setRole(role);

            userService.registerUser(user);
            System.out.println("Created " + role + ": " + username);
        }
    }
}