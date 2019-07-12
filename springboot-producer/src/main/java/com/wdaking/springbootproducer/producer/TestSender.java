package com.wdaking.springbootproducer.producer;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Version 1.0
 * @Author: wangjian
 * @Date: 2019-07-11 19:48
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
public class TestSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String msg){
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(msg);
        rabbitTemplate.convertAndSend("first-exchange", "first.abcd", msg, correlationData);
    }
}
