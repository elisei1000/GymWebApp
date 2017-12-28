package com.gymwebapp.Controller;

import com.gymwebapp.controller.UserController;
import com.gymwebapp.domain.Client;
import com.gymwebapp.domain.User;
import com.gymwebapp.model.UserModel;
import com.gymwebapp.util.Response;
import com.gymwebapp.util.Status;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Test
    public void testAddNewUser(){

        UserModel client = new UserModel("testCclient3", "password", "email@yahoo.com", "Elisei",
                new Date(1000));

        Response response = userController.add(client);
        assertThat(response.getStatus()).isEqualTo(Status.STATUS_OK);

    }

    @Test
    public void testGetUserByUsername(){
        UserModel client = new UserModel("testCclient3", "password", "email@yahoo.com", "Elisei",
                new Date(1000));

        Response response = userController.add(client);
        assertThat(response.getStatus()).isEqualTo(Status.STATUS_OK);

        response = userController.getUserByUsername("testCclient3");

        User user = (User)response.getData().get("user");
        assertThat(user.getName()).isEqualTo("testCclient3");
        assertThat(user.getEmail()).isEqualTo("email@yahoo.com");
    }

}
