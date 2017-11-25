package com.gymwebapp.repository;

import com.gymwebapp.domain.Coach;
import com.gymwebapp.domain.Course;
import com.gymwebapp.domain.RepositoryException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@DataJpaTest
public class CourseRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() throws Exception {


        userRepository.add(new Coach("coach1", "pass"));
        userRepository.add(new Coach("coach2", "pass"));
        userRepository.add(new Coach("coach3", "pass"));
        userRepository.add(new Coach("coach4", "pass"));

        courseRepository.add(new Course("course1","no desc",2,4,5,new Date(), new Date(), 10, (Coach)userRepository.get("coach1") ));
        courseRepository.add(new Course("course2","desc", 2,4,5,new Date(), new Date(), 10, (Coach)userRepository.get("coach2")));
        courseRepository.add(new Course("course2","desc23",2,4,5,new Date(), new Date(), 10, (Coach)userRepository.get("coach3")));
        courseRepository.add(new Course("course3","desc",2,4,5,new Date(), new Date(), 10, (Coach)userRepository.get("coach2")));

    }

    @Test
    public void testAddCourseNotExistingShouldAddInRepository(){

        try {

            courseRepository.add(new Course(2,4,5,new Date(), new Date(), 10, (Coach)userRepository.get("coach1")));
//            javax.persistence.PersistenceException: org.hibernate.PersistentObjectException: detached entity passed to persist: com.gymwebapp.domain.Course
//            courseRepository.add(new Course(2,4,5,new Date(), new Date(), 10));

            assertThat(courseRepository.size()).isEqualTo(5);

            Course course = courseRepository.get(5);
            assertThat(course.getMaxPlaces()).isEqualTo(10);
            assertThat(course.getStartHour()).isEqualTo(4);
            assertThat(course.getEndHour()).isEqualTo(5);
            assertThat(course.getDifficultyLevel()).isEqualTo(2);

        }catch (RepositoryException e){
            assert(false);
        }
    }

    @Test
    public void testAddUpdateCourseShouldUpdateCourse(){

        Course course = new Course("course3","desc",9,17,19,new Date(), new Date(), 20, (Coach)userRepository.get("coach4"));
        int id = courseRepository.getAll().get(0).getId();
        course.setId(id);

        try{

            courseRepository.update(course);

            course = courseRepository.get(id);
            assertThat(course.getDifficultyLevel()).isEqualTo(9);
            assertThat(course.getStartHour()).isEqualTo(17);
            assertThat(course.getEndHour()).isEqualTo(19);
            assertThat(course.getMaxPlaces()).isEqualTo(20);

        }catch (RepositoryException e){
            assert(false);
        }

    }

    @Test
    public void testGetAllCoachShouldReturnAllForCoach(){

        Coach coach = (Coach) userRepository.get("coach2");
        assertThat(courseRepository.getAllCoursesForCoach(coach).size()).isEqualTo(2);
    }

    @Test
    public void testDeleteCourseCourseShouldBeDeleted(){

        try{

            int id = courseRepository.getAll().get(0).getId();

            courseRepository.remove(id);
            assertThat(courseRepository.size()).isEqualTo(3);

        }catch (RepositoryException e){
            assert(false);
        }

    }

    @Test
    public void testDeleteCourseCourseDoesntExistThrowsRepositoryException() {
        try{

            courseRepository.remove(20);
            assert(false);

        }catch (RepositoryException e){
            assert(true);
        }
    }

}