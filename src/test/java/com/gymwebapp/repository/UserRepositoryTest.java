package com.gymwebapp.repository;

import com.gymwebapp.domain.Client;
import com.gymwebapp.domain.RepositoryException;
import com.gymwebapp.domain.Subscription;
import com.gymwebapp.domain.User;
import junit.framework.TestResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;

import static org.assertj.core.api.Assertions.*;
/**
 * Created by elisei on 23.11.2017.
 */

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest{

    @Autowired
    private TestEntityManager entityManager;


    @Autowired
    private UserRepository userRepository;

    @Test
    public void testAddNewUserNotExistingAndExistingShouldAddInRepo(){

        try {
            assertThat(userRepository.size()).isEqualTo(0);
            Date date = new Date();
            userRepository.add(new User("username", "password", "email", "Elisei",
                    date));
            assertThat(userRepository.size()).isEqualTo(1);

            User user = userRepository.get("username");
            assertThat(user.getPassword()).isEqualTo("password");
            assertThat(user.getBirthDay()).isEqualTo(date);
            assertThat(user.getEmail()).isEqualTo("email");
            assertThat(user.getName()).isEqualTo("Elisei");

            try {
                userRepository.add(new User("username", "password", "email", "Elisei",
                        date));
                assertThat(true).isFalse();
            }catch(RepositoryException e){
                assert(true);
            }
        } catch (RepositoryException e) {
            assertThat(true).isFalse();
        }
    }

    @Test
    public void testAddNewClientNotExistingShouldAddInRepo(){

        try{

            assertThat(userRepository.size()).isEqualTo(0);
            Date date = new Date();
            Date startDate = new Date();
            Date endDate = new Date();
            Subscription subscription = new Subscription(1,startDate, endDate);
            userRepository.add(new Client("username", "password", "email", "Elisei",
                    date, subscription));

            Client client = (Client) userRepository.get("username");
            assertThat(client.getSubscription().getId()).isEqualTo(1);
            assertThat(client.getSubscription().getEndDate()).isEqualTo(endDate);
            assertThat(client.getSubscription().getStartDate()).isEqualTo(startDate);



        }catch(RepositoryException e){
            assertThat(true).isFalse();
        }
    }



}
