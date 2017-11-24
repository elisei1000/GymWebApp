package com.gymwebapp.repository;

import com.gymwebapp.domain.Client;
import com.gymwebapp.domain.Feedback;
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

public class FeedBackRepositoryTest {

    @InjectMocks
    private FeedBackRepository victim;

    @Mock
    private EntityManager entityManager;

    @Before
    public void setUp() throws Exception {
        victim = new FeedBackRepository();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetIntegerFoundReturnFeedback() throws Exception{
        //given
        Feedback feedback = new Feedback(1,2,"a","a",new Date(),new Client());
        //when
        when(entityManager.find(any(), any())).thenReturn(feedback);
        //then
        Feedback feedback1 = victim.get(1);
        assertTrue(feedback1.getId()==1);
    }

    @Test
    public void testGetIntegerNotFoundThrowsRepositoryException() throws Exception{
        //when
        when(entityManager.find(any(), any())).thenReturn(null);
        //then
        Feedback feedback1 = victim.get(1);
        assertTrue(feedback1==null);
    }

    @Test
    public void testGetAllNoFeedbacksThrowRepositoryException() throws Exception{
        //given
        TypedQuery<Object> mock = mock(TypedQuery.class);
        ArrayList<Object> feedbacks = new ArrayList<>();
        //when
        when(mock.getResultList()).thenReturn(feedbacks);
        when(entityManager.createQuery(any(), any())).thenReturn(mock);
        //then

        List<Feedback> feedbackList = victim.getAll();
        assertTrue(feedbackList.size()==0);


    }

    @Test
    public void testGetAllAtLeastOneFeedbackReturnsList() throws Exception{
        //given
        TypedQuery<Object> mock = mock(TypedQuery.class);
        ArrayList<Object> feedbacks = new ArrayList<>();
        feedbacks.add(new Feedback(1,1,"a","a",new Date(), new Client()));
        feedbacks.add(new Feedback(2,3,"ba","ab",new Date(), new Client()));
        //when
        when(mock.getResultList()).thenReturn(feedbacks);
        when(entityManager.createQuery(any(), any())).thenReturn(mock);
        //then
        List<Feedback> feedbackList = victim.getAll();
        assertTrue(feedbackList.size() == 2);

    }

}