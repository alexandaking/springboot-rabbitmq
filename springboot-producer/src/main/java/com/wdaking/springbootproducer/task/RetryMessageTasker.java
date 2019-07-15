package com.wdaking.springbootproducer.task;

import com.alibaba.fastjson.JSONObject;
import com.wdaking.common.constants.Constants;
import com.wdaking.common.utils.FastJsonConvertUtil;
import com.wdaking.entity.ActivityOtMsg;
import com.wdaking.entity.BrokerMessageLog;
import com.wdaking.springbootproducer.mapper.BrokerMessageLogMapper;
import com.wdaking.springbootproducer.producer.ActivityOtMsgSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 定时任务，定时重新发送消息
 * @Version 1.0
 * @Author: wangjian
 * @Date: 2019-07-15 10:39
 */
@Component
public class RetryMessageTasker {
    private static final Logger logger = LoggerFactory.getLogger(RetryMessageTasker.class);
    private static final int MAX_RETRY = 3;

    @Autowired
    private ActivityOtMsgSender activityOtMsgSender;
    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    @Scheduled(initialDelay = 5000, fixedDelay = 10000)
    public void reSend(){
        List<BrokerMessageLog> brokerMessageLogList = brokerMessageLogMapper.query4StatusAndTimeoutMessage();
        brokerMessageLogList.forEach(brokerMessageLog -> {
            if (brokerMessageLog.getTryCount() >= MAX_RETRY){
                // update fail message
                brokerMessageLogMapper.changeBrokerMessageLogStatus(brokerMessageLog.getMessageId(),
                        Constants.ORDER_SEND_FAILURE,
                        new Date());
            }else {
                //resend
                brokerMessageLogMapper.update4ReSend(brokerMessageLog.getMessageId(), new Date());
                ActivityOtMsg activityOtMsg = FastJsonConvertUtil.convertJSONToObject(brokerMessageLog.getMessage(),
                        ActivityOtMsg.class);
                try {
                    activityOtMsgSender.sendActivityOtMsg(activityOtMsg, 0L);
                } catch (Exception e) {
                    logger.error("activityOtMsg send message fail, error info:[{}], activity ot message:[{}], timestamp:[{}]",
                            e,
                            JSONObject.toJSONString(activityOtMsg),
                            new Date());
                }
            }
        });
    }
}
