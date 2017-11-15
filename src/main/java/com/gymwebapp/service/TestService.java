package com.gymwebapp.service;

import com.gymwebapp.domain.Test;
import com.gymwebapp.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by elisei on 15.11.2017.
 */
@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;


    @Transactional
    public void addTest(Test test){
        testRepository.add(test);
    }

    @Transactional
    public List<Test> getAll() {
        return testRepository.getAll();
    }

    @Transactional
    public void deleteTest(Integer id) {
        testRepository.delete(id);
    }


    @Transactional
    public void update(Integer id, Test test) {
        testRepository.update(id, test);
    }
}
