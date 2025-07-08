package com.kailas.mm;

import com.kailas.mm.scopes.BeanScopeTestService;
import com.kailas.mm.scopes.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;


@SpringBootApplication
//@EnableFeignClients
//@ComponentScan(basePackages = "com.kailas.mm.*")

public class MMApplication implements CommandLineRunner {

//   //@PostConstruct
//   // public void initLogic(){
//        System.out.println("INSIDE THE POST CONSTRUCT");
//    }


    public static void main(String args[]) {
        System.out.println("Will START RUN METHOD");
        ConfigurableApplicationContext context = SpringApplication.run(MMApplication.class, args);
        System.out.println("After Run Method");
        //Singleton Test
//        BeanScopeTestService service1=context.getBean(BeanScopeTestService.class);
//        System.out.println(service1);
//        BeanScopeTestService service2=context.getBean(BeanScopeTestService.class);
//        System.out.println(service2);
//        BeanScopeTestService service3=context.getBean(BeanScopeTestService.class);
//        System.out.println(service3);
//
//      Single single1 =  context.getBean(Single.class);
//        Single single2 =  context.getBean(Single.class)  ;

    }

    @Override
    public void run(String... args) throws Exception {

    }


//    @Override
//    public void run(String... args) throws Exception {
//        System.out.println("IN COMMAND LINE RUNNER METHOD");
//    }
}
