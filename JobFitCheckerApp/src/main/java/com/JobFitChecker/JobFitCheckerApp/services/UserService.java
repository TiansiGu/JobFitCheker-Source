package com.JobFitChecker.JobFitCheckerApp.services;

import com.JobFitChecker.JobFitCheckerApp.model.entities.User;
import com.JobFitChecker.JobFitCheckerApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;


@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        // Encrypt the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User getUserProfile(long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        return null;
    }

    public void updateUserProfile(long id, User user) {
        Optional<User> optionalExistedUser = userRepository.findById(id);
        if (optionalExistedUser.isPresent()) {
            User existedUser = optionalExistedUser.get();
            if ((user.getEmail() != null))
                existedUser.setEmail(user.getEmail());
            if (user.getFirstName() != null)
                existedUser.setFirstName(user.getFirstName());
            if (user.getLastName() != null)
                existedUser.setLastName(user.getLastName());
            if (user.getPhoneNumber() != null)
                existedUser.setPhoneNumber(user.getPhoneNumber());
            if (user.getDegree() != null)
                existedUser.setDegree(user.getDegree());
            if (user.getNeedSponsor() != null)
                existedUser.setNeedSponsor(user.getNeedSponsor());

            userRepository.save(existedUser);
        }
    }
}