package com.kailas.mm.scopes;

import org.springframework.stereotype.Component;

@Component
public class Single {
    public Single() {
        System.out.println("I am in single class my dude");
    }
}
