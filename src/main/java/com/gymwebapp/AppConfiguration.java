package com.gymwebapp;

import com.gymwebapp.domain.Validator.CoachValidator;
import com.gymwebapp.domain.Validator.UserValidator;
import com.gymwebapp.repository.TestRepository;
import com.gymwebapp.service.TestService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by elisei on 15.11.2017.
 */
@EnableAutoConfiguration
@Configuration
public class AppConfiguration {


    @Bean
    public TestService testService(){
        return new TestService();
    }

    @Bean
    public TestRepository testRepository(){
        return new TestRepository();
    }

    @Bean
    public UserValidator userValidator(){return new UserValidator();}

    @Bean
    public CoachValidator coachValidator() {
        return new CoachValidator();
    }


}
