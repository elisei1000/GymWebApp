package com.gymwebapp.repository;

import com.gymwebapp.domain.*;
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
import java.util.List;

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

    @Test
    public void testAddNewCoachClientWithTheSameUsernameShouldThrowException(){

        Date date = new Date();
        Subscription subscription = new Subscription(1,new Date(), new Date());
        Client client = new Client("username", "password", "email", "Elisei",
                date, subscription);
        try {
            userRepository.add(client);

            Coach coach = new Coach("username", "pass", "coach@yahoo.com", "grigore", new Date());

            try{
                userRepository.add(coach);
                assert(false);
            }catch (RepositoryException e){
                assert(true);
            }

        }catch (RepositoryException e){
            assert(false);
        }

    }

    @Test
    public void testUpdateCoachShouldUpdate(){

        Coach coach = new Coach("username", "password", "email", "name", new Date());

        try{

            userRepository.add(coach);
            Coach coach1 = new Coach("username", "password1", "email1", "name1", new Date());

            userRepository.update(coach1);

            coach = (Coach) userRepository.get("username");
            assertThat(coach.getPassword()).isEqualTo("password1");
            assertThat(coach.getEmail()).isEqualTo("email1");
            assertThat(coach.getName()).isEqualTo("name1");

        }catch (RepositoryException e){
            assert(false);
        }

    }

    @Test
    public void testUpadteClientNotFoundShouldThrowException(){
        Client client=new Client("username", "password", "email", "Elisei",
                new Date(), new Subscription(1, new Date(), new Date()));
        try{

            userRepository.update(client);
            assert(false);
        }catch (RepositoryException e){
            assert(true);
        }
    }

    @Test
    public void testDeleteAdministratorExistsShouldDelete(){

        Administrator administrator = new Administrator("administrator", "password"
                , "email", "name", new Date());

        try{

            userRepository.add(administrator);
            assertThat(userRepository.size()).isEqualTo(1);

            userRepository.remove("administrator");
            assertThat(userRepository.size()).isEqualTo(0);
        }catch (RepositoryException e){
            assert(false);
        }
    }

    @Test
    public void testDeleteAdministratorDontExistShouldThrowException(){
        try {
            userRepository.remove("aaa");
            assert(false);
        }catch (RepositoryException e){
            assert(true);
        }
    }

    @Test
    public void testGetAllCoachesAndGetAllClientsAndAllAdministrators(){
        try {
            userRepository.add(new Client("a", "a", "b", "a", new Date()));
            userRepository.add(new Client("b", "a", "b", "a", new Date()));
            userRepository.add(new Client("c", "a", "b", "a", new Date()));

            userRepository.add(new Coach("aa", "aa", "aa", "aa",new Date()));
            userRepository.add(new Coach("ab", "aa", "aa", "aa",new Date()));
            userRepository.add(new Coach("ac", "aa", "aa", "aa",new Date()));
            userRepository.add(new Coach("ad", "aa", "aa", "aa",new Date()));

            userRepository.add(new Administrator("administrator", "password", "email", "name", new Date()));
            userRepository.add(new Administrator("administrator1", "password", "email", "name", new Date()));
            userRepository.add(new Administrator("administrator2", "password", "email", "name", new Date()));


            assertThat(userRepository.getAllCoaches().size()).isEqualTo(4);
            assertThat(userRepository.getAllClients().size()).isEqualTo(3);
            assertThat(userRepository.getAllAdministrators().size()).isEqualTo(3);

            assertThat(userRepository.getAll().size()).isEqualTo(10);

        }catch (RepositoryException e){
            assert(false);
        }
    }


}
