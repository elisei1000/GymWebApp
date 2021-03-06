package com.gymwebapp.service;

import com.gymwebapp.domain.Client;
import com.gymwebapp.domain.RepositoryException;
import com.gymwebapp.domain.User;
import com.gymwebapp.domain.Validator.UserValidator;
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
 * Created by david on 18.11.2017.
 */

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserValidator userValidator;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public <T extends User> List<String> addUser(T user) {
        List<String> errors = userValidator.validate(user);

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
    public <T extends User> List<String> updateUser(T user) {
        List<String> errors = userValidator.validate(user);
        if (errors.size() != 0) {
            return errors;
        }
        if (!user.getUsername().isEmpty() || user.getUsername()!=null) {
            try {
                userRepository.update(user);
            } catch (RepositoryException e) {
                errors.add(e.getMessage());
            }
        } else
            errors.add("Username is empty!");
        return errors;
    }

    @Transactional
    public User findUser(String username) {
        return userRepository.get(username);
    }

    @Transactional
    public <T extends User> List<String> removeUser(T user) {
        List<String> errors = new ArrayList<>();

        if( user.getUsername()==null)
            errors.add("Username is empty!");
        else {
            if (!user.getUsername().isEmpty()) {
                try {
                    userRepository.remove(user.getUsername());
                } catch (RepositoryException e) {
                    errors.add(e.getMessage());
                }
            } else
                errors.add("Username is empty!");
        }
        return errors;
    }

    @Transactional
    public Client getClient(String username) {
        List<Client> clients=userRepository.getAllClients();
        if(clients==null){
            return null;
        }

        for(Client client:clients) {
            if (client.getUsername().compareTo(username)==0) {
                return client;
            }
        }
        return null;
    }

    @Transactional
    public long getSize(){
        return userRepository.size();
    }
}
