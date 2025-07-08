package com.kailas.mm.controller;


import com.kailas.mm.scopes.RequestScopeBeanTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    RequestScopeBeanTest requestScopeBeanTest;

   @GetMapping("/invoke/requestscope")
    public void getRequestScopedBean(){

       System.out.println(requestScopeBeanTest.getMessage());
    }

}
