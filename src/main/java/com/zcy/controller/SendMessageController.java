package com.zcy.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author zhouchunyang
 * @Date: Created in 9:39 2021/9/15
 * @Description:
 */

@RestController
@RequestMapping("/send")
public class SendMessageController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/sendDirectMessage")
    public String sendDirectMessage(String messageContext){
        String messageId = String.valueOf(UUID.randomUUID());
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map = new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageContext",messageContext);
        map.put("createTime",createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend("TestDirectExchange", "TestDirectRouting", map);
        return null;
    }

    @RequestMapping("/sendTopicMessage1")
    public String sendTopicMessage1(String messageContext) {
        String messageId = String.valueOf(UUID.randomUUID());
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map = new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageContext",messageContext);
        map.put("createTime",createTime);
        //主题交换机，两个队列分别为topic.man/topic.woman，
        // topi.man队列路由规则为topic.man，top.woman队列路由规则为topic.*
        //所以此方法发送的消息两个队列都可以收到
        rabbitTemplate.convertAndSend("topicExchange", "topic.man", map);
        return null;
    }

    @RequestMapping("/sendTopicMessage2")
    public String sendTopicMessage2(String messageContext) {
        String messageId = String.valueOf(UUID.randomUUID());
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map = new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageContext",messageContext);
        map.put("createTime",createTime);
        //但此方法的发送消息就只能topic.woman队列能收到
        rabbitTemplate.convertAndSend("topicExchange", "topic.woman", map);
        return null;
    }

    @RequestMapping("/sendFanoutMessage")
    public String sendFanoutMessage(String messageContext){
        String messageId = String.valueOf(UUID.randomUUID());
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map = new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageContext",messageContext);
        map.put("createTime",createTime);
        rabbitTemplate.convertAndSend("fanoutExchange",null,map);
        return null;
    }

    /**
     * 通过RabbitConfig可以看到写了两个回调函数，一个叫 ConfirmCallback ，一个叫 RetrunCallback；
     * 那么以上这两种回调函数都是在什么情况会触发呢？
     * 先从总体的情况分析，推送消息存在四种情况：
     * ①消息推送到server，但是在server里找不到交换机  ConfirmCallback
     * ②消息推送到server，找到交换机了，但是没找到队列 ConfirmCallback、ReturnCallback
     * ③消息推送到sever，交换机和队列啥都没找到 ConfirmCallback
     * ④消息推送成功 ConfirmCallback
     * */
    @RequestMapping("/testMessageAck")
    public String testMessageAck(String messageContext){
        String messageId = String.valueOf(UUID.randomUUID());
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map = new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageContext",messageContext);
        map.put("createTime",createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend("not-exit-exchange", "TestDirectRouting", map);
        return null;
    }

    @RequestMapping("/testMessageAck2")
    public String testMessageAck2(String messageContext){
        String messageId = String.valueOf(UUID.randomUUID());
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map = new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageContext",messageContext);
        map.put("createTime",createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
        rabbitTemplate.convertAndSend("lonelyDirectExchange", "TestDirectRouting", map);
        return null;
    }
}
