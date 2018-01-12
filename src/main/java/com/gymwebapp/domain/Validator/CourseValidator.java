package com.gymwebapp.domain.Validator;

import com.gymwebapp.model.CourseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 06.12.2017.
 */
public class CourseValidator implements Validator<CourseModel>{
    @Override
    public List<String> validate(CourseModel courseModel) {
        List<String> errors = new ArrayList<>();

        if(courseModel.getDifficultyLevel()<0){
            errors.add("Difficulty level shoud be a positive number!");
        }
        if(courseModel.getNumberOfParticipants()!=null){
            errors.add("You shouldn't give number of participants!");
        }
        if(courseModel.getStartHour()<8 && courseModel.getStartHour()>22){
            errors.add("Start hour should be between 8 and 22!");
        }
        if(courseModel.getEndHour()<8 && courseModel.getEndHour()>22){
            errors.add("End hour should be between 8 and 22!");
        }
        if(courseModel.getEndHour().compareTo(courseModel.getStartHour())<0){
            errors.add("Start hour is greater than end hour!");
        }
        if(courseModel.getEndDate().compareTo(courseModel.getStartDate())<0){
            errors.add("Start date is grater than end date!");
        }
        if(courseModel.getTitle()==null || courseModel.getTitle().isEmpty() || courseModel.getTitle()==""){
            errors.add("Title can't be empty!");
        }
        if(courseModel.getTeacher()==null || courseModel.getTeacher().isEmpty() || courseModel.getTeacher()==""){
            errors.add("Coach can't be epty!");
        }
        return errors;
    }
}
