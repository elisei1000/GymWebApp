package com.gymwebapp.Service;


import com.gymwebapp.domain.Client;
import com.gymwebapp.domain.Coach;
import com.gymwebapp.domain.RepositoryException;
import com.gymwebapp.domain.User;
import com.gymwebapp.domain.Validator.UserValidator;
import com.gymwebapp.repository.UserRepository;
import com.gymwebapp.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;


import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@DataJpaTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserValidator userValidator;

    @Test
    public void testAddNewUserShouldAdd(){

        User user = new Coach("testcoach1", "aa", "aaaa@yahoo.com", "aa",new Date());
        long size = userService.getAllCoaches().size();
        List<String> errors = userService.addUser(user);
        System.out.println(errors);
        assertThat(errors.size()).isEqualTo(0);
        assertThat((long)userService.getAllCoaches().size()).isEqualTo(size+1L);

        userService.removeUser(user);
        assertThat((long)userService.getAllCoaches().size()).isEqualTo(size);

    }

    @Test
    public void testFindUserShouldFind(){

        User user = new Coach("testcoach1", "aa", "aaaa@yahoo.com", "aa",new Date(1000));

        List<String> errors = userService.addUser(user);
        assertThat(errors.size()).isEqualTo(0);
        User u = userService.findUser("testcoach1");
        assertThat(u.getName()).isEqualTo("aa");
        assertThat(u.getEmail()).isEqualTo("aaaa@yahoo.com");

        userService.removeUser(user);

    }

    @Test
    public void testRemoveUserShouldRemove(){

        User user = new Coach("testcoach2", "dd", "test@yahoo.com", "aa",new Date(1000));

        long dim = userService.getSize();
        List<String> errors = userService.addUser(user);
        assertThat(userService.getSize()).isEqualTo(dim+1);
        assertThat(errors.size()).isEqualTo(0);

        userService.removeUser(user);
        assertThat(userService.getSize()).isEqualTo(dim);

    }

    @Test
    public void testRemoveUserShouldReturnErrors(){

        User user = new User();

        List<String> errors = userService.removeUser(user);
        assertThat(errors.size()).isEqualTo(1);

    }


    @Test
    public void testUpdateUserShouldUpdate(){

        User user = new Coach("testuser1", "aadd", "test@yahoo.com", "aa",new Date(1000));

        List<String> errors = userService.addUser(user);
        assertThat(errors.size()).isEqualTo(0);
        User u = userService.findUser("testuser1");
        assertThat(u.getName()).isEqualTo("aa");
        assertThat(u.getEmail()).isEqualTo("test@yahoo.com");

        User user1 = new Coach("testuser1", "aadd", "test1@yahoo.com", "dd",new Date(1000));
        userService.updateUser(user1);

        assertThat(u.getName()).isEqualTo("dd");
        assertThat(u.getEmail()).isEqualTo("test1@yahoo.com");


        userService.removeUser(user);

    }
}
