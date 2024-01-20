package com.kailas.mm.scopes;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class BeanScopeTestService {
    public BeanScopeTestService() {
        System.out.println("Bean invokded ");
    }
}
