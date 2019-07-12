package com.wdaking.springbootconsumer.consumer;

import com.rabbitmq.client.Channel;
import com.wdaking.common.constants.RabbitConstants;
import com.wdaking.entity.ActivityOverMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * 活动消息消费者
 * @Version 1.0
 * @Author: wangjian
 * @Date: 2019-07-12 17:23
 */
@Component
public class ActivityMsgReceiver {
    
    private final static Logger logger = LoggerFactory.getLogger(ActivityMsgReceiver.class);

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = RabbitConstants.ACTIVITY_QUEUE, durable = "true"),
            exchange = @Exchange(name = RabbitConstants.ACTIVITY_EXCHANGE, durable = "true", type = "topic"),
            key = "activity.*"
        )
    )
    @RabbitHandler
    public void onActivtyMessage(@Payload ActivityOverMsg activityOverMsg, 
                                 @Headers Map<String, Object> headers, 
                                 Channel channel) throws IOException {
        logger.info("Receive Message, Start Processing, activityId:[{}], messageId:[{}]", activityOverMsg.getId(), 
                activityOverMsg.getMessageId());

        /**
         * Delivery Tag 用来标识信道中投递的消息。RabbitMQ 推送消息给 Consumer 时，会附带一个 Delivery Tag，
         * 以便 Consumer 可以在消息确认时告诉 RabbitMQ 到底是哪条消息被确认了。
         * RabbitMQ 保证在每个信道中，每条消息的 Delivery Tag 从 1 开始递增。
         */
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);

        /**
         *  multiple 取值为 false 时，表示通知 RabbitMQ 当前消息被确认
         *  如果为 true，则额外将比第一个参数指定的 delivery tag 小的消息一并确认
         */
        //ACK,确认一条消息已经被消费
        channel.basicAck(deliveryTag,false);
        
    }
}
