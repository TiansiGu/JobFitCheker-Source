package com.JobFitChecker.JobFitCheckerApp.Controller;

import com.JobFitChecker.JobFitCheckerApp.Model.User;
import com.JobFitChecker.JobFitCheckerApp.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;

@RestController
// @RequestMapping("/auth") // Base path for all methods in this controller
@CrossOrigin // Allow all origins, you might want to restrict this in a production
             // environment
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

    // Logout a user (optional)
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
        // Get the logged in user fron session
        if (session != null) {
            String loggedInUserName = ((User) session.getAttribute("loggedInUser")).getUsername();
            Long logggedInUserId = ((User) session.getAttribute("loggedInUser")).getUserId();

            System.out.println("line98: Username: " + loggedInUserName);
            System.out.println("line98: id: " + logggedInUserId);

            // Check if user is logged in (exists in session)
            if (loggedInUserName != null) {
                return ResponseEntity.ok(loggedInUserName); // return the user object (or just username if needed)
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
                String loggedInUserName = loggedInUser.getUsername(); // Get username
                String loggedInUserEmail = loggedInUser.getEmail(); // Get email or other details

                if (loggedInUserId != null) {
                    // Create a map to hold the user details
                    Map<String, Object> returnObj = new HashMap<>();
                    returnObj.put("userId", loggedInUserId);
                    returnObj.put("name", loggedInUserName);
                    returnObj.put("email", loggedInUserEmail);
                    // Add more fields if necessary
                    System.out.println("line 132 " + returnObj.toString());

                    return ResponseEntity.ok(returnObj); // Spring will automatically convert Map to JSON
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found in the database.");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user is currently logged in.");
    }

}
