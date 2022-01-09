package com.springcloud.order.service.impl;

import com.springcloud.order.service.IRedis;
import com.springcloud.order.service.QueueMessageService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class QueueMessageServiceImpl implements QueueMessageService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IRedis iRedis;

    @Override
    public void send(String exchangeKey, String routingKey, Object message) {
        CorrelationData correlationData = new CorrelationData(iRedis.getCaseNumber("NO"));
        rabbitTemplate.convertAndSend(exchangeKey, routingKey, message, correlationData);
    }


    @Override
    public void delayedSend(String exchangeKey, String routingKey, Object msg,final int xdelay) {

        rabbitTemplate.convertAndSend(exchangeKey, routingKey, msg, message -> {
            // 设置延迟时间
            message.getMessageProperties().setDelay(xdelay);
            return message;
        });
    }
}
