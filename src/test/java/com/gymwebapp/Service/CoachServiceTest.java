package com.gymwebapp.Service;

import com.gymwebapp.domain.Coach;
import com.gymwebapp.service.CoachService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CoachServiceTest {
    @Autowired
    private CoachService coachService;

    @Test
    public void testAddNewCoachShouldAdd(){
        Coach coach = new Coach("testCoach", "aA!-67", "testCoach@yahoo.com", "Marius Pop", new Date(2000), "best coach");
        long size = coachService.getAllCoaches().size();
        List<String> errors = coachService.addCoach(coach);
        System.out.println(errors);
        assertThat(errors.size()).isEqualTo(0);

        Coach invalidCoach1 = new Coach("testCoach", "aA!-67", "testCoach@yahoo.com", "Marius Pop", new Date(2000), "best coach");
        errors = coachService.addCoach(invalidCoach1);
        assertThat(errors.size()).isEqualTo(1);

        Coach invalidCoach2 = new Coach("testCoach", "aA!-67", "testCoach@yahoo.com", "Marius Pop", new Date(2000), null);
        errors = coachService.addCoach(invalidCoach2);
        assertThat(errors.size()).isEqualTo(1);

        assertThat((long) coachService.getAllCoaches().size()).isEqualTo(size + 1L);

        coachService.removeCoach(coach);
        assertThat((long) coachService.getAllCoaches().size()).isEqualTo(size);
    }

    @Test
    public void testFindCoachShouldFind(){

        Coach coach = new Coach("testCoach", "aA!-67", "testCoach@yahoo.com", "Marius Pop", new Date(2000), "best coach");

        List<String> errors = coachService.addCoach(coach);
        System.out.println(errors);
        assertThat(errors.size()).isEqualTo(0);

        Coach c = coachService.getCoach("testCoach");
        assertThat(c.getName()).isEqualTo("Marius Pop");
        assertThat(c.getEmail()).isEqualTo("testCoach@yahoo.com");

        coachService.removeCoach(coach);
    }

    @Test
    public void testRemoveCoachShouldRemove(){

        Coach coach = new Coach("testCoach", "aA!-67", "testCoach@yahoo.com", "Marius Pop", new Date(2000), "best coach");
        int dim = coachService.getAllCoaches().size();
        List<String> errors = coachService.addCoach(coach);
        System.out.println(errors);
        assertThat(coachService.getAllCoaches().size()).isEqualTo(dim+1);
        assertThat(errors.size()).isEqualTo(0);

        coachService.removeCoach(coach);
        assertThat(coachService.getAllCoaches().size()).isEqualTo(dim);

    }

    @Test
    public void testRemoveCoachShouldReturnErrors(){

        Coach coach = new Coach();

        List<String> errors = coachService.removeCoach(coach);
        assertThat(errors.size()).isEqualTo(1);
    }


    @Test
    public void testUpdateCoachShouldUpdate(){

        Coach coach = new Coach("testCoach", "aA!-67", "testCoach@yahoo.com", "Marius Pop", new Date(2000), "best coach");
        List<String> errors = coachService.addCoach(coach);
        assertThat(errors.size()).isEqualTo(0);
        Coach c = coachService.getCoach("testCoach");
        assertThat(c.getName()).isEqualTo("Marius Pop");
        assertThat(c.getEmail()).isEqualTo("testCoach@yahoo.com");

        Coach toUpdate = new Coach("testCoach", "aA!-67", "testCoach2@yahoo.com", "Marius Pope", new Date(2000), "best coach");
        coachService.updateCoach(toUpdate);

        assertThat(toUpdate.getName()).isEqualTo("Marius Pope");
        assertThat(toUpdate.getEmail()).isEqualTo("testCoach2@yahoo.com");

        coachService.removeCoach(coach);
    }
}