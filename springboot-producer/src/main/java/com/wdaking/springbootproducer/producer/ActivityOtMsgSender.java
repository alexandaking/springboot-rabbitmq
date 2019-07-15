package com.wdaking.springbootproducer.producer;

import com.alibaba.fastjson.JSONObject;
import com.wdaking.common.constants.Constants;
import com.wdaking.common.constants.RabbitConstants;
import com.wdaking.entity.ActivityOtMsg;
import com.wdaking.springbootproducer.mapper.BrokerMessageLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Version 1.0
 * @Author: wangjian
 * @Date: 2019-07-12 17:58
 */
@Component
public class ActivityOtMsgSender {

    private final static Logger logger = LoggerFactory.getLogger(ActivityOtMsgSender.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            logger.info("ConfirmCallback get correlationData : [{}]", JSONObject.toJSONString(correlationData));
            String messageId = correlationData.getId();
            if (ack){
                //如果confirm返回成功 则进行更新
                brokerMessageLogMapper.changeBrokerMessageLogStatus(messageId, Constants.ORDER_SEND_SUCCESS, new Date());
            }else {
                logger.error("msg confirm error, error reason : {}", cause);
            }
        }
    };

    /**
     * 发送活动消息对象实体类
     * @param activityOtMsg @see ActivityOtMsg
     * @throws Exception e
     */
    public void sendActivityOtMsg(ActivityOtMsg activityOtMsg, long ttl) throws Exception{

        rabbitTemplate.setConfirmCallback(confirmCallback);

        // 消息唯一ID
        CorrelationData correlationData = new CorrelationData(activityOtMsg.getMessageId());
        rabbitTemplate.convertAndSend(RabbitConstants.ACTIVITY_EXCHANGE,
                String.format(RabbitConstants.ROUTING_KEY, "normal"),
                activityOtMsg,message -> {
            message.getMessageProperties().setHeader("x-delay", ttl);
            return message;
            }, correlationData);
    }
}
