package com.springcloud.oms;

import com.springcloud.myrule.MyselfRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

@SpringBootApplication
@EnableEurekaClient
@RibbonClient(name = "mcroservice-payment",configuration = MyselfRule.class)
public class OmsMain {
    public static void main(String[] args) {
        SpringApplication.run(OmsMain.class,args);
    }
}
