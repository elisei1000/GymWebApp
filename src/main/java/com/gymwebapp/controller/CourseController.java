package com.gymwebapp.controller;

import com.gymwebapp.domain.*;
import com.gymwebapp.domain.Validator.CourseValidator;
import com.gymwebapp.domain.Validator.FeedbackValidator;
import com.gymwebapp.domain.Validator.Validator;
import com.gymwebapp.model.CourseModel;
import com.gymwebapp.model.FeedbackModel;
import com.gymwebapp.model.ScheduleModel;
import com.gymwebapp.service.CourseService;
import com.gymwebapp.service.FeedBackService;
import com.gymwebapp.service.UserService;
import com.gymwebapp.util.Response;
import com.gymwebapp.util.Status;
import com.sun.prism.Image;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.eclipse.jetty.util.MultiPartInputStreamParser;
import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
                String author = null;
                if (cf.getAuthor() != null)
                    author = cf.getAuthor().getUsername();
                feedbackResponse.add(new FeedbackModel(cf.getId(), cf.getStarsCount(), cf.getSummary(), cf.getDetails(), cf.getDate(), author));
            }

            return new Response(Status.STATUS_OK, errors, Pair.of("feedbacks", feedbackResponse));
        }
    }

    @PostMapping(value = "/course/{id}/feedback")
    public Response addFedback(@PathVariable Integer id, @RequestBody FeedbackModel feedbackModel, Principal principal) {

        Validator<FeedbackModel> validator = new FeedbackValidator();

        List<String> validatorErrors = validator.validate(feedbackModel);

        if (validatorErrors.size() != 0) {
            return new Response(Status.STATUS_FAILED, validatorErrors);
        }

        feedbackModel.setDate(new Date());
        if (principal != null) {
            feedbackModel.setAuthor(principal.getName());
        } else {
            List<String> e = new ArrayList<>();
            e.add("Nu sunteti logat!");
            return new Response(Status.STATUS_NOT_LOGGED_IN, e);
        }

        List<String> errors = courseService.addFeedback(id, feedbackModel);

        if (errors.size() != 0) {
            return new Response(Status.STATUS_FAILED, errors);
        } else {
            return new Response(Status.STATUS_OK, errors);
        }
    }

    @PutMapping(value = "/course/{id}/feedback")
    public Response modifyFedback(@PathVariable Integer id, @RequestBody FeedbackModel feedbackModel, Principal principal) {
        Validator<FeedbackModel> validator = new FeedbackValidator();

        List<String> validatorErrors = validator.validate(feedbackModel);

        if (validatorErrors.size() != 0) {
            return new Response(Status.STATUS_FAILED, validatorErrors);
        }

        feedbackModel.setDate(new Date());
        if (principal != null) {
            feedbackModel.setAuthor(principal.getName());
        } else {
            List<String> e = new ArrayList<>();
            e.add("Nu sunteti logat!");
            return new Response(Status.STATUS_NOT_LOGGED_IN, e);
        }

        List<String> errors = courseService.modifyFeedback(id, feedbackModel);

        if (errors.size() != 0) {
            return new Response(Status.STATUS_FAILED, errors);
        } else {
            return new Response(Status.STATUS_OK, errors);
        }
    }

    @DeleteMapping(value = "/course/{id}/feedback")
    public Response deleteFedback(@PathVariable Integer id, Principal principal) {

        List<String> errors = new ArrayList<>();

        String username = null;
        if (principal != null) {
            username = principal.getName();
        } else {
            errors.add("Nu sunteti logat!");
            return new Response(Status.STATUS_NOT_LOGGED_IN, errors);
        }

        courseService.deleteFeedback(id, username);

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

    @GetMapping(value = "course/{id}/attended")
    public Response checkattendParticipant(@PathVariable Integer id, Principal principal) {
        List<String> errors = new ArrayList<>();

        String username = null;
        if (principal != null) {
            username = principal.getName();
        } else {
            errors.add("Nu sunteti logat!");
            return new Response(Status.STATUS_NOT_LOGGED_IN, errors);
        }
        Client client = userService.getClient(username);

        if (client == null) {
            errors.add("Clientul nu exista!");
            return new Response(Status.STATUS_FAILED, errors);
        } else {
            try {
                boolean check = courseService.checkAttendUserToCourse(id, client);
                return new Response(Status.STATUS_OK, new ArrayList<String>(), Pair.of("attended", check));
            } catch (RepositoryException e) {
                errors.add(e.getMessage());
                return new Response(Status.STATUS_FAILED, errors);

            }
        }
    }

    @PutMapping(value = "course/{id}/attend")
    public Response attendParticipant(@PathVariable Integer id, Principal principal) {
        List<String> errors = new ArrayList<>();

        String username = null;
        if (principal != null) {
            username = principal.getName();
        } else {
            errors.add("Nu sunteti logat!");
            return new Response(Status.STATUS_NOT_LOGGED_IN, errors);
        }
        Client client = userService.getClient(username);

        if (client == null) {
            errors.add("Clientul nu exista!");
            return new Response(Status.STATUS_FAILED, errors);
        } else {
            try {
                courseService.attendUserToCourse(id, client);
                return new Response(Status.STATUS_OK, errors);
            } catch (RepositoryException e) {
                errors.add(e.getMessage());
                return new Response(Status.STATUS_FAILED, errors);

            }
        }
    }

    @DeleteMapping(value = "course/{id}")
    public Response deleteCourse(@PathVariable Integer id) {
        List<String> errors = new ArrayList<>();
        try {
            courseService.deleteCourse(id);
            deleteImage(id);
            return new Response(Status.STATUS_OK, errors);
        } catch (RepositoryException e) {
            errors.add("Nu a putut fi sters cursul!");
            return new Response(Status.STATUS_FAILED, errors);
        }
    }

    @GetMapping(value = "course/program")
    public Response schedule(@RequestParam String startDate, @RequestParam String endDate) {
        List<String> errors = new ArrayList<>();

        if (startDate.equals(null) || startDate.isEmpty() || endDate.equals(null) || endDate.isEmpty())
            errors.add("Datele sunt invalide !");

        if (errors.size() != 0)
            return new Response(Status.STATUS_FAILED, errors);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date sDate = null, eDate = null;
        try {
            sDate = format.parse(startDate);
            eDate = format.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ScheduleModel scheduleModel = new ScheduleModel(sDate, eDate);
        List<List<Course>> schedule_courses = courseService.getSchedule(scheduleModel);
        List<List<CourseModel>> schedule_response = new ArrayList<>();
        for (int i = 0; i < schedule_courses.size(); i++)
            schedule_response.add(new ArrayList<>());

        for (int i = 0; i < schedule_courses.size(); i++)
            for (Course course : schedule_courses.get(i))
                schedule_response.get(i).add(new CourseModel(course.getId(), course.getDifficultyLevel(), course.getStartHour(), course.getEndHour(), course.getStartDate(), course.getEndDate(), course.getMaxPlaces(), course.getClients().size(), course.getTeacher().getName(), course.getTitle(), course.getDescription()));
        return new Response(Status.STATUS_OK, new ArrayList<>(), Pair.of("program", schedule_response));
    }

    @PostMapping(value = "/course/{id}/image")
    public Response addImage(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        CourseModel courseModel = courseService.getCourse(id);

        List<String> errors = new ArrayList<>();

        if (courseModel.getId() == null) {
            errors.add("Cursul dat nu exista!");
            return new Response(Status.STATUS_FAILED, errors);
        }

        String pathName =
                String.format("./src/main/resources/static/uploaded/course%s.jpg", id);
        try {
            try {
                BufferedImage image = ImageIO.read(file.getInputStream());
            } catch (Exception e) {
                System.out.println("It's not an image!");
            }
            byte[] bytes = file.getBytes();
            Path path = Paths.get(pathName);
            File localFile = path.toFile();
            localFile.getParentFile().mkdirs();
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Response(Status.STATUS_OK, errors);
    }

    private void deleteImage(Integer id) {
        String pathName =
                String.format("./src/main/resources/static/uploaded/course%s.jpg", id);
        File file = new File(pathName);
        file.delete();
    }

    @RequestMapping(value = "/course/{id}/image", method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImage(@PathVariable Integer id) {
        String defaultPath = "./src/main/resources/static/uploaded/courseDefault.jpg";
        String pathName =
                String.format("./src/main/resources/static/uploaded/course%d.jpg", id);
        File imgFile = new File(pathName);

        CourseModel courseModel = courseService.getCourse(id);

        if (courseModel.getId() == null) {
            return null;
        }

        if (imgFile.exists()) {
            try {
                return Files.readAllBytes(Paths.get(pathName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            return Files.readAllBytes(Paths.get(defaultPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
