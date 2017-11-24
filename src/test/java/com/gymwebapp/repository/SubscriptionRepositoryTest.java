package com.gymwebapp.repository;

import com.gymwebapp.domain.Subscription;
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

public class SubscriptionRepositoryTest {

    @InjectMocks
    private SubscriptionRepository victim;

    @Mock
    private EntityManager entityManager;

    @Before
    public void setUp() throws Exception {
        victim = new SubscriptionRepository();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetSubscriptionReturnNull() throws Exception{
        //when
        when(entityManager.find(any(), any())).thenReturn(null);
        //then
        Subscription subscription = victim.get(1);
        assertTrue(subscription == null);
    }

    @Test
    public void testGetSubscriptionReturnSubscription() throws Exception{
        //given
        Subscription subscription = new Subscription(3,new Date(), new Date());
        //when
        when(entityManager.find(any(), any())).thenReturn(subscription);
        //then
        Subscription subscription1 = victim.get(3);
        assertTrue(subscription1!=null);
    }

    @Test
    public void testGetAllNoSubscriptions() throws Exception{
        //given
        TypedQuery<Object> mock = mock(TypedQuery.class);
        //when
        when(mock.getResultList()).thenReturn(new ArrayList<>());
        when(entityManager.createQuery(any(), any())).thenReturn(mock);
        //then
        List<Subscription> subscriptions = victim.getAll();
        assertTrue(subscriptions.size()==0);
    }

    @Test
    public void testGetAllSubscriptions() throws Exception{
        //given
        TypedQuery<Object> mock = mock(TypedQuery.class);
        ArrayList<Object> subs = new ArrayList<>();
        subs.add(new Subscription(1, new Date(), new Date()));
        subs.add(new Subscription(2, new Date(), new Date()));
        //when
        when(mock.getResultList()).thenReturn(subs);
        when(entityManager.createQuery(any(), any())).thenReturn(mock);
        //then
        List<Subscription> subscriptions = victim.getAll();
        assertTrue(subscriptions.size()==2);
    }

    @Test
    public void testcheckIfSubscriptionExistsReturnFalse() throws Exception{
        //when
        when(entityManager.find(any(), any())).thenReturn(null);
        //then
        boolean subscription = victim.checkIfSubscriptionExists(1);
        assertTrue(subscription == false);
    }

    @Test
    public void testcheckIfSubscriptionExistsReturnTrue() throws Exception{
        //given
        Subscription subscription = new Subscription(3,new Date(), new Date());
        //when
        when(entityManager.find(any(), any())).thenReturn(subscription);
        //then
        boolean exists = victim.checkIfSubscriptionExists(1);
        assertTrue(exists);
    }

}