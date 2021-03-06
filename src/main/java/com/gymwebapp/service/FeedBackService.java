package com.gymwebapp.service;

import com.gymwebapp.domain.*;
import com.gymwebapp.model.FeedbackModel;
import com.gymwebapp.repository.FeedBackRepository;
import com.gymwebapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vasi on 26.11.2017.
 */
@Service
public class FeedBackService {

    @Autowired
    private FeedBackRepository feedBackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public List<CoachFeedback> getAllCoachFeedBacks(String coach_username) {
        List<CoachFeedback> coachFeedbacks = new ArrayList<>();
        for (CoachFeedback cf : feedBackRepository.getAllCoachFeedbacks())
            if (cf.getCoach().getUsername().equals(coach_username))
                coachFeedbacks.add(cf);
        return coachFeedbacks;
    }

    @Transactional
    public List<CourseFeedback> getAllCourseFeedBacks(Integer id){
        List<CourseFeedback> feedbacks=new ArrayList<>();
        for(CourseFeedback cf:feedBackRepository.getAllCourseFeedbacks()){
            if(cf.getCourse().getId()==id){
                feedbacks.add(cf);
            }
        }

        return feedbacks;
    }

    @Transactional
    public void deleteCourseFeedback(Integer id) throws RepositoryException {
        feedBackRepository.remove(id);
    }

    @Transactional
    public long size(){
        return feedBackRepository.size();
    }

    @Transactional
    public Integer getLastId(){
        return feedBackRepository.getLastGeneratedValue();
    }

    @Transactional
    public List<String> addCoachFeedback(String username, FeedbackModel feedbackModel) {
        List<String> errors = new ArrayList<>();
        Coach coach = (Coach) userRepository.get(username);

        if (coach == null) {
            errors.add("Given coach doesn't exists!");
            return errors;
        }

        List<CoachFeedback> feedbacks = coach.getFeedbacks();

        for (CoachFeedback feedback : feedbacks) {
            String author = "";
            if (feedback.getAuthor() != null) {
                author = feedback.getAuthor().getUsername();
            }
            if (author.compareTo(feedbackModel.getAuthor()) == 0) {
                errors.add("The user already has given feedback!");
                return errors;
            }
        }

        Client client = userService.getClient(feedbackModel.getAuthor());
        Feedback feedback = new CoachFeedback(feedbackModel.getStarsCount(), feedbackModel.getSummary(), feedbackModel.getDetails(), feedbackModel.getDate(), client, coach);

        try {
            feedBackRepository.add(feedback);
        } catch (RepositoryException e) {
            errors.add("System error!");
        }
        return errors;
    }

    @Transactional
    public List<String> modifyCoachFeedback(String username, FeedbackModel feedbackModel) {
        List<String> errors = new ArrayList<>();
        Coach coach = (Coach) userRepository.get(username);

        if (coach == null) {
            errors.add("Given coach doesn't exists!");
            return errors;
        }

        List<CoachFeedback> feedbacks = coach.getFeedbacks();
        Feedback feedbackModified = null;

        for (CoachFeedback feedback : feedbacks) {
            String author = "";
            if (feedback.getAuthor() != null) {
                author = feedback.getAuthor().getUsername();
            }
            if (author.compareTo(feedbackModel.getAuthor()) == 0) {
                feedbackModified = feedback;
            }
        }

        if (feedbackModified == null) {
            errors.add("User didn't give feedback!");
            return errors;
        }

        feedbackModified.setDate(feedbackModel.getDate());
        feedbackModified.setDetails(feedbackModel.getDetails());
        feedbackModified.setStarsCount(feedbackModel.getStarsCount());
        feedbackModified.setSummary(feedbackModel.getSummary());

        try {
            feedBackRepository.update(feedbackModified);
        } catch (RepositoryException e) {
            errors.add("System error!");
        }
        return errors;
    }

    @Transactional
    public List<String> deleteCoachFeedback(String username, String client) {
        List<String> errors = new ArrayList<>();
        Coach coach = (Coach) userRepository.get(username);

        if (coach == null) {
            errors.add("Given coach doesn't exists!");
            return errors;
        }

        List<CoachFeedback> feedbacks = coach.getFeedbacks();
        Integer idFeedback = null;

        for (Feedback feedback : feedbacks) {
            if (feedback.getAuthor().getUsername().compareTo(client) == 0) {
                idFeedback = feedback.getId();
                break;
            }
        }

        if (idFeedback == null) {
            errors.add("Feedback doesn't exists!");
            return errors;
        }

        try {
            feedBackRepository.remove(idFeedback);
        } catch (RepositoryException e) {
            errors.add("System error!");
        }
        return errors;
    }
}
