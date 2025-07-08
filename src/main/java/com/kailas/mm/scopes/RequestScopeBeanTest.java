package com.kailas.mm.scopes;


import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST,proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestScopeBeanTest {
    private String message;

    public RequestScopeBeanTest() {
        System.out.println("Request Scoped  Ben callled");
        this.message = "Hello i am in request Scoped  Bean";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
