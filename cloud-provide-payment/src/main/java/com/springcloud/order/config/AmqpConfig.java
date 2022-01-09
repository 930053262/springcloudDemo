package com.springcloud.order.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * rabbit配置类（声明交换机、队列以及他们的绑定关系）
 *
 */
@Configuration
public class AmqpConfig {

    // 交换机名称(延时队列)
    public static final String DELAYED_EXCHANGE_KEY = "exchange.delayed";
    // 队列名称（成团状态更新）
    public static final String ORDER_GROUP_JOIN_QUEUE_KEY = "order.group.join.delayed";
    //队列路线/绑定关系（成团状态更新）
    public static final String ORDER_GROUP_JOIN_ROUTK = "order.group.join.delayed";

    /**
     * 延时队列交换器
     *
     */
    @Bean
    public CustomExchange testExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE_KEY, "x-delayed-message", true, false, args);
    }
    /**
     * 成团状态更新推送队列
     *
     */
    @Bean
    public Queue orderGroupJoinQueue() {
        return new Queue(ORDER_GROUP_JOIN_QUEUE_KEY, true);
    }

    @Bean
    public Binding flashSalePushBinding(CustomExchange delayedExchange, Queue orderGroupJoinQueue) {
        Binding binding=BindingBuilder.bind(orderGroupJoinQueue).to(delayedExchange).with(ORDER_GROUP_JOIN_ROUTK).noargs();
        return binding;
    }
}
