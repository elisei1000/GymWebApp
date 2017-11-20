package com.gymwebapp.service;

import com.gymwebapp.domain.Coach;
import com.gymwebapp.domain.RepositoryException;
import com.gymwebapp.domain.User;
import com.gymwebapp.domain.Validator.UserValidator;
import com.gymwebapp.model.UserModel;
import com.gymwebapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by david on 18.11.2017.
 */

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserValidator userValidator;

    @Transactional
    public <T extends User> List<String> addUser(T user){
        List<String> errors=new ArrayList<String>();
        errors = userValidator.validate(user);
        if(errors.size() != 0){
            return errors;
        }

        if(!user.getUsername().isEmpty()) {
            try{
                userRepository.add(user);
            } catch (RepositoryException e) {
                errors.add("Username already exists!");
            }
        }
        return errors;
    }


    public List<String> checkIfExistUser(User user) {
        List<String> errors = new ArrayList<>();
        User dbUser = userRepository.get(user.getId());
        return errors;
    }
}
