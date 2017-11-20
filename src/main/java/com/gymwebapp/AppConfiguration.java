package com.gymwebapp;

import com.gymwebapp.domain.Validator.UserValidator;
import com.gymwebapp.repository.TestRepository;
import com.gymwebapp.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;

import javax.persistence.EntityManagerFactory;

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
}
