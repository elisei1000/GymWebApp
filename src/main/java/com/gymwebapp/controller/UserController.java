package com.gymwebapp.controller;

import com.gymwebapp.model.UserModel;
import com.gymwebapp.service.UserService;
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
    public ResponseEntity login(@RequestBody UserModel user) {
        List<String> errors = userService.checkIfExistUser(user);

        if (errors.size() == 0) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Status.STATUS_OK);
        }
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(errors);
    }

    @PostMapping(value = "/register")
    public ResponseEntity add(@RequestBody UserModel user) {

        List<String> errors = userService.addUser(user);

        if (errors.size() == 0) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Status.STATUS_OK);
        }

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(errors);
    }
}