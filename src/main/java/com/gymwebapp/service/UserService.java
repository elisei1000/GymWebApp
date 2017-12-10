package com.gymwebapp.service;

import com.gymwebapp.domain.Coach;
import com.gymwebapp.domain.RepositoryException;
import com.gymwebapp.domain.Subscription;
import com.gymwebapp.domain.User;
import com.gymwebapp.domain.Validator.UserValidator;
import com.gymwebapp.repository.SubscriptionRepository;
import com.gymwebapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 18.11.2017.
 */

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserValidator userValidator;


    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public <T extends User> List<String> addUser(T user) {
        List<String> errors;
        errors = userValidator.validate(user);
        if (errors.size() != 0) {
            return errors;
        }

        if (!user.getUsername().isEmpty()) {
            try {
                userRepository.add(user);
            } catch (RepositoryException e) {
                errors.add("Username already exists!");
            }
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return errors;
    }

    @Transactional
    public List<String> checkIfExistUser(User user) {
        List<String> errors = new ArrayList<>();
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            errors.add("Username is empty!");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            errors.add("Password is empty!");
        }

        if (errors.size() != 0) {
            return errors;
        }

        if (!userRepository.checkUserPassword(user)) {
            errors.add("Username or password is incorrect!");
        }
        return errors;
    }

    @Transactional
    public <T extends User> List<String> updateUser(T user) {
        List<String> errors = userValidator.validate(user);
        if (errors.size() != 0) {
            return errors;
        }
        if (!user.getUsername().isEmpty()) {
            try {
                userRepository.update(user);
            } catch (RepositoryException e) {
                errors.add(e.getMessage());
            }
        } else
            errors.add("Username is empty !");
        return errors;
    }

    @Transactional
    public User findUser(String username) {
        return userRepository.get(username);
    }

    @Transactional
    public <T extends User> List<String> removeUser(T user) {
        List<String> errors = new ArrayList<>();
        if (!user.getUsername().isEmpty()) {
            try {
                userRepository.remove(user.getUsername());
            } catch (RepositoryException e) {
                errors.add(e.getMessage());
            }
        } else
            errors.add("Username is empty !");
        return errors;
    }

    @Transactional
    public List<Coach> getAllCoaches() {
        return userRepository.getAllCoaches();
    }
}
