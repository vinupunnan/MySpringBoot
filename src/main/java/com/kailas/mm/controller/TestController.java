package com.kailas.mm.controller;

import com.kailas.mm.scopes.RequestScopeTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    RequestScopeTest test;
    @GetMapping("/requestScope")
    public void testScope(){
        String message = test.getMessage();
        System.out.println(message);
    }
}
