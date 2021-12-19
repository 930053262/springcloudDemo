package com.springcloud.geteway;

import com.springcloud.geteway.filter.TokenFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class GatewayMain {

    public static void main(String[] args) {
        SpringApplication.run(GatewayMain.class,args);
    }


    @Bean
    public TokenFilter tokenFilter(){
        return new TokenFilter();
    }

}
