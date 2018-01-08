package com.gymwebapp.repository;


import static java.lang.Math.toIntExact;
import static org.assertj.core.api.Assertions.assertThat;

import com.gymwebapp.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@DataJpaTest
public class FeedBackRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private FeedBackRepository feedBackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Before
    public void setUp() throws Exception {

        userRepository.add(new Client("client1","a","a","a", new Date()));
        userRepository.add(new Client("client2","a","a","a", new Date()));
        userRepository.add(new Client("client3","a","a","a", new Date()));


        feedBackRepository.add(new Feedback(4,"a","a<",new Date(),(Client)userRepository.get("client1")));
        feedBackRepository.add(new Feedback(3,"a","a<",new Date(),(Client) userRepository.get("client2")));
        feedBackRepository.add(new Feedback(2,"a","a<",new Date(),(Client) userRepository.get("client3")));
        feedBackRepository.add(new Feedback(5,"a","a<",new Date(),(Client) userRepository.get("client1")));
        feedBackRepository.add(new Feedback(5,"a","a<",new Date(),(Client)userRepository.get("client1")));
        feedBackRepository.add(new Feedback(4,"a","a<",new Date(),(Client)userRepository.get("client2")));
        //  size = 6
    }


    @Test
    public void testAddNewFeedbackExistingClientShouldAddClientInRepository(){

        try {
            long size= feedBackRepository.size();
            feedBackRepository.add(new Feedback(4, "asda", "null", new Date(), (Client) userRepository.get("client1")));

            assertThat(feedBackRepository.size()).isEqualTo(size+1);

            List<Feedback> feedbacks = feedBackRepository.getAll();
            Feedback feedback = feedbacks.get(feedbacks.size()-1);

            assertThat(feedback.getAuthor()).isEqualTo(userRepository.get("client1"));
            assertThat(feedback.getDetails()).isEqualTo("null");
            assertThat(feedback.getSummary()).isEqualTo("asda");
            assertThat(feedback.getStarsCount()).isEqualTo(4);

        }catch (RepositoryException e){
            assert(false);
        }
    }

    @Test
    public void testUpdateFeedBackExistingShouldUpdate(){

        try{

            Feedback feedback = new Feedback(4, "aaaa", "Aaaa", new Date(), (Client) userRepository.get("client2"));
            int id = feedBackRepository.getAll().get(0).getId();
            feedback.setId(id);

            feedBackRepository.update(feedback);
            feedback = feedBackRepository.get(id);

            assertThat(feedback.getStarsCount()).isEqualTo(4);
            assertThat(feedback.getSummary()).isEqualTo("aaaa");
            assertThat(feedback.getDetails()).isEqualTo("Aaaa");
            assertThat(feedback.getAuthor()).isEqualTo(userRepository.get("client2"));

        }catch (RepositoryException e){
            assert(false);
        }
    }

    @Test
    public void testUpdateFeedBackNotExistingShouldThrowRepositoryException(){

        try{

            Feedback feedback = new Feedback(4, "aaaa", "Aaaa", new Date(), (Client) userRepository.get("client2"));
            feedback.setId(-1);

            feedBackRepository.update(feedback);
            assert(false);

        }catch (RepositoryException e){
            assert(true);
        }
    }

    @Test
    public void testFeedbackDeleteExistingShouldDelete(){


        try{
            long size = feedBackRepository.size();
            assertThat(feedBackRepository.size()).isEqualTo(size);

            int id = feedBackRepository.getAll().get(0).getId();
            feedBackRepository.remove(id);

            assertThat(feedBackRepository.size()).isEqualTo(size-1);
        }catch (RepositoryException e){
            assert(false);
        }

    }

    @Test
    public void testFeedBackDeleteNotExistingShouldThrowRepositoryException(){
        try{

            feedBackRepository.remove(-1);
            assert(false);
        }catch (RepositoryException e){
            assert(true);
        }
    }

    @Test
    public void testGetAllClientFeedBacksShouldReturnList(){

        List<Feedback> feedbacks = feedBackRepository.getAllClientFeedbacks((Client)userRepository.get("client1"));
        assertThat(feedbacks.size()).isEqualTo(3);
    }

    @Test
    public void testGetAllCourseFeedbacksShouldReturnList(){
        try {
            userRepository.add(new Coach("coach1", "pass"));
            feedBackRepository.add(new CoachFeedback(5, "aa", "asda", new Date()
                    , (Client) userRepository.get("client2")
                    , (Coach)userRepository.get("coach1")));

            List<CoachFeedback> courseFeedbacks = feedBackRepository.getAllCoachFeedbacks();
            assertThat(courseFeedbacks.size()).isEqualTo(1);

        }catch (RepositoryException e){
            assert(false);
        }
    }

    @Test
    public void testGetAllCoachFeedbacksShouldReturnList(){

        try {
            userRepository.add(new Coach("coach1", "pass"));
            courseRepository.add(new Course("a","a",4,4,4,new Date(), new Date(),10,(Coach)userRepository.get("coach1")));
            List<Course> courseList = courseRepository.getAll();
            int id = courseList.get(0).getId();
            feedBackRepository.add(new CourseFeedback(4,"a","a",new Date(),(Client)userRepository.get("client1"), courseRepository.get(id) ));

            List<CourseFeedback> courseFeedbackList = feedBackRepository.getAllCourseFeedbacks();
            assertThat(courseFeedbackList.size()).isEqualTo(1);

        }catch (RepositoryException e){
            assert(false);
        }

    }

}