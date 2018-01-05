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
            errors.add("Nivelul de dificultate este un numar pozitiv!");
        }
        if(courseModel.getNumberOfParticipants()!=null){
            errors.add("Numarul de participanti nu trebuie dat!");
        }
        if(courseModel.getStartHour()<8 && courseModel.getStartHour()>22){
            errors.add("Ora de inceput trebuie sa fie intre 8-22!");
        }
        if(courseModel.getEndHour()<8 && courseModel.getEndHour()>22){
            errors.add("Ora de final trebuie sa fie intre 8-22!");
        }
        if(courseModel.getEndHour().compareTo(courseModel.getStartHour())<0){
            errors.add("Ora de final este mai mica ca ora de inceput!");
        }
        if(courseModel.getEndDate().compareTo(courseModel.getStartDate())<0){
            errors.add("Data de final este mai mica ca data de inceput!");
        }
        if(courseModel.getTitle()==null || courseModel.getTitle().isEmpty() || courseModel.getTitle()==""){
            errors.add("Titlul nu poate fi gol!");
        }
        if(courseModel.getTeacher()==null || courseModel.getTeacher().isEmpty() || courseModel.getTeacher()==""){
            errors.add("Numele antrenorului nu poate fi gol nu poate fi gol!");
        }
        return errors;
    }
}
