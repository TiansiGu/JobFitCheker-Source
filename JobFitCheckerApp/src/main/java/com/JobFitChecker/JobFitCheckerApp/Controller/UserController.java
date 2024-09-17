package com.JobFitChecker.JobFitCheckerApp.Controller;

import com.JobFitChecker.JobFitCheckerApp.Model.User;
import com.JobFitChecker.JobFitCheckerApp.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/auth") // Base path for all methods in this controller
@CrossOrigin // Allow all origins, you might want to restrict this in a production environment
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // Register a new user
    @PostMapping("/register") // Correct path, as the base path is already /auth
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            // Check if user exists
            if (userService.findByEmail(user.getEmail()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already registered.");
            }

            // Create the user
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during registration.");
        }
    }

    // Login a user
    @PostMapping("/login") // Correct path, as the base path is already /auth
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        try {
            User user = userService.findByEmail(loginRequest.getEmail());
            // Check if user exists and password matches
            if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
            }

            System.out.println("Login successfully");
            // Successfully authenticated
            return ResponseEntity.ok("{\"message\":\"Login successful\"}");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during login.");
        }
    }

}
