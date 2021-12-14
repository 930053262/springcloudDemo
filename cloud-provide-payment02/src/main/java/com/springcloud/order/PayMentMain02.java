package com.springcloud.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class PayMentMain02 {

    public static void main(String[] args) {
        SpringApplication.run(PayMentMain02.class,args);
    }


}
