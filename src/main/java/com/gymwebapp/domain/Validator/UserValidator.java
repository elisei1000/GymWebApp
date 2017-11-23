package com.gymwebapp.domain.Validator;

import com.gymwebapp.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by elisei on 20.11.2017.
 */
public class UserValidator implements Validator<User> {
    @Override
    public List<String> validate(User user) {
        List<String> errors = new ArrayList<>();

        if(user.getUsername() == null || user.getUsername().isEmpty()){
            errors.add("Username is empty!");
        }

        if(user.getPassword() == null || user.getPassword().isEmpty()){
            errors.add("Password is empty!");
        }

        if(user.getName()!= null && user.getName().isEmpty()){
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
        return errors;
    }
}
