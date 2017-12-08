package com.gymwebapp.controller;

import com.gymwebapp.domain.*;
import com.gymwebapp.model.UserModel;
import com.gymwebapp.service.SubscriptionService;
import com.gymwebapp.service.UserService;
import com.gymwebapp.util.Response;
import com.gymwebapp.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.util.Pair;
import org.springframework.format.annotation.DateTimeFormat;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by david on 18.11.2017.
 */
@CrossOrigin
@org.springframework.web.bind.annotation.RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SubscriptionService subscriptionService;

//    @PostMapping(value = "/login")
//    public Response login(@RequestBody UserModel userModel) {
//        User user = new User(userModel.getUsername(), userModel.getPassword(),
//                userModel.getEmail(), userModel.getName(), userModel.getBirthDay());
//        List<String> errors = userService.checkIfExistUser(user);
//
//        if(errors.size()==0){
//            return new Response(Status.STATUS_OK, errors);
//        }else{
//            return new Response(Status.STATUS_FAILED, errors);
//        }
//    }


    @PostMapping(value = "/user-register")
    public Response add(@RequestBody UserModel userModel) {
        Client client = new Client(userModel.getUsername(), userModel.getPassword(), userModel.getEmail(),
                userModel.getName(), userModel.getBirthDay());
        List<String> errors = userService.addUser(client);

        if(errors.size()==0){
            return new Response(Status.STATUS_OK, errors);
        }else{
            return new Response(Status.STATUS_FAILED, errors);
        }
    }

    @RequestMapping(value = "/cuser", method = RequestMethod.GET)
    public Response currentUser(Principal principal) {

        List<String> error = new ArrayList<>();
        if (principal == null) {
            error.add("Nu sunteti logat!");
            return new Response(Status.STATUS_NOT_LOGGED_IN, error);
        }
        String username = principal.getName();
        User cuser = userService.findUser(username);
        User sendUser = new User();
        sendUser.setUsername(cuser.getUsername());
        sendUser.setBirthDay(cuser.getBirthDay());
        sendUser.setName(cuser.getName());
        sendUser.setEmail(cuser.getEmail());
        return new Response(Status.STATUS_OK, error, Pair.of("user", sendUser));
    }

    @RequestMapping(value = "/cuser/hasPermission", method = RequestMethod.GET)
    public Response hasPermission(@RequestParam("page") String page, Principal principal) {
        User cuser = new User();
        Boolean isLogged = true;
        if (principal != null) {
            String username = principal.getName();
            cuser = userService.findUser(username);
        } else {
            isLogged = false;
        }

        List<String> error = new ArrayList<>();
        List<String> pages = new ArrayList<>();
        pages.add("MANAGE_COURSES");
        pages.add("CLIENT_COACHES");
        pages.add("FEEDBACKS");
        pages.add("COURSES");
        pages.add("COACHES");
        pages.add("PERSONAL_INFO");
        pages.add("HOME");
        pages.add("LOGIN");
        pages.add("REGISTER");
        pages.add("CONTACT");
        pages.add("ABOUT");
        pages.add("MANAGE_COACHES");
        pages.add("CLIENT_COURSES");

        if (!isLogged && (page.equals("FEEDBACKS")
                || page.equals("CLIENT_COACHES")   || page.equals("CLIENT_COURSES") || page.equals("PERSONAL_INFO")
                || page.equals("MANAGE_COACHES")   || page.equals("MANAGE_COURSES")
                )) {
            error.add("Nu sunteti logat!");
            return new Response(Status.STATUS_NOT_LOGGED_IN, error);
        }

        if (isLogged && cuser.getClass() != Client.class && page.equals("PERSONAL_INFO")) {
            error.add("Nu aveti drepturi pentru a accesa aceasta pagina!");
            return new Response(Status.STATUS_PERMISSION_DENIED, error);
        }

        if (page.equals("COACHES") && ((cuser.getClass() != Coach.class && isLogged) || !isLogged)) {
            error.add("Nu aveti drepturi pentru a accesa aceasta pagina!");
            return new Response(Status.STATUS_PERMISSION_DENIED, error);
        }

        if (page.equals("COURSES") && ((cuser.getClass() != Coach.class && isLogged) || !isLogged)) {
            error.add("Nu aveti drepturi pentru a accesa aceasta pagina!");
            return new Response(Status.STATUS_PERMISSION_DENIED, error);
        }

        if (page.equals("FEEDBACKS") && cuser.getClass() != Coach.class && isLogged) {
            error.add("Nu aveti drepturi pentru a accesa aceasta pagina!");
            return new Response(Status.STATUS_PERMISSION_DENIED, error);
        }

        if (page.equals("CLIENT_COACHES") && cuser.getClass() != Client.class && isLogged) {
            error.add("Nu aveti drepturi pentru a accesa aceasta pagina!");
            return new Response(Status.STATUS_PERMISSION_DENIED, error);
        }

        if (page.equals("CLIENT_COURSES") && cuser.getClass() != Client.class && isLogged) {
            error.add("Nu aveti drepturi pentru a accesa aceasta pagina!");
            return new Response(Status.STATUS_PERMISSION_DENIED, error);
        }

        if (page.equals("MANAGE_COACHES") && cuser.getClass() != Administrator.class && isLogged) {
            error.add("Nu aveti drepturi pentru a accesa aceasta pagina!");
            return new Response(Status.STATUS_PERMISSION_DENIED, error);
        }

        if (page.equals("MANAGE_COURSES") && cuser.getClass() != Administrator.class && isLogged) {
            error.add("Nu aveti drepturi pentru a accesa aceasta pagina!");
            return new Response(Status.STATUS_PERMISSION_DENIED, error);
        }

        if (!pages.contains(page)) {
            error.add("Ati accesat o pagina inexistenta!");
            return new Response(Status.STATUS_FAILED, error);
        }

        error.add("");
        return new Response(Status.STATUS_OK, error);
    }

    @RequestMapping(value = "/cuser/subscription", method = RequestMethod.GET)
    public Response currentUserSubscription(Principal principal) {
        List<String> error = new ArrayList<>();
        if (principal == null) {
            error.add("Nu sunteti logat!");
            return new Response(Status.STATUS_NOT_LOGGED_IN, error);
        }
        String username = principal.getName();
        User cuser = userService.findUser(username);
        if (cuser.getClass() != Client.class) {
            error.add("Nu sunteti client!");
            return new Response(Status.STATUS_FAILED, error);
        }
        Client client = (Client) userService.findUser(username);
        Subscription sendSubscription = new Subscription();
        if (client.getSubscription() == null) {
            error.add("Nu aveti abonament inregistrat!");
            return new Response(Status.STATUS_OK, error);
        }
        sendSubscription.setStartDate(client.getSubscription().getStartDate());
        sendSubscription.setEndDate(client.getSubscription().getEndDate());
        return new Response(Status.STATUS_OK, error, Pair.of("subscription", sendSubscription));
    }

    @RequestMapping(value = "/cuser/subscription", method = RequestMethod.POST)
    public Response addSubscriptionCurrentUser(@RequestParam(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate, Principal principal) {
        Date currentDate = new Date();
        List<String> errors = new ArrayList<>();
        if (principal == null) {
            errors.add("Nu sunteti logat!");
            return new Response(Status.STATUS_NOT_LOGGED_IN, errors);
        }
        if (endDate.getTime() <= currentDate.getTime()) {
            errors.add("End date trebuie sa fie ulterioara datei currente!");
            return new Response(Status.STATUS_FAILED, errors);
        }
        Subscription subscription = new Subscription();
        subscription.setStartDate(currentDate);
        subscription.setEndDate(endDate);
        subscriptionService.addSubscription(subscription);
        String username = principal.getName();
        User cuser = userService.findUser(username);
        if (cuser.getClass() != Client.class) {
            errors.add("Nu sunteti client!");
            return new Response(Status.STATUS_FAILED, errors);
        }
        Client client = (Client) userService.findUser(username);
        Subscription currenUserSubscription = client.getSubscription();
        if (currenUserSubscription != null) {
            errors.add("Aveti deja abonament!");
            return new Response(Status.STATUS_FAILED, errors);
        }
        client.setSubscription(subscription);
        return new Response(Status.STATUS_OK, errors);
    }

    @RequestMapping(value = "/cuser/subscription", method = RequestMethod.PUT)
    public Response updateSubscriptionCurrentUser(@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate, Principal principal) {
        Date currentDate = new Date();
        List<String> errors = new ArrayList<>();
        if (principal == null) {
            errors.add("Nu sunteti logat!");
            return new Response(Status.STATUS_NOT_LOGGED_IN, errors);
        }
        if (endDate.getTime() <= currentDate.getTime()) {
            errors.add("End date trebuie sa fie ulterioara datei currente!");
            return new Response(Status.STATUS_FAILED, errors);
        }

        String username = principal.getName();
        User cuser = userService.findUser(username);
        if (cuser.getClass() != Client.class) {
            errors.add("Nu sunteti client!");
            return new Response(Status.STATUS_FAILED, errors);
        }
        Client client = (Client) userService.findUser(username);
        Subscription currenUserSubscription = client.getSubscription();
        if (currenUserSubscription == null) {
            errors.add("Nu aveti abonament!");
            return new Response(Status.STATUS_FAILED, errors);
        }
        currenUserSubscription.setEndDate(endDate);
        currenUserSubscription.setStartDate(new Date());
        subscriptionService.updateSubscription(currenUserSubscription);
        client.setSubscription(currenUserSubscription);
        return new Response(Status.STATUS_OK, errors);
    }

}