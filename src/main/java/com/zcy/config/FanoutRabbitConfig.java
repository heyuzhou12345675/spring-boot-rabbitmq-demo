package com.zcy.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author zhouchunyang
 * @Date: Created in 14:33 2021/9/15
 * @Description:
 */
@Configuration
public class FanoutRabbitConfig {
    //注册队列
    @Bean
    public Queue queueA(){
        return new Queue("fanout.A");
    }

    @Bean
    public Queue queueB(){
        return new Queue("fanout.B");
    }

    @Bean
    public Queue queueC(){
        return new Queue("fanout.C");
    }

    //注册扇形交换机
    @Bean
    FanoutExchange  fanoutExchange(){
        return new FanoutExchange("fanoutExchange");
    }

    //将队列绑定至交换机  扇形交换机无须配置路由key
    @Bean
    Binding bindingExchangeA(){
        return BindingBuilder.bind(queueA()).to(fanoutExchange());
    }


    @Bean
    Binding bindingExchangeB() {
        return BindingBuilder.bind(queueB()).to(fanoutExchange());
    }

    @Bean
    Binding bindingExchangeC() {
        return BindingBuilder.bind(queueC()).to(fanoutExchange());
    }
}
