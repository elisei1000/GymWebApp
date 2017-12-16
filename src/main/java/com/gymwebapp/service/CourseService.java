package com.gymwebapp.service;

import com.gymwebapp.domain.*;
import com.gymwebapp.model.CourseModel;
import com.gymwebapp.model.FeedbackModel;
import com.gymwebapp.repository.CourseRepository;
import com.gymwebapp.repository.FeedBackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private FeedBackRepository feedBackRepository;

    @Transactional
    public List<CourseModel> getAll() {
        List<CourseModel> courseModelList = new ArrayList<>();

        for (Course course : courseRepository.getAll()) {
            String teacher = null;
            if (course.getTeacher() != null) {
                teacher = course.getTeacher().getUsername();
            }
            courseModelList.add(new CourseModel(course.getId(), course.getDifficultyLevel(), course.getStartHour(), course.getEndHour(), course.getStartDate(), course.getEndDate(), course.getMaxPlaces(), course.getClients().size(), teacher, course.getTitle(), course.getDescription()));

        }

        return courseModelList;
    }

    @Transactional
    public CourseModel getCourse(Integer id) {
        Course course = courseRepository.get(id);

        if (course == null) {
            return new CourseModel();
        } else {
            String teacher = null;
            if (course.getTeacher() != null)
                teacher = course.getTeacher().getUsername();
            return new CourseModel(course.getId(), course.getDifficultyLevel(), course.getStartHour(), course.getEndHour(), course.getStartDate(), course.getEndDate(), course.getMaxPlaces(), course.getClients().size(), teacher, course.getTitle(), course.getDescription());
        }
    }

    @Transactional
    public void addCourse(CourseModel courseModel, Coach teacher) throws RepositoryException {
        Course course = new Course(courseModel.getTitle(), courseModel.getDescription(), courseModel.getDifficultyLevel(), courseModel.getStartHour(), courseModel.getEndHour(), courseModel.getStartDate(), courseModel.getEndDate(), courseModel.getMaxPlaces(), teacher);

        courseRepository.add(course);
    }

    @Transactional
    public void modifyCourse(CourseModel courseModel, Coach teacher) throws RepositoryException {
        Course course = courseRepository.get(courseModel.getId());
        course.setTitle(courseModel.getTitle());
        course.setDescription(courseModel.getDescription());
        course.setDifficultyLevel(courseModel.getDifficultyLevel());
        course.setStartHour(courseModel.getStartHour());
        course.setEndHour(courseModel.getEndHour());
        course.setStartDate(courseModel.getStartDate());
        course.setEndDate(courseModel.getEndDate());
        course.setMaxPlaces(courseModel.getMaxPlaces());
        course.setTeacher(teacher);

        courseRepository.update(course);
    }

    @Transactional
    public void attendUserToCourse(Integer id, Client client) throws RepositoryException {
        Course course = courseRepository.get(id);
        if (course == null) {
            throw new RepositoryException("");
        }
        List<Client> clients = course.getClients();

        clients.add(client);

        courseRepository.update(course);
    }

    @Transactional
    public void deleteCourse(Integer id) throws RepositoryException {
        courseRepository.remove(id);
    }

    @Transactional
    public List<String> addFeedback(Integer id,FeedbackModel feedbackModel) {
        List<String> errors = new ArrayList<>();

        Course course = courseRepository.get(id);

        if (course == null) {
            errors.add("Cursul dat nu exista!");
            return errors;
        }

        List<CourseFeedback> feedbacks = course.getFeedbacks();

        for (CourseFeedback feedback : feedbacks) {
            String author="";
            if(feedback.getAuthor()!=null){
                author=feedback.getAuthor().getUsername();
            }
            if (author.compareTo(feedbackModel.getAuthor()) == 0) {
                errors.add("Utilizatorul a dat deja feedback!");
                return  errors;
            }
        }

        Feedback feedback=new CourseFeedback(feedbackModel.getStarsCount(),feedbackModel.getSummary(),feedbackModel.getDetails(),feedbackModel.getDate(),null,course);

        try {
            feedBackRepository.add(feedback);
        } catch (RepositoryException e) {
            errors.add("Eroare de sistem!");
        }
        return errors;
    }

    @Transactional
    public List<String> modifyFeedback(Integer id,FeedbackModel feedbackModel) {
        List<String> errors = new ArrayList<>();

        Course course = courseRepository.get(id);

        if (course == null) {
            errors.add("Cursul dat nu exista!");
            return errors;
        }

        List<CourseFeedback> feedbacks = course.getFeedbacks();

        Feedback feedbackModified=null;

        for (CourseFeedback feedback : feedbacks) {
            String author="";
            if(feedback.getAuthor()!=null){
                author=feedback.getAuthor().getUsername();
            }
            if (author.compareTo(feedbackModel.getAuthor()) == 0) {
                feedbackModified=feedback;
            }
        }

        if(feedbackModified==null) {
            errors.add("Utilizatorul nu a dat feedback!");
            return errors;
        }

        feedbackModified.setDate(new Date());
        feedbackModified.setDetails(feedbackModel.getDetails());
        feedbackModified.setStarsCount(feedbackModel.getStarsCount());
        feedbackModified.setSummary(feedbackModel.getSummary());

        try {
            feedBackRepository.update(feedbackModified);
        } catch (RepositoryException e) {
            errors.add("Eroare de sistem!");
        }
        return errors;
    }
}
