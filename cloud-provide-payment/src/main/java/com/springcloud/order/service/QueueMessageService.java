package com.springcloud.order.service;

public interface QueueMessageService {

    /**
     * 发送正常队列消息
     *
     */
    void send(String exchangeKey, String routingKey, Object message);


    /**
     * 发送延时队列消息
     *
     */
    void delayedSend(String exchangeKey, String routingKey, Object message, int msec);
}
