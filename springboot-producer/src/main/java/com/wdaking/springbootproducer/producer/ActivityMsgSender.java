package com.wdaking.springbootproducer.producer;

import com.alibaba.fastjson.JSONObject;
import com.wdaking.common.constants.RabbitConstants;
import com.wdaking.entity.ActivityOverMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Version 1.0
 * @Author: wangjian
 * @Date: 2019-07-12 17:58
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@Component
public class ActivityMsgSender {

    private final static Logger logger = LoggerFactory.getLogger(ActivityMsgSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //todo ： 重写confirnCallback方法
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            logger.info("ConfirmCallback get correlationData : [{}]", JSONObject.toJSONString(correlationData));
            String messageId = correlationData.getId();
            if (ack){
                //todo: 修改数据库中消息的状态
            }else {
                logger.error("msg confirm error, error reason : {}", cause);
            }
        }
    };


    public void sendOrder(ActivityOverMsg activityOverMsg) throws Exception{

        rabbitTemplate.setConfirmCallback(confirmCallback);

        // 消息唯一ID
        CorrelationData correlationData = new CorrelationData(activityOverMsg.getMessageId());
        rabbitTemplate.convertAndSend(RabbitConstants.ACTIVITY_EXCHANGE,
                String.format(RabbitConstants.ROUTING_KEY, ""),
                activityOverMsg, correlationData);
    }
}
