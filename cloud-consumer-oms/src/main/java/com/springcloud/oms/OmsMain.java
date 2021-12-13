package com.springcloud.oms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class OmsMain {
    public static void main(String[] args) {
        SpringApplication.run(OmsMain.class,args);
    }
}
