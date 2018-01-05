package com.gymwebapp.Service;

import com.gymwebapp.domain.*;
import com.gymwebapp.model.CourseModel;
import com.gymwebapp.model.FeedbackModel;
import com.gymwebapp.service.CourseService;
import com.gymwebapp.service.FeedBackService;
import com.gymwebapp.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class FeedbackServiceTest {

    @Autowired
    private FeedBackService feedBackService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;


    @Test
    public void testAddNewFeedBackDeleteGetAllCourseFeedbacks(){
        try{
            Client client = new Client("testCclient3", "password", "email@yahoo.com", "Elisei",
                    new Date(1000));
            userService.addUser(client);

            CourseModel courseModel = new CourseModel(0
                    , 2
                    , 4
                    , 5
                    , new Date()
                    , new Date()
                    , 10
                    , 0
                    , "userCtest1"
                    , "Title1"
                    , "no desc");

            courseService.addCourse(courseModel, userService.getCoach("userCtest1"));
            courseService.attendUserToCourse(courseService.getLastId(), client);

            List<Client> clients = courseService.getAllClientsForCourse(courseService.getLastId());

            assertThat(clients.size()).isEqualTo(1);
            assertThat(clients.get(0).getUsername()).isEqualTo("testCclient3");

            FeedbackModel feedbackModel = new FeedbackModel(0, 3, "summary", "details", new Date(),"testCclient3" );
            List<String> errors = courseService.addFeedback(courseService.getLastId(), feedbackModel);

            List<CourseFeedback> feedbacks = feedBackService.getAllCourseFeedBacks(courseService.getLastId());
            assertThat(feedbacks.size()).isEqualTo(1);

            feedBackService.deleteCourseFeedback(feedBackService.getLastId());
            feedbacks = feedBackService.getAllCourseFeedBacks(courseService.getLastId());
            assertThat(feedbacks.size()).isEqualTo(0);
        }catch (RepositoryException e){
            assert(false);
        }
    }

    @Test
    public void testGetAllCoachFeedbacks(){

        Coach coach = new Coach("testCclient3", "password", "email@yahoo.com", "Elisei",
                new Date(1000));
        userService.addUser(coach);
        List<CoachFeedback> coachFeedbacks = feedBackService.getAllCoachFeedBacks("testCclient3");
        assertThat(coachFeedbacks.size()).isEqualTo(0);

    }

}
