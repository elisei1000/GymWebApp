package com.gymwebapp.domain.Validator;

import com.gymwebapp.model.FeedbackModel;

import java.util.ArrayList;
import java.util.List;

public class FeedbackValidator implements Validator<FeedbackModel> {

    @Override
    public List<String> validate(FeedbackModel feedbackModel) {
        List<String> errors = new ArrayList<>();

        if (feedbackModel.getAuthor() != null) {
            errors.add("Author must be empty!");
        }
        if (feedbackModel.getDate() != null) {
            errors.add("Data must be empty!");
        }

        if (feedbackModel.getStarsCount() < 0 && feedbackModel.getStarsCount() > 5) {
            errors.add("Number o starts should be between 0 and 5");
        }

        return errors;
    }
}
