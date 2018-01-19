package com.gymwebapp.controller;

import com.gymwebapp.domain.Coach;
import com.gymwebapp.domain.CoachFeedback;
import com.gymwebapp.domain.Validator.FeedbackValidator;
import com.gymwebapp.domain.Validator.Validator;
import com.gymwebapp.model.CoachModel;
import com.gymwebapp.model.FeedbackModel;
import com.gymwebapp.service.CoachService;
import com.gymwebapp.service.FeedBackService;
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
    private CoachService coachService;

    @Autowired
    private FeedBackService feedBackService;

    @PostMapping(value = "/coach")
    public Response add(@RequestBody CoachModel coachModel) {
        Coach coach = new Coach(coachModel.getUsername(), coachModel.getPassword(), coachModel.getEmail(),
                coachModel.getName(), coachModel.getBirthDay(), coachModel.getAbout());
        List<String> errors = coachService.addCoach(coach);

        if (errors.size() == 0) {
            coachModel.setPassword(null);
            return new Response(Status.STATUS_OK, errors, Pair.of("coach", coachModel));
        } else {
            return new Response(Status.STATUS_FAILED, errors);
        }
    }

    @PutMapping(value = "/coach/{username}")
    public Response update(@PathVariable String username, @RequestBody CoachModel coachModel) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Coach coach = new Coach(username, coachModel.getPassword(), coachModel.getEmail(), coachModel.getName(), coachModel.getBirthDay(), coachModel.getAbout());
        List<String> errors = coachService.updateCoach(coach);
        if (errors.size() == 0) {
            return new Response(Status.STATUS_OK, errors);
        } else {
            return new Response(Status.STATUS_FAILED, errors);
        }
    }

    @DeleteMapping(value = "/coach/{username}")
    public Response remove(@PathVariable String username) {
        Coach coach = new Coach(username, null);
        List<String> errors = coachService.removeCoach(coach);
        if (errors.size() == 0) {
            return new Response(Status.STATUS_OK, errors);
        } else {
            return new Response(Status.STATUS_FAILED, errors);
        }
    }

    @GetMapping(value = "/coach")
    public Response getAll() {
        List<Coach> coaches = coachService.getAllCoaches();
        List<CoachModel> coaches_response = new ArrayList<>();
        for (Coach c : coaches) {
            coaches_response.add(new CoachModel(c.getUsername(), "", c.getEmail(), c.getName(), c.getBirthDay(), c.getAbout()));
        }
        return new Response(Status.STATUS_OK, new ArrayList<>(), Pair.of("coaches", coaches_response));
    }

    @GetMapping(value = "/coach/{username}")
    public Response getCoach(@PathVariable String username) {
        Coach coach = coachService.getCoach(username);
        List<String> errors = new ArrayList<>();
        if (coach == null) {
            errors.add("Coach with given username does't exists!");
            return new Response(Status.STATUS_FAILED, errors);
        }

        CoachModel coachModel = new CoachModel(coach.getUsername(), "", coach.getEmail(), coach.getName(), coach.getBirthDay(), coach.getAbout());
        return new Response(Status.STATUS_OK, new ArrayList<>(), Pair.of("coach", coachModel));
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
            e.add("You're not logged!");
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
            e.add("You're not logged!");
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
            errors.add("You're not logged!");
            return new Response(Status.STATUS_NOT_LOGGED_IN, errors);
        }

        feedBackService.deleteCoachFeedback(username, client);

        if (errors.size() != 0) {
            return new Response(Status.STATUS_FAILED, errors);
        } else {
            return new Response(Status.STATUS_OK, errors);
        }
    }

    @PostMapping(value = "/coach/{username}/image")
    public Response addImage(@PathVariable String username, @RequestParam("file") MultipartFile file) {
        Coach coach = this.coachService.getCoach(username);

        List<String> errors = new ArrayList<>();

        if(username.contains("/")){
            errors.add("Cannot have special characters!");
            return new Response(Status.STATUS_FAILED, errors);
        }

        if (coach == null) {
            errors.add("Given coach doesn't exists!");
            return new Response(Status.STATUS_FAILED, errors);
        }

        String pathName =
                String.format("./src/main/resources/static/uploaded/coach%s.jpg", username);
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

    private void deleteImage(String username) {
        String pathName =
                String.format("./src/main/resources/static/uploaded/coach%s.jpg", username);
        File file = new File(pathName);
        file.delete();
    }

    @RequestMapping(value = "/coach/{username}/image", method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImage(@PathVariable String username) {
        String defaultPath = "./src/main/resources/static/uploaded/coachDefault.jpg";
        String pathName =
                String.format("./src/main/resources/static/uploaded/coach%s.jpg", username);
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
