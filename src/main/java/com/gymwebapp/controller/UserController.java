package com.gymwebapp.controller;

import com.gymwebapp.domain.*;
import com.gymwebapp.model.SubscriptionModel;
import com.gymwebapp.model.UserModel;
import com.gymwebapp.service.SubscriptionService;
import com.gymwebapp.service.UserService;
import com.gymwebapp.util.Response;
import com.gymwebapp.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.util.Pair;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by david on 18.11.2017.
 */
@CrossOrigin
@org.springframework.web.bind.annotation.RestController
public class UserController {

    @Autowired
    private UserService userService;

    List<String> pages;
    List<String> pagesAllUsers;
    List<String> pagesNotLoggedUser;
    List<String> pagesClient;
    List<String> pagesAdministrator;
    List<String> pagesCoach;

    @Autowired
    private SubscriptionService subscriptionService;

    public UserController() {
        pages = new ArrayList<>();
        pagesAllUsers = new ArrayList<>();
        pagesNotLoggedUser = new ArrayList<>();
        pagesClient = new ArrayList<>();
        pagesAdministrator = new ArrayList<>();
        pagesCoach = new ArrayList<>();

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

        pagesAllUsers.add("HOME");
        pagesAllUsers.add("LOGIN");
        pagesAllUsers.add("REGISTER");
        pagesAllUsers.add("CONTACT");
        pagesAllUsers.add("ABOUT");

        pagesNotLoggedUser.add("COURSES");
        pagesNotLoggedUser.add("COACHES");

        pagesClient.add("PERSONAL_INFO");
        pagesClient.add("CLIENT_COACHES");
        pagesClient.add("CLIENT_COURSES");

        pagesAdministrator.add("MANAGE_COACHES");
        pagesAdministrator.add("MANAGE_COURSES");

        pagesCoach.add("COACHES");
        pagesCoach.add("COURSES");
        pagesCoach.add("FEEDBACKS");
    }

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

        Status status = errors.size() == 0 ? Status.STATUS_OK : Status.STATUS_FAILED;
        return new Response(status, errors);
    }

    @RequestMapping(value = "/cuser", method = GET)
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

    @RequestMapping(value = "/cuser/hasPermission", method = GET)
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

        if (!pages.contains(page)) {
            error.add("Ati accesat o pagina inexistenta!");
            return new Response(Status.STATUS_FAILED, error);
        }

        if (!isLogged && !pagesNotLoggedUser.contains(page) && !pagesAllUsers.contains(page)) {
            return new Response(Status.STATUS_NOT_LOGGED_IN, error);
        }

        if (isLogged && cuser.getClass() == Client.class && !pagesClient.contains(page) && !pagesAllUsers.contains(page)) {
            return new Response(Status.STATUS_PERMISSION_DENIED, error);
        }

        if (isLogged && cuser.getClass() == Administrator.class && !pagesAdministrator.contains(page) && !pagesAllUsers.contains(page)) {
            return new Response(Status.STATUS_PERMISSION_DENIED, error);
        }

        if (isLogged && cuser.getClass() == Coach.class && !pagesCoach.contains(page) && !pagesAllUsers.contains(page)) {
            return new Response(Status.STATUS_PERMISSION_DENIED, error);
        }

        error.add("");
        return new Response(Status.STATUS_OK, error);
    }

    @RequestMapping(value = "/cuser/subscription", method = GET)
    public Response currentUserSubscription(Principal principal) {
        List<String> error = new ArrayList<>();
        if (principal == null) {
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
            return new Response(Status.STATUS_FAILED, error);
        }
        sendSubscription.setStartDate(client.getSubscription().getStartDate());
        sendSubscription.setEndDate(client.getSubscription().getEndDate());
        return new Response(Status.STATUS_OK, error, Pair.of("subscription", sendSubscription));
    }

    @RequestMapping(value = "/cuser/subscription", method = RequestMethod.POST)
    public Response addSubscriptionCurrentUser(@RequestBody SubscriptionModel subscriptionModel, Principal principal) {
        Date currentDate = new Date();
        List<String> errors = new ArrayList<>();
        if (principal == null) {
            return new Response(Status.STATUS_NOT_LOGGED_IN, errors);
        }
        if (subscriptionModel.getEndDate().getTime() <= currentDate.getTime()) {
            errors.add("End date trebuie sa fie ulterioara datei currente!");
            return new Response(Status.STATUS_FAILED, errors);
        }
        Subscription subscription = new Subscription();
        subscription.setStartDate(currentDate);
        subscription.setEndDate(subscriptionModel.getEndDate());
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
        subscriptionService.addSubscription(subscription);
        client.setSubscription(subscription);
        return new Response(Status.STATUS_OK, errors);
    }

    @RequestMapping(value = "/cuser/subscription", method = RequestMethod.PUT)
    public Response updateSubscriptionCurrentUser(@RequestBody SubscriptionModel subscriptionModel, Principal principal) {
        Date currentDate = new Date();
        List<String> errors = new ArrayList<>();
        if (principal == null) {
            return new Response(Status.STATUS_NOT_LOGGED_IN, errors);
        }
        if (subscriptionModel.getEndDate().getTime() <= currentDate.getTime()) {
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
        currenUserSubscription.setEndDate(subscriptionModel.getEndDate());
        currenUserSubscription.setStartDate(new Date());
        subscriptionService.updateSubscription(currenUserSubscription);
        client.setSubscription(currenUserSubscription);
        return new Response(Status.STATUS_OK, errors);
    }

    @RequestMapping(value = "/cuser/permissions", method = GET)
    public Response getAllPermissions(Principal principal) {
        List<String> errors = new ArrayList<>();
        List<String> lstAllPages = new ArrayList<>();
        lstAllPages.addAll(this.pagesAllUsers);
        if (principal == null) {
            lstAllPages.addAll(this.pagesNotLoggedUser);
            return new Response(Status.STATUS_NOT_LOGGED_IN, errors, Pair.of("pages", lstAllPages));
        }
        String username = principal.getName();
        User cuser = userService.findUser(username);
        if (cuser.getClass() == Client.class) {
            lstAllPages.addAll(this.pagesClient);
        }
        if (cuser.getClass() == Administrator.class) {
            lstAllPages.addAll(this.pagesAdministrator);
        }
        if (cuser.getClass() == Coach.class) {
            lstAllPages.addAll(this.pagesCoach);
        }
        return new Response(Status.STATUS_OK, errors, Pair.of("pages", lstAllPages));
    }

    @GetMapping(value = "/user/{username}")
    public Response getUserByUsername(@PathVariable String username) {
        List<String> error = new ArrayList<>();
        User user = userService.findUser(username);
        if (user == null) {
            error.add("Nu exista user cu acest username!");
            return new Response(Status.STATUS_FAILED, error);
        }
        User sendUser = new User();
        sendUser.setUsername(user.getUsername());
        sendUser.setName(user.getUsername());
        sendUser.setEmail(user.getEmail());
        return new Response(Status.STATUS_OK, error, Pair.of("user", sendUser));
    }

}