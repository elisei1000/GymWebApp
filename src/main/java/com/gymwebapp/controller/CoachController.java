package com.gymwebapp.controller;

import com.gymwebapp.domain.Coach;
import com.gymwebapp.domain.CoachFeedback;
import com.gymwebapp.domain.Validator.FeedbackValidator;
import com.gymwebapp.domain.Validator.Validator;
import com.gymwebapp.model.FeedbackModel;
import com.gymwebapp.model.UserModel;
import com.gymwebapp.service.FeedBackService;
import com.gymwebapp.service.UserService;
import com.gymwebapp.util.Response;
import com.gymwebapp.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vasi on 26.11.2017.
 */
@CrossOrigin
@org.springframework.web.bind.annotation.RestController
public class CoachController {

    @Autowired
    private UserService userService;

    @Autowired
    private FeedBackService feedBackService;

    @PostMapping(value = "/coach")
    public Response add(@RequestBody UserModel userModel) {
        Coach coach = new Coach(userModel.getUsername(), userModel.getPassword(), userModel.getEmail(),
                userModel.getName(), userModel.getBirthDay());
        List<String> errors = userService.addUser(coach);

        if (errors.size() == 0) {
            return new Response(Status.STATUS_OK, errors);
        } else {
            return new Response(Status.STATUS_FAILED, errors);
        }
    }

    @PutMapping(value = "/coach/{username}")
    public Response update(@PathVariable String username, @RequestBody UserModel userModel) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Coach coach = new Coach(username, passwordEncoder.encode(userModel.getPassword()), userModel.getEmail(), userModel.getName(), userModel.getBirthDay());
        List<String> errors = userService.updateUser(coach);
        if (errors.size() == 0) {
            return new Response(Status.STATUS_OK, errors);
        } else {
            return new Response(Status.STATUS_FAILED, errors);
        }
    }

    @DeleteMapping(value = "/coach/{username}")
    public Response remove(@PathVariable String username) {
        Coach coach = new Coach(username, null);
        List<String> errors = userService.removeCoach(coach);
        if (errors.size() == 0) {
            return new Response(Status.STATUS_OK, errors);
        } else {
            return new Response(Status.STATUS_FAILED, errors);
        }
    }

    @GetMapping(value = "/coach")
    public Response getAll() {
        List<Coach> coaches = userService.getAllCoaches();
        List<UserModel> coaches_response = new ArrayList<>();
        for (Coach c : coaches) {
            coaches_response.add(new UserModel(c.getUsername(), "", c.getEmail(), c.getName(), c.getBirthDay()));
        }
        return new Response(Status.STATUS_OK, new ArrayList<>(), Pair.of("coaches", coaches_response));
    }

    @GetMapping(value = "/coach/{username}/feedback")
    public Response getAllFeedBacks(@PathVariable String username) {
        List<CoachFeedback> feedbacks = feedBackService.getAllCoachFeedBacks(username);
        List<FeedbackModel> feedbackModels = new ArrayList<>();
        for (CoachFeedback cf : feedbacks)
            feedbackModels.add(new FeedbackModel(cf.getId(), cf.getStarsCount(), cf.getSummary(), cf.getDetails(), cf.getDate(), cf.getAuthor().getUsername()));
        return new Response(Status.STATUS_OK, new ArrayList<>(), Pair.of("feedbacks", feedbackModels));
    }

    @PostMapping(value = "/coach/{username}/feedback")
    public Response addFeedback(@PathVariable String username, @RequestBody FeedbackModel feedbackModel, Principal principal) {
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

        List<String> errors = feedBackService.addCoachFeedback(username, feedbackModel);

        if (errors.size() != 0) {
            return new Response(Status.STATUS_FAILED, errors);
        } else {
            return new Response(Status.STATUS_OK, errors);
        }
    }

    @PutMapping(value = "/coach/{username}/feedback")
    public Response modifyFeedback(@PathVariable String username, @RequestBody FeedbackModel feedbackModel, Principal principal) {
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

        List<String> errors = feedBackService.modifyCoachFeedback(username, feedbackModel);

        if (errors.size() != 0) {
            return new Response(Status.STATUS_FAILED, errors);
        } else {
            return new Response(Status.STATUS_OK, errors);
        }
    }

    @DeleteMapping(value = "/coach/{username}/feedback")
    public Response deleteFedback(@PathVariable String username, Principal principal) {
        List<String> errors = new ArrayList<>();

        String client = null;
        if (principal != null) {
            client = principal.getName();
        } else {
            errors.add("Nu sunteti logat!");
            return new Response(Status.STATUS_NOT_LOGGED_IN, errors);
        }

        feedBackService.deleteCoachFeedback(username, client);

        if (errors.size() != 0) {
            return new Response(Status.STATUS_FAILED, errors);
        } else {
            return new Response(Status.STATUS_OK, errors);
        }
    }

    @PostMapping(value = "/coach/{id}/image")
    public Response addImage(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {

        List<String> errors = new ArrayList<>();

        String pathName =
                String.format("./src/main/resources/static/uploaded/coach%s.jpg", id);
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
                String.format("./src/main/resources/static/uploaded/coach%s.jpg", id);
        File file = new File(pathName);
        file.delete();
    }

    @RequestMapping(value = "/coach/{id}/image", method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImage(@PathVariable Integer id) {
        String defaultPath = "./src/main/resources/static/uploaded/coachDefault.jpg";
        String pathName =
                String.format("./src/main/resources/static/uploaded/coach%d.jpg", id);
        File imgFile = new File(pathName);

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
