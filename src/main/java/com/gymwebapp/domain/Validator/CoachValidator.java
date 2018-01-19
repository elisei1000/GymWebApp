package com.gymwebapp.domain.Validator;

import com.gymwebapp.domain.Coach;

import java.util.List;

/**
 * Created by vasi on 19.01.2018.
 */
public class CoachValidator extends UserValidator {

    public List<String> validate(Coach coach) {
        UserValidator userValidator = new UserValidator();
        List<String> errors = userValidator.validate(coach);

        if (coach.getAbout() == null) {
            errors.add("About is null!");
        }

        return errors;
    }
}
