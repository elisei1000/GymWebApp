package com.gymwebapp.controller;

import com.gymwebapp.domain.*;
import com.gymwebapp.domain.Validator.CourseValidator;
import com.gymwebapp.domain.Validator.FeedbackValidator;
import com.gymwebapp.domain.Validator.Validator;
import com.gymwebapp.model.CourseModel;
import com.gymwebapp.model.FeedbackModel;
import com.gymwebapp.service.CourseService;
import com.gymwebapp.service.FeedBackService;
import com.gymwebapp.service.UserService;
import com.gymwebapp.util.Response;
import com.gymwebapp.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;
import sun.misc.Request;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by david on 30.11.2017.
 */
@CrossOrigin
@org.springframework.web.bind.annotation.RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private FeedBackService feedBackService;

    @GetMapping(value = "/course")
    public Response getAllCourses() {
        return new Response(Status.STATUS_OK, new ArrayList<String>(), Pair.of("courses", courseService.getAll()));
    }

    @GetMapping(value = "/course/{id}")
    public Response getCourse(@PathVariable Integer id) {
        CourseModel courseModel = courseService.getCourse(id);

        List<String> errors = new ArrayList<>();

        if (courseModel.getId() != null) {
            return new Response(Status.STATUS_OK, errors, Pair.of("course", courseModel));
        } else {
            errors.add("Cursul dat nu exista!");
            return new Response(Status.STATUS_FAILED, errors);
        }
    }

    @GetMapping(value = "/course/{id}/feedback")
    public Response getAllFedbacks(@PathVariable Integer id) {
        CourseModel courseModel = courseService.getCourse(id);

        List<String> errors = new ArrayList<>();

        if (courseModel.getId() == null) {
            errors.add("Cursul dat nu exista!");
            return new Response(Status.STATUS_FAILED, errors);
        } else {
            List<CourseFeedback> feedbacks = feedBackService.getAllCourseFeedBacks(id);
            List<FeedbackModel> feedbackResponse = new ArrayList<>();

            for (CourseFeedback cf : feedbacks) {
                String author=null;
                if(cf.getAuthor()!=null)
                    author=cf.getAuthor().getUsername();
                feedbackResponse.add(new FeedbackModel(cf.getId(), cf.getStarsCount(), cf.getSummary(), cf.getDetails(), cf.getDate(), author));
            }

            return new Response(Status.STATUS_OK, errors, Pair.of("feedbacks", feedbackResponse));
        }
    }

    @PostMapping(value = "/course/{id}/feedback")
    public Response addFedback(@PathVariable Integer id,@RequestBody FeedbackModel feedbackModel,Principal principal) {

        Validator<FeedbackModel> validator=new FeedbackValidator();

        List<String> validatorErrors=validator.validate(feedbackModel);

        if(validatorErrors.size()!=0){
            return new Response(Status.STATUS_FAILED, validatorErrors);
        }

        feedbackModel.setDate(new Date());
        feedbackModel.setAuthor(principal.getName());

        List<String> errors = courseService.addFeedback(id,feedbackModel);

        if (errors.size() != 0) {
            return new Response(Status.STATUS_FAILED, errors);
        } else {
            return new Response(Status.STATUS_OK, errors);
        }
    }

    @PutMapping(value = "/course/{id}/feedback")
    public Response modifyFedback(@PathVariable Integer id,@RequestBody FeedbackModel feedbackModel,Principal principal) {
        Validator<FeedbackModel> validator=new FeedbackValidator();

        List<String> validatorErrors=validator.validate(feedbackModel);

        if(validatorErrors.size()!=0){
            return new Response(Status.STATUS_FAILED, validatorErrors);
        }

        feedbackModel.setDate(new Date());
        feedbackModel.setAuthor(principal.getName());

        List<String> errors = courseService.modifyFeedback(id,feedbackModel);

        if (errors.size() != 0) {
            return new Response(Status.STATUS_FAILED, errors);
        } else {
            return new Response(Status.STATUS_OK, errors);
        }
    }

    @DeleteMapping(value = "/course/{id}/feedback")
    public Response deleteFedback(@PathVariable Integer id) {

        List<String> errors= new ArrayList<>();

        if (errors.size() != 0) {
            return new Response(Status.STATUS_FAILED, errors);
        } else {
            return new Response(Status.STATUS_OK, errors);
        }
    }

    @PostMapping(value = "/course")
    public Response addCourse(@RequestBody CourseModel courseModel) {
        Validator<CourseModel> validator = new CourseValidator();
        List<String> errors = validator.validate(courseModel);

        Coach coach = userService.getCoach(courseModel.getTeacher());
        if (coach == null) {
            errors.add("Antrenorul nu exista!");
        }

        if (errors.size() != 0) {
            return new Response(Status.STATUS_FAILED, errors);
        } else {
            try {
                courseService.addCourse(courseModel, coach);
                return new Response(Status.STATUS_OK, errors);
            } catch (RepositoryException e) {
                errors.add("Cursul nu a putut fi adaugat!");
                return new Response(Status.STATUS_FAILED, errors);
            }
        }
    }

    @PutMapping(value = "course/{id}")
    public Response modifyCourse(@RequestBody CourseModel courseModel, @PathVariable Integer id) {
        Validator<CourseModel> validator = new CourseValidator();
        List<String> errors = validator.validate(courseModel);
        Coach coach = userService.getCoach(courseModel.getTeacher());
        if (coach == null) {
            errors.add("Antrenorul nu exista!");
        }


        if (errors.size() != 0) {
            return new Response(Status.STATUS_FAILED, errors);
        } else {
            try {
                courseModel.setId(id);
                courseService.modifyCourse(courseModel, coach);
                return new Response(Status.STATUS_OK, errors);
            } catch (RepositoryException e) {
                errors.add("Cursul dat nu exista!");
                return new Response(Status.STATUS_FAILED, errors);
            }
        }
    }

    @PutMapping(value = "course/{id}/attend")
    public Response attendParticipant(@PathVariable Integer id, Principal principal) {

        String username = principal.getName();
        Client client = userService.getClient(username);
        List<String> errors = new ArrayList<>();

        if (client == null) {
            errors.add("Clientul nu exista!");
            return new Response(Status.STATUS_FAILED,errors);
        } else {

            try {
                courseService.attendUserToCourse(id, client);
                return new Response(Status.STATUS_OK,errors);
            } catch (RepositoryException e) {
                errors.add("Nu a putut fi adaugat clientul!");
                return new Response(Status.STATUS_FAILED,errors);

            }
        }
    }

    @DeleteMapping(value = "course/{id}")
    public Response deleteCourse(@PathVariable Integer id) {
        List<String> errors = new ArrayList<>();
        try {
            courseService.deleteCourse(id);
            return new Response(Status.STATUS_OK, errors);
        } catch (RepositoryException e) {
            errors.add("Nu a putut fi sters cursul!");
            return new Response(Status.STATUS_FAILED, errors);
        }
    }

}
