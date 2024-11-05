package com.JobFitChecker.JobFitCheckerApp.controllers;

import com.JobFitChecker.JobFitCheckerApp.model.entities.User;
import com.JobFitChecker.JobFitCheckerApp.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin // Allow all origins, might want to restrict this if it is a production environment
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // Register a new user
    @PostMapping("/register")
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
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest, HttpServletRequest request) {
        try {
            User user = userService.findByEmail(loginRequest.getEmail());
            // Check if user exists and password matches
            if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
            }

            // Retrieve the session and set the logged-in user attribute
            HttpSession session = request.getSession();
            session.setAttribute("loggedInUser", user);
            System.out.println("line66: Username: " + ((User) session.getAttribute("loggedInUser")).getUsername());
            System.out.println("line66: Email: " + ((User) session.getAttribute("loggedInUser")).getEmail());

            System.out.println("line66: " + session.getAttribute("loggedInUser").toString());

            System.out.println("Session ID after login: " + session.getId()); // Log session ID

            System.out.println("Login successfully");
            // Successfully authenticated
            return ResponseEntity.ok("{\"message\":\"Login successful\"}");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during login.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpSession session) {
        // Invalidate session to log the user out
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully");
    }

    // Get currently logged-in user
    @GetMapping("/currentUser")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // false to prevent creating a new session if none exists
        System.out.println("line 91 : " + session);
        // Get the logged in user from session
        if (session != null) {
            String loggedInUserName = ((User) session.getAttribute("loggedInUser")).getUsername();
            Long loggedInUserId = ((User) session.getAttribute("loggedInUser")).getUserId();

            System.out.println("line98: Username: " + loggedInUserName);
            System.out.println("line98: id: " + loggedInUserId);

            // Check if user is logged in (exists in session)
            if (loggedInUserName != null) {
                return ResponseEntity.ok(loggedInUserName); // return the username
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user is currently logged in.");
    }

    // Fetch the logged-in user's profile from the database
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // false prevents creating a new session
        if (session != null) {
            User loggedInUser = (User) session.getAttribute("loggedInUser");

            if (loggedInUser != null) {
                Long loggedInUserId = loggedInUser.getUserId();
                User userProfile = userService.getUserProfile(loggedInUserId);

                if (userProfile != null) {
                    // Create a map to hold the user details
//                    Map<String, Object> returnObj = new HashMap<>();
//                    returnObj.put("userId", loggedInUserId);
//                    returnObj.put("email", userProfile.getEmail());

                    return ResponseEntity.ok(userProfile); // Spring will automatically convert User object to JSON
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found in the database.");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user is currently logged in.");
    }

    // Update Profile information in the database
    @PutMapping("/update-profile")
    public ResponseEntity<String> updateProfile(@RequestBody User user, HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            User loggedInUser = (User) session.getAttribute("loggedInUser");

            if (loggedInUser != null) {
                Long loggedInUserId = loggedInUser.getUserId();
                if (loggedInUserId != null) {
                    try {
                        userService.updateUserProfile(loggedInUserId, user);
                        return ResponseEntity.status(HttpStatus.OK).body("Profile update succeeded");
                    } catch (Exception ex) {
                        log.error("Failed to update profile for user {}: ", loggedInUserId, ex);
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Update failed");
                    }
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user is currently logged in.");
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        System.out.println("The email to check: " + email);
        try {
            if (!(email.isEmpty())) {
                boolean emailExists = userService.findByEmail(email) != null;
                return ResponseEntity.ok(emailExists);
            }
            return ResponseEntity.badRequest().body(false);
        } catch  (Exception ex)  {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

}
