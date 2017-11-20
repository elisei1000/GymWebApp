package com.gymwebapp.service;

import com.gymwebapp.domain.Coach;
import com.gymwebapp.domain.RepositoryException;
import com.gymwebapp.domain.User;
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

    @Transactional
    public <T extends User> List<String> addUser(T user){
        List<String> errors=new ArrayList<String>();

        if(user.getUsername() == null || user.getUsername().isEmpty()){
            errors.add("Username is empty!");
        }

        if(user.getPassword() == null || user.getPassword().isEmpty()){
            errors.add("Password is empty!");
        }

        if(user.getName().isEmpty()){
            errors.add("Name is empty!");
        }

        if (null != user.getEmail()) {
            String regex = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(user.getEmail());
            if (!matcher.matches()) {
                errors.add("Email is not valid!");
            }
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


}
