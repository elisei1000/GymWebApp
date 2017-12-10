package com.gymwebapp;

import com.gymwebapp.domain.*;
import com.gymwebapp.repository.SubscriptionRepository;
import com.gymwebapp.service.SubscriptionService;
import com.gymwebapp.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

@SpringBootApplication
public class GymwebappApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymwebappApplication.class, args);
	}
	@Bean
	public CommandLineRunner demo(UserService userService, SubscriptionService subscriptionService) {
		return (String... args) -> {

			User user =new User();
			user.setUsername("test");
			user.setPassword("some");
			user.setEmail("example@yahoo.com");
			user.setBirthDay(new Date());
			user.setName("Test");
			userService.addUser(user);
			Administrator admin = new Administrator("admin", "admin", "admin@yahoo.com", "Administrator", new Date());
			userService.addUser(admin);
			Coach coach = new Coach("coach", "coach", "coach@yahoo.com", "Coach", new Date());
			userService.addUser(coach);
		};
	}
}
