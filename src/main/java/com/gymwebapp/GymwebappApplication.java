package com.gymwebapp;

import com.gymwebapp.domain.*;
import com.gymwebapp.model.CourseModel;
import com.gymwebapp.repository.CourseRepository;
import com.gymwebapp.repository.SubscriptionRepository;
import com.gymwebapp.service.CourseService;
import com.gymwebapp.service.SubscriptionService;
import com.gymwebapp.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class GymwebappApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymwebappApplication.class, args);
	}
	@Bean
	public CommandLineRunner demo(UserService userService, SubscriptionService subscriptionService, CourseService courseService) {
		return (String... args) -> {

			Client client =new  Client();
			client.setUsername("test");
			client.setPassword("some");
			client.setEmail("example@yahoo.com");
			client.setBirthDay(new Date(81590522));
			client.setName("Test");
			userService.addUser(client);
			Administrator admin = new Administrator("admin", "admin", "admin@yahoo.com", "Administrator", new Date(81590522));
			userService.addUser(admin);
			Coach coach = new Coach("coach", "coach", "coach@yahoo.com", "Coach", new Date(81590522));
			System.out.println(userService.addUser(coach));

			List<CourseModel> courseList=courseService.getAll();
			int i=courseList.size() - 1;

			for(;i <10; i++){
				CourseModel course = new CourseModel();
				course.setTitle(String.format("Course #%d", i+1));
				course.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur dui nunc, consectetur facilisis placerat et, placerat sit amet eros. Maecenas convallis diam in tempus scelerisque. Nunc eget neque blandit quam pulvinar finibus interdum sit amet felis. Sed eu purus nec enim accumsan porttitor eget et lectus. In id placerat ipsum, non porta enim. Phasellus at enim vel mauris auctor egestas. Donec sodales facilisis ante, ac egestas arcu vehicula eget.\n"
								+
								"\n" +
								"In finibus metus ante, non finibus est tempor aliquam. Phasellus at massa in felis finibus congue. Aenean vehicula lobortis mauris, eu elementum tellus semper vitae. Fusce laoreet dolor enim, et convallis mi tempor quis. Donec non sem at nisl imperdiet molestie quis non arcu. Praesent non ipsum cursus, consequat ligula eu, facilisis nisi. Nam eget leo quis nunc pharetra suscipit.\n" +
								"\n" +
								"Nullam id lacus elit. In euismod scelerisque aliquam. Phasellus sit amet arcu enim. Ut dui neque, sollicitudin sed tempor ut, euismod nec libero. Donec porttitor augue id libero sagittis posuere. Nullam vulputate odio eget malesuada vehicula. Integer vitae mi interdum, dapibus dui eget, fringilla est. Cras eu augue et sapien tempus porttitor. Curabitur sodales risus quis leo pulvinar maximus. Cras ut feugiat nisi. Quisque auctor metus libero, at pretium dui convallis et. Quisque nisi ex, lacinia id euismod non, pretium vel nulla. Duis rhoncus tortor ac turpis dignissim, et convallis ligula rutrum. Curabitur ac malesuada nisl, id ornare nisi."
						);
				course.setId(1);
				course.setStartHour(10);
				course.setEndHour(13);
				course.setStartDate(new Date());
				course.setEndDate(new Date());
				course.setDifficultyLevel((i % 5) + 1);
				course.setMaxPlaces(10);
				courseService.addCourse(course, coach);
			}

		};
	}
}
