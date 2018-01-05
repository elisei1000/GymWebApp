package com.gymwebapp.domain.Validator;

import com.gymwebapp.model.FeedbackModel;

import java.util.ArrayList;
import java.util.List;

public class FeedbackValidator implements Validator<FeedbackModel> {

    @Override
    public List<String> validate(FeedbackModel feedbackModel) {
        List<String> errors = new ArrayList<>();

        if (feedbackModel.getAuthor() != null) {
            errors.add("Autorul nu trebuie dat!");
        }
        if (feedbackModel.getDate() != null) {
            errors.add("Data nu trebuie data!");
        }

        if (feedbackModel.getStarsCount() < 0 && feedbackModel.getStarsCount() > 5) {
            errors.add("Numarul de stele este intre 0 si 5");
        }

        return errors;
    }
}
