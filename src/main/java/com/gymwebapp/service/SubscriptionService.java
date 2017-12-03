package com.gymwebapp.service;

import com.gymwebapp.domain.RepositoryException;
import com.gymwebapp.domain.Subscription;
import com.gymwebapp.domain.Validator.SubscriptionValidator;
import com.gymwebapp.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SubscriptionService {
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Transactional
    public void addSubscription(Subscription subscription) {
        try {
            subscriptionRepository.add(subscription);
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void updateSubscription(Subscription subscription) {
        try {
            subscriptionRepository.update(subscription);
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }


    @Transactional
    public Subscription find(Integer id) {
        return subscriptionRepository.get(id);
    }
}

