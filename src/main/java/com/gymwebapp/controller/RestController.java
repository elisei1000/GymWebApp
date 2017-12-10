package com.gymwebapp.controller;

import com.gymwebapp.domain.Test;
import com.gymwebapp.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Date;
import java.util.List;

/**
 * Created by elisei on 15.11.2017.
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")
public class RestController {

    @Autowired
    private TestService testService;
    private static  int lastId= 0;

    @RequestMapping(value="/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String Hello( @PathVariable String name
    ){
        return "Hello " + name;
    }

    @RequestMapping(value = "/request1", method = RequestMethod.POST)
    public String handler(@RequestParam(name = "name") @DateTimeFormat(pattern="yyyy-MM-dd") Date date) {
       return "POST: " + date.toString();
    }

    @RequestMapping(method = RequestMethod.GET, produces= MediaType.APPLICATION_JSON_VALUE)
    public List<Test> getAll(){
        return testService.getAll();
    }

    @RequestMapping(value="/add/{name}", method = RequestMethod.GET)
    public void addTest(@PathVariable  String name){
        testService.addTest(new Test(name));
    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
    public void deleteTest(@PathVariable Integer id){
        testService.deleteTest(id);
    }


    @RequestMapping(value="/update/{id}/{name}", method = RequestMethod.GET)
    public void updateTest(@PathVariable Integer id, @PathVariable String name){
        testService.update(id, new Test(name));
    }
}