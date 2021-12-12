package com.springcloud.demo.Controller;

import com.springCloud.pojo.CommonResult;
import com.springCloud.pojo.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

@Controller
@Slf4j
public class OrderController {

    //调用支付订单服务端的ip+端口号
    public static final  String PAYMENT_URL = "http://localhost:8001";

    @Autowired
    private RestTemplate restTemplate;
    //创建支付订单的接口
    @GetMapping("/consumer/payment/create")
    public CommonResult<Payment> create(Payment payment){
        return restTemplate.postForObject(PAYMENT_URL+"/payment/create",payment, CommonResult.class);
    }
    //获取id获取支付订单
    @GetMapping("/consumer/payment/get/{id}")
    public CommonResult<Payment> getPayment(@PathVariable("id") Long id){
        CommonResult forObject = restTemplate.getForObject(PAYMENT_URL + "/payment/get/" + id, CommonResult.class);
        System.out.println("你好：get接口");
        return forObject;
    }

    //获取id获取支付订单
    @GetMapping("/consumer/payment/sayHello")
    public CommonResult<Payment> sayHello(){
        System.out.println("你好：hello");
        return new CommonResult(200,"请你说：","hello");
    }

    //获取id获取支付订单
    @GetMapping("/consumer/myConsumer")
    public CommonResult<Payment> myConsumer(){
        System.out.println("你好：myConsumer");
        return new CommonResult(200,"请你说：","myConsumer");
    }
}
