package com.xuecheng.test.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    /**队列*/
    public static final String QUEUE_INFORM_EMAI = "queue_inform_email";
    public static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    public static final String EXCHANGE_TOPICS_INFORM = "exchange_topics_inform";
    public static final String ROUTING_EMAIL = "inform.#.email.#";
    public static final String ROUTING_SMS = "inform.#.sms.#";

    /**
     * 声明交换机
     * @return
     */
    @Bean(EXCHANGE_TOPICS_INFORM)
    public Exchange EXCHANGE_TOPICS_INFORM(){
        //durable(true) 持久化，mq重启后交换机还在
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_INFORM).durable(true).build();
    }
    /**
     * 声明email队列
     */
    @Bean(QUEUE_INFORM_EMAI)
    public Queue QUEUE_INFORM_EMAI(){
        return new Queue(QUEUE_INFORM_EMAI);
    }

    /**
     * 声明sms队列
     */
    @Bean(QUEUE_INFORM_SMS)
    public Queue QUEUE_INFORM_SMS(){
        return new Queue(QUEUE_INFORM_SMS);
    }

    /**
     * 绑定交换机和email队列
     */
    @Bean
    public Binding BINDING_QUEUE_INFORM_EMAIL(@Qualifier(QUEUE_INFORM_EMAI)Queue queue,
                                              @Qualifier(EXCHANGE_TOPICS_INFORM)Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_EMAIL).noargs();
    }

    /**
     * 绑定交换机和sms队列
     */
    @Bean
    public Binding BINDING_QUEUE_INFORM_SMS(@Qualifier(QUEUE_INFORM_SMS)Queue queue,
                                              @Qualifier(EXCHANGE_TOPICS_INFORM)Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_SMS).noargs();
    }
}
