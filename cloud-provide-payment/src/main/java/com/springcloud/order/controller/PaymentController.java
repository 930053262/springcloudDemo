package com.springcloud.order.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.springCloud.pojo.CommonResult;
import com.springCloud.pojo.Payment;
import com.springcloud.order.config.AmqpConfig;
import com.springcloud.order.service.IRedis;
import com.springcloud.order.service.PaymentService;
import com.springcloud.order.service.QueueMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;


/*
 * 提供restful服务  供其他服务调用
 *
 * */
@RestController
@Slf4j
public class PaymentController implements ApplicationListener<WebServerInitializedEvent >{

    @Autowired
    private PaymentService paymentService;

    private int serverPort;

    @Autowired
    private IRedis iRedis;

    @Autowired
    private QueueMessageService queueMessageService;

    //注入服务发现的注解
    @Autowired
    private DiscoveryClient discoveryClient;

    @PostMapping("/payment/create")
    public CommonResult create(@RequestBody Payment dept){
        int i = paymentService.create(dept);
        log.info("***************插入成功*******"+i);
        if(i>0){
            return new CommonResult(200,"插入数据库成功"+serverPort,i);
        }else{
            return new CommonResult(444,"插入数据库失败",null);
        }
    }

    @GetMapping("/payment/get/{key}")
    public CommonResult queryById(@PathVariable("key") String id){
       /* Payment payment = paymentService.queryById(id);
        log.info("*******erere查询成功*********"+payment);

        if(payment!=null){
            return new CommonResult(200,"查询成功"+serverPort,payment);
        }else{
            return new CommonResult(444,"查询失败",null);
        }*/
        iRedis.set(id,"testValue",2000);

        String no = iRedis.getCaseNumber("No");
        System.out.println("==自增序列："+no);

        System.out.println("======"+id);
        String value = iRedis.get(id);
        System.out.println("value="+value);

        //msec：距离调用队列的时间（毫秒） param_extend：向队列传递的参数
        int msec = Integer.parseInt(Long.toString(DateUtil.betweenMs(new Date(), new Date())))+5000;
        HashMap<String, Object> param_extend = new HashMap<>();
        param_extend.put("id","123456");
        queueMessageService.delayedSend(AmqpConfig.DELAYED_EXCHANGE_KEY, AmqpConfig.ORDER_GROUP_JOIN_QUEUE_KEY, JSON.toJSONString(param_extend), msec);

        return new CommonResult(200,"成功",value);
    }

    @GetMapping("/payment/lb/{id}")
    public CommonResult queryLb(@PathVariable("id") Long id){
        Payment payment = paymentService.queryById(id);
        log.info("***************查询成功*********"+payment);

        if(payment!=null){
            return new CommonResult(200,"查询成功"+serverPort,payment);
        }else{
            return new CommonResult(444,"查询失败",null);
        }
    }

    //获取服务信息
    @GetMapping("/payment/discovery")
    public  Object discovery(){
        List<String> services = discoveryClient.getServices();
        for (String s : services){
            log.info("********注册到eureka中的服务中有:"+services);
        }
        List <ServiceInstance> instances = discoveryClient.getInstances("MCROSERVICE-PAYMENT");
        for (ServiceInstance s: instances) {
            log.info("当前服务的实例有"+s.getServiceId()+"\t"+s.getHost()+"\t"+s.getPort()+"\t"+s.getUri());
        }
        return this.discoveryClient;
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.serverPort = event.getWebServer().getPort();
    }

    public int getPort() {
        return this.serverPort;
    }

    //模拟业务接口延时3秒
    @GetMapping("/payment/feign/timeout")
    public int PaymentFeignTimeOut() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        return serverPort;
    }
}
