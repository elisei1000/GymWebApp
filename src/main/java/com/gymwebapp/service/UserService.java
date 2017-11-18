package com.gymwebapp.service;

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
    public List<String> addUser(UserModel userModel){
        List<String> errors=new ArrayList<String>();

        User user=new User(userModel.getUsername(),Integer.toString(userModel.getPassword().hashCode()),userModel.getEmail(),userModel.getName(),userModel.getBirthDay());

        if(userModel.getUsername().isEmpty()){
            errors.add("Username is empty!");
        }

        if(userModel.getPassword().isEmpty()){
            errors.add("Password is empty!");
        }

        if(userModel.getName().isEmpty()){
            errors.add("Password is empty!");
        }

        if (null != userModel.getEmail()) {
            String regex = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(userModel.getEmail());
            if (!matcher.matches()) {
                errors.add("Email is not valid!");
            }
        }

        if(!userModel.getUsername().isEmpty()) {
            if (!this.userRepository.checkIfUsernameExists(user)) {
                userRepository.add(user);
            } else {
                errors.add("Username already exists!");
            }
        }

        return errors;
    }

    @Transactional
    public List<String> checkIfExistUser(UserModel userModel){
        List<String> errors=new ArrayList<String>();

        User user=new User(userModel.getUsername(),Integer.toString(userModel.getPassword().hashCode()),userModel.getEmail(),userModel.getName(),userModel.getBirthDay());

        if(userModel.getUsername().isEmpty()){
            errors.add("Username is empty!");
        }

        if(userModel.getPassword().isEmpty()){
            errors.add("Password is empty!");
        }
        if(errors.size()==0) {
            if (!this.userRepository.checkIfUserExists(user)) {
                errors.add("Username or password incorrect!");
            }
        }

        return errors;
    }
}
