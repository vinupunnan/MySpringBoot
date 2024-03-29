package com.kailas.mm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
//@EnableFeignClients
//@ComponentScan(basePackages = "com.kailas.mm.*")
public class MMApplication {

    public static void main(String args[]){
        SpringApplication.run(MMApplication.class,args);

    }


}
