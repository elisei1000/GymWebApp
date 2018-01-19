package com.gymwebapp.domain.Validator;

import com.gymwebapp.domain.Subscription;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SubscriptionValidator implements Validator<Subscription> {
    @Override
    public List<String> validate(Subscription subscription){
        List<String> errors = new ArrayList<>();

        if(subscription.getEndDate() == null){
            errors.add("End date empty!");
        }

        if(subscription.getEndDate().getTime()>=new Date().getTime()){
            errors.add("End date must be after current date!");
        }
        return errors;
    }
}
