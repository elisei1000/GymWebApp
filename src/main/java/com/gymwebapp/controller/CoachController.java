package com.gymwebapp.controller;

import com.gymwebapp.domain.Coach;
import com.gymwebapp.model.UserModel;
import com.gymwebapp.service.UserService;
import com.gymwebapp.util.Response;
import com.gymwebapp.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vasi on 26.11.2017.
 */
@CrossOrigin
@org.springframework.web.bind.annotation.RestController
public class CoachController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/coach")
    public Response add(@RequestBody UserModel userModel) {
        //to do : check if has permission
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
        Coach coach = new Coach(username, userModel.getPassword(), userModel.getEmail(), userModel.getName(), userModel.getBirthDay());
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
        List<String> errors = userService.removeUser(coach);
        if (errors.size() == 0) {
            return new Response(Status.STATUS_OK, errors);
        } else {
            return new Response(Status.STATUS_FAILED, errors);
        }
    }

    @GetMapping(value = "/coach")
    public Response getAll() {
        List<Coach> coaches = userService.getAllCoaches();
        for (Coach c : coaches)
            c.setPassword(null);
        return new Response(Status.STATUS_OK, new ArrayList<>(), Pair.of("coaches", coaches));
    }
}
