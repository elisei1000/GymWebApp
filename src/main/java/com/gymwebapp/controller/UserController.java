package com.gymwebapp.controller;

import com.gymwebapp.domain.Client;
import com.gymwebapp.domain.User;
import com.gymwebapp.model.UserModel;
import com.gymwebapp.service.UserService;
import com.gymwebapp.util.Response;
import com.gymwebapp.util.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Created by david on 18.11.2017.
 */
@CrossOrigin
@org.springframework.web.bind.annotation.RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public Response login(@RequestBody UserModel userModel) {
        User user = new User(userModel.getUsername(), userModel.getPassword(),
                userModel.getEmail(), userModel.getName(), userModel.getBirthDay());
        List<String> errors = userService.checkIfExistUser(user);

        if(errors.size()==0){
            return new Response(Status.STATUS_OK, errors);
        }else{
            return new Response(Status.STATUS_FAILED, errors);
        }
    }

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
}