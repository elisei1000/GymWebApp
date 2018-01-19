package com.gymwebapp.domain.Validator;

import com.gymwebapp.domain.User;

import java.util.*;
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
        } else {
            String regex = "^([A-Za-z_][A-Za-z0-9_]{3,})$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(user.getUsername());
            if (!matcher.matches()) {
                errors.add("Username must contain minimum 4 characters, start with _ or a letter!");
            }
        }

        if(user.getPassword() == null || user.getPassword().isEmpty()){
            errors.add("Password is empty!");
        } else {
            String regex = "^((?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#!@$%^&*+-]).{6,})$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(user.getPassword());
            if (!matcher.matches()) {
                errors.add("Password must contain minimum 6 characters, at least one uppercase letter, one lowercase letter, one number and one special character!");
            }
        }

        if(user.getName() == null || user.getName().isEmpty()){
            errors.add("Name is empty!");
        } else {
            String regex = "^((?=.*?[A-Za-z])(?=.*?[ ]).{5,})$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(user.getName());
            if (!matcher.matches()) {
                errors.add("Name must contain minimum 4 letter and a space!");
            }
        }

        if(user.getBirthDay() == null || user.getBirthDay().getTime() == 0
                || user.getBirthDay().getTime() >= new Date().getTime()
                ) {
            errors.add("BirthDay is invalid!");
        } else {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(user.getBirthDay());
            int year = calendar.get(Calendar.YEAR);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            if (currentYear - year < 12)
                errors.add("You must be at least 12 years old!");
        }

        if(user.getEmail()== null || user.getEmail().isEmpty()){
            errors.add("Email is empty!");
        }
        else {
            String regex = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(user.getEmail());
            if (!matcher.matches()) {
                errors.add("Email is invalid!");
            }
        }

        return errors;
    }
}
