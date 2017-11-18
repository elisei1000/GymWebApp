package com.gymwebapp.controller;

import com.gymwebapp.model.UserModel;
import com.gymwebapp.service.UserService;
import com.gymwebapp.util.Data;
import com.gymwebapp.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Created by david on 18.11.2017.
 */
@org.springframework.web.bind.annotation.RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public Data login(@RequestBody UserModel user) {
        List<String> errors = userService.checkIfExistUser(user);

        if(errors.size()==0){
            return new Data(Status.STATUS_OK,errors);
        }else{
            return new Data(Status.STATUS_NOT_LOGGED_IN,errors);
        }
    }

    @PostMapping(value = "/register")
    public Data add(@RequestBody UserModel user) {

        List<String> errors = userService.addUser(user);

        if(errors.size()==0){
            return new Data(Status.STATUS_OK,errors);
        }else{
            return new Data(Status.STATUS_FAILED,errors);
        }
    }
}