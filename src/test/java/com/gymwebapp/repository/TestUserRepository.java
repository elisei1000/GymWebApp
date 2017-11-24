package com.gymwebapp.repository;

import com.gymwebapp.domain.User;
import junit.framework.TestResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


import static org.assertj.core.api.Assertions.*;
/**
 * Created by elisei on 23.11.2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestUserRepository{



    @Test
    @Transactional
    public void test(){
        UserRepository userRepository = new UserRepository();

        User user = userRepository.get("elisei1000");

        assertThat(user).isEqualTo(null);


    }

}
