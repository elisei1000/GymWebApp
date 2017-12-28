package com.gymwebapp.service;

import com.gymwebapp.domain.CoachFeedback;
import com.gymwebapp.domain.Course;
import com.gymwebapp.domain.CourseFeedback;
import com.gymwebapp.domain.RepositoryException;
import com.gymwebapp.repository.FeedBackRepository;
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
}
