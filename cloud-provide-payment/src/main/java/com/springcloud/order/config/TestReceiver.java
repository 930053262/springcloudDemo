package com.springcloud.order.config;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/**
 * 监听队列消息
 *
 */
@Slf4j
@Component
@EnableRabbit
public class TestReceiver {


    @RabbitListener(queues = AmqpConfig.ORDER_GROUP_JOIN_QUEUE_KEY)
    public void process(String msg, Channel channel, Message message) {
        log.info("======================延时队列开始执行。。。。。。。。。。。。。。");
        log.info(new Date().toString() + ",延时收到了信息 message = " + msg);
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            Map p = JSON.parseObject(msg, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
