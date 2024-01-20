package com.kailas.mm;

import com.kailas.mm.scopes.BeanScopeTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
//@EnableFeignClients
//@ComponentScan(basePackages = "com.kailas.mm.*")

public class MMApplication {


    public static void main(String args[]){
        ConfigurableApplicationContext context =  SpringApplication.run(MMApplication.class,args);
       //Singleton Test
        BeanScopeTestService service1=context.getBean(BeanScopeTestService.class);
        BeanScopeTestService service2=context.getBean(BeanScopeTestService.class);
        BeanScopeTestService service3=context.getBean(BeanScopeTestService.class);
    }


}
