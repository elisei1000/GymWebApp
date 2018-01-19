package com.gymwebapp.service;

import com.gymwebapp.domain.Coach;
import com.gymwebapp.domain.Course;
import com.gymwebapp.domain.RepositoryException;
import com.gymwebapp.domain.Validator.CoachValidator;
import com.gymwebapp.repository.CourseRepository;
import com.gymwebapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vasi on 19.01.2018.
 */
@Service
public class CoachService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CoachValidator coachValidator;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public List<String> addCoach(Coach coach) {
        List<String> errors = coachValidator.validate(coach);

        if (errors.size() != 0) {
            return errors;
        }

        try {
            userRepository.add(coach);
        } catch (RepositoryException e) {
            errors.add("Username already exists!");
        }

        coach.setPassword(passwordEncoder.encode(coach.getPassword()));
        return errors;
    }

    @Transactional
    public List<String> updateCoach(Coach coach) {
        if (coach.getPassword() == null || coach.getPassword().isEmpty())
            coach.setPassword(this.getCoach(coach.getUsername()).getPassword());
        List<String> errors = coachValidator.validate(coach);
        if (errors.size() != 0) {
            return errors;
        }

        try {
            userRepository.update(coach);
        } catch (RepositoryException e) {
            errors.add(e.getMessage());
        }
        return errors;
    }

    @Transactional
    public List<String> removeCoach(Coach coach) {
        List<String> errors = new ArrayList<>();

        if (coach.getUsername() == null)
            errors.add("Username is empty!");
        else {
            if (!coach.getUsername().isEmpty()) {
                try {
                    List<Course> courses = courseRepository.getAll();
                    for (Course c : courses)
                        if (c.getTeacher() != null && c.getTeacher().getUsername().equals(coach.getUsername())) {
                            c.setTeacher(null);
                            courseRepository.update(c);
                        }
                    userRepository.remove(coach.getUsername());
                } catch (RepositoryException e) {
                    errors.add(e.getMessage());
                }
            } else
                errors.add("Username is empty!");
        }
        return errors;
    }

    @Transactional
    public List<Coach> getAllCoaches() {
        return userRepository.getAllCoaches();
    }

    @Transactional
    public Coach getCoach(String username) {
        List<Coach> coaches = userRepository.getAllCoaches();
        if (coaches == null) {
            return null;
        }

        for (Coach coach : coaches) {
            if (coach.getUsername().compareTo(username) == 0) {
                return coach;
            }
        }
        return null;
    }
}
