package com.gymwebapp.Service;

import com.gymwebapp.domain.*;
import com.gymwebapp.model.CourseModel;
import com.gymwebapp.model.FeedbackModel;
import com.gymwebapp.repository.CourseRepository;
import com.gymwebapp.repository.FeedBackRepository;
import com.gymwebapp.repository.UserRepository;
import com.gymwebapp.service.CourseService;
import com.gymwebapp.service.FeedBackService;
import com.gymwebapp.service.UserService;
import com.sun.org.apache.regexp.internal.RE;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@RunWith(SpringRunner.class)
@DataJpaTest
public class CourseServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private FeedBackService feedBackService;

    @Test
    public void testAddNewCourseShouldAdd(){

        userService.addUser(new Coach("userCtest1", "pass", "coach@yahoo.com", "grigore", new Date()));

        CourseModel courseModel = new CourseModel(0
                ,2
                ,4
                ,5
                ,new Date()
                , new Date()
                , 10
                , 12
                , "userCtest1"
                , "Title1"
                ,"no desc");

        long dim = courseService.size();

        try {
            courseService.addCourse(courseModel, userService.getCoach("userCtest1"));
            assertThat(courseService.size()).isEqualTo(dim+1);

            courseService.deleteCourse(courseService.getLastId());
            assertThat(courseService.size()).isEqualTo(dim);

            User user = new User();
            user.setUsername("userCtest1");
            userService.removeUser(user);

        }catch (RepositoryException e)
        {
            assertThat(true).isFalse();
        }
    }

    @Test
    public void testGetCourseShouldGet(){

        try{

            CourseModel courseModel = new CourseModel(0
                    ,2
                    ,4
                    ,5
                    ,new Date()
                    , new Date()
                    , 10
                    , 12
                    , "userCtest1"
                    , "Title1"
                    ,"no desc");

            long dim = courseService.size();

            courseService.addCourse(courseModel, userService.getCoach("userCtest1"));
            assertThat(courseService.size()).isEqualTo(dim+1);

            CourseModel courseModel1 = courseService.getCourse(courseService.getLastId());
            assertThat(courseModel1.getStartHour()).isEqualTo(4);
            assertThat(courseModel1.getEndHour()).isEqualTo(5);
            assertThat(courseModel1.getTitle()).isEqualTo("Title1");

            courseService.deleteCourse(courseService.getLastId());
            assertThat(courseService.size()).isEqualTo(dim);

        }catch (RepositoryException e){
            assertThat(true).isFalse();
        }

    }

    @Test
    public void testDeleteCourseShouldDelete(){

        try{
            CourseModel courseModel = new CourseModel(0
                    ,2
                    ,4
                    ,5
                    ,new Date()
                    , new Date()
                    , 10
                    , 12
                    , "userCtest1"
                    , "Title1"
                    ,"no desc");

            long dim = courseService.size();

            courseService.addCourse(courseModel, userService.getCoach("userCtest1"));
            assertThat(courseService.size()).isEqualTo(dim+1);

            courseService.deleteCourse(courseService.getLastId());
            assertThat(courseService.size()).isEqualTo(dim);

        }catch (RepositoryException e){
            assertThat(true).isFalse();
        }

    }

    @Test
    public void testDeleteShouldThrowException(){

        try{

            courseService.deleteCourse(-1);

        }catch (RepositoryException e){
            assertThat(true).isTrue();
        }
    }

    @Test
    public void testAttendUserToCourseShouldAttend(){

        try {

            Client client = new Client("testCclient1", "password", "email@yahoo.com", "Elisei",
                    new Date(1000));
            userService.addUser(client);

            CourseModel courseModel = new CourseModel(0
                    ,2
                    ,4
                    ,5
                    ,new Date()
                    , new Date()
                    , 10
                    , 0
                    , "userCtest1"
                    , "Title1"
                    ,"no desc");

            courseService.addCourse(courseModel, userService.getCoach("userCtest1"));
            courseService.attendUserToCourse(courseService.getLastId(), client);

            List<Client> clients = courseService.getAllClientsForCourse(courseService.getLastId());

            assertThat(clients.size()).isEqualTo(1);
            assertThat(clients.get(0).getUsername()).isEqualTo("testCclient1");

        }catch (RepositoryException e){
            assertThat(true).isFalse();
        }

    }

    @Test
    public void testAttenduserToCourseShouldThrowException(){

        try {
            Client client = new Client("testCclient1", "password", "email@yahoo.com", "Elisei",
                    new Date(1000));
            userService.addUser(client);

            CourseModel courseModel = new CourseModel(0
                    , 2
                    , 4
                    , 5
                    , new Date()
                    , new Date()
                    , 0
                    , 0
                    , "userCtest1"
                    , "Title1"
                    , "no desc");

            courseService.addCourse(courseModel, userService.getCoach("userCtest1"));
            courseService.attendUserToCourse(courseService.getLastId(), client);
            assertThat(true).isFalse();
        }catch (RepositoryException e){
            assertThat(true).isTrue();
        }

    }

    @Test
    public void testAddFeedbackShouldAdd(){
        try {
            Client client = new Client("testCclient2", "password", "email@yahoo.com", "Elisei",
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
            assertThat(clients.get(0).getUsername()).isEqualTo("testCclient2");

            FeedbackModel feedbackModel = new FeedbackModel(0, 3, "summary", "details", new Date(),"testCclient2" );
            List<String> errors = courseService.addFeedback(courseService.getLastId(), feedbackModel);
            System.out.println(errors);

            List<CourseFeedback> feedbacks = feedBackService.getAllCourseFeedBacks(courseService.getLastId());
            assertThat(feedbacks.size()).isEqualTo(1);


        }catch (RepositoryException e){
            assertThat(true).isFalse();
        }

    }

    @Test
    public void testAddFeedbackShouldThrowException(){
        try {
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

            FeedbackModel feedbackModel1 = new FeedbackModel(0, 4, "summary1", "detai1ls", new Date(),"testCclient3" );
            errors = courseService.addFeedback(courseService.getLastId(), feedbackModel1);
            feedbacks = feedBackService.getAllCourseFeedBacks(courseService.getLastId());
            /// PROBLEM HERE, TO FIX
            System.out.println("===============================================================");
            System.out.println(feedbacks.size());
            System.out.println("===============================================================");

        }catch (RepositoryException e){
            System.out.println(e.getMessage());
            assert(false);
        }
    }

}
