package com.gymwebapp.repository;

import com.gymwebapp.domain.Client;
import com.gymwebapp.domain.Coach;
import com.gymwebapp.domain.User;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserRepositoryTest {

    @InjectMocks
    private UserRepository victim;

    @Mock
    private EntityManager entityManager;

    @Before
    public void setUp() throws Exception {
        victim = new UserRepository();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckUserPassswordWhenUserIsNullThenReturnFalse() throws Exception {
        //given
        when(entityManager.find(any(), any())).thenReturn(null);
        //when
        boolean lalala = victim.checkUserPassword(new User());
        //then
        assertFalse(lalala);
    }

    @Test
    public void testCheckUserPassswordWhenPasswordIsNullThenReturnFalse() throws Exception {
        //given
        when(entityManager.find(any(), any())).thenReturn(new User());
        //when
        boolean lalala = victim.checkUserPassword(new User());
        //then
        assertFalse(lalala);
    }

    @Test
    public void testCheckUserPasswordWhenUserHasPasswordReturnTrue() throws Exception{
        //given
        User user = new User("aaa","bbb");
        //when
        when(entityManager.find(any(), any())).thenReturn(user);
        //then
        assertTrue(victim.checkUserPassword(new User("aaa","bbb")));
    }

    @Test
    public void testGetAllCoaches() throws Exception{
        //given
        TypedQuery<Object> mock = mock(TypedQuery.class);
        ArrayList<Object> coachList = new ArrayList<>();
        //when
        when(mock.getResultList()).thenReturn(coachList);
        when(entityManager.createQuery(any(), any())).thenReturn(mock);
        //then
        List<Coach> allCoaches = victim.getAllCoaches();
        assertTrue(allCoaches.size()==0);
    }

    @Test
    public void testGetAllClients() throws Exception{
        //given
        TypedQuery<Object> mock = mock(TypedQuery.class);
        ArrayList<Object> clients = new ArrayList<>();
        clients.add(new Client("aaa","bbb","aaa","aaa",new Date()));
        //when
        when(mock.getResultList()).thenReturn(clients);
        when(entityManager.createQuery(any(), any())).thenReturn(mock);
        //then
        List<Client> allClients = victim.getAllClients();
        assertTrue(allClients.size() == 1);

    }


}