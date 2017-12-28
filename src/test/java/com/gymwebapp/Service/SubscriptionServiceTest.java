package com.gymwebapp.Service;

import com.gymwebapp.domain.Client;
import com.gymwebapp.domain.Subscription;
import com.gymwebapp.service.SubscriptionService;
import com.gymwebapp.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@RunWith(SpringRunner.class)
public class SubscriptionServiceTest {

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private UserService userService;

    @Test
    public void testAddNewSubscriptionFindDelete(){

        Client client = new Client("testCclient3", "password", "email@yahoo.com", "Elisei",
                new Date(1000));
        userService.addUser(client);

        long dim = subscriptionService.size();

        Subscription subscription = new Subscription(new Date(10000), new Date(20000), client);
        subscriptionService.addSubscription(subscription);

        Subscription sub = subscriptionService.find(subscriptionService.getLastId());

        assertThat(sub.getEndDate()).isEqualTo(new Date(20000));
        assertThat(sub.getStartDate()).isEqualTo(new Date(10000));
        assertThat(subscriptionService.size()).isEqualTo(dim+1);

        subscriptionService.deleteSubscription(sub);
        assertThat(subscriptionService.size()).isEqualTo(dim);

    }

    @Test
    public void testUpdate(){

        Client client = new Client("testCclient3", "password", "email@yahoo.com", "Elisei",
                new Date(1000));
        userService.addUser(client);

        Subscription subscription = new Subscription(new Date(10000), new Date(20000), client);
        subscriptionService.addSubscription(subscription);
        subscription = subscriptionService.find(subscriptionService.getLastId());
        subscription.setEndDate(new Date(15000));
        subscription.setStartDate(new Date(12000));

        subscriptionService.updateSubscription(subscription);

        assertThat(subscriptionService.find(subscriptionService.getLastId()).getStartDate()).isEqualTo(new Date(12000));
        assertThat(subscriptionService.find(subscriptionService.getLastId()).getEndDate()).isEqualTo(new Date(15000));

        subscriptionService.deleteSubscription(subscription);
    }

}
