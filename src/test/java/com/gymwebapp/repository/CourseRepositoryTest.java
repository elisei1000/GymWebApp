package com.gymwebapp.repository;

import com.gymwebapp.domain.Coach;
import com.gymwebapp.domain.Course;
import com.gymwebapp.domain.RepositoryException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CourseRepositoryTest {

    @InjectMocks
    private CourseRepository victim;

    @Mock
    private EntityManager entityManager;

    @Before
    public void setUp() throws Exception {
        victim = new CourseRepository();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCourseExistReturnsCourse() throws Exception {
        //given
        Course course = new Course(1, 2, 8, 8, new Date(), new Date(), 12, new Coach());
        //when
        when(entityManager.find(any(), any())).thenReturn(course);
        //then

        Course course1 = victim.get(1);
        assertTrue(course1.getMaxPlaces() == 12);

    }

    @Test
    public void testGetCourseDontExistThrowsRepositoryException() throws Exception{
        //given
        //when
        when(entityManager.find(any(), any())).thenReturn(null);
        //then
        Course course = victim.get(1);
        assertTrue(course==null);

    }

    @Test
    public void testGetAllCoursesNoCoursesReturnsCourseListSize0() throws Exception{
        //given
        TypedQuery<Object> mock = mock(TypedQuery.class);
        //when
        when(mock.getResultList()).thenReturn(new ArrayList<>());
        when(entityManager.createQuery(any(), any())).thenReturn(mock);
        //then

        List<Course> courseList = victim.getAll();
        assertTrue(courseList.size()==0);

    }

    @Test
    public void testGetAllCoursesReturnCourseList() throws Exception{
        //given
        TypedQuery<Object> mock = mock(TypedQuery.class);
        ArrayList<Object> courses = new ArrayList<>();
        courses.add(new Course(1,2,5,6,new Date(), new Date(),10 ,new Coach()));
        courses.add(new Course(3,3,5,6,new Date(), new Date(),10 ,new Coach()));
        //when
        when(mock.getResultList()).thenReturn(courses);
        when(entityManager.createQuery(any(), any())).thenReturn(mock);
        //then
        List<Course> courseList = victim.getAll();
        assertTrue(courseList.size()==2);
    }

}