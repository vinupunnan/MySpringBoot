package com.kailas.mm.scopes;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope( value = "prototype")
public class ProtoBean {

    public ProtoBean() {
        System.out.println("The protype Bean is invoked from PROTOBEean");
    }
}
