package com.gymwebapp.service;

import com.gymwebapp.domain.RepositoryException;
import com.gymwebapp.domain.User;
import com.gymwebapp.domain.Validator.UserValidator;
import com.gymwebapp.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 18.11.2017.
 */

@Service
public class ClientService {

    @Autowired
    private ClientRepository userRepository;

    @Autowired
    private UserValidator userValidator;

    @Transactional
    public <T extends User> List<String> addUser(User user){
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

    @Transactional
    public List<String> checkIfExistUser(User user) {
        List<String> errors = new ArrayList<>();
        errors = userValidator.validate(user);

        if(errors.size()!=0){
            return errors;
        }

        if(!userRepository.checkIfUserExists(user)){
            errors.add("Username or password is incorrect!");
        }
        return errors;
    }
}
