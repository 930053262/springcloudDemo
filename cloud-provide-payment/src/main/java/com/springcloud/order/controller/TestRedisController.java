package com.springcloud.order.controller;

import com.springCloud.pojo.CommonResult;
import com.springCloud.pojo.Payment;
import com.springcloud.order.service.IRedis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class TestRedisController {

    @Autowired
    private IRedis iRedis;

    @GetMapping("/redis/getdata/{key}")
    public CommonResult getRedisData(@PathVariable("key") String key){
        String value = iRedis.get(key);
        String no = iRedis.getCaseNumber("No");
        System.out.println("==自增序列："+no);
        return new CommonResult(200,"成功",no);
    }
}
