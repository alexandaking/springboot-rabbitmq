package com.wdaking.springbootproducer.service;

import com.alibaba.fastjson.JSONObject;
import com.wdaking.common.constants.Constants;
import com.wdaking.common.utils.FastJsonConvertUtil;
import com.wdaking.entity.ActivityOtMsg;
import com.wdaking.entity.BrokerMessageLog;
import com.wdaking.springbootproducer.mapper.ActivityOtMsgMapper;
import com.wdaking.springbootproducer.mapper.BrokerMessageLogMapper;
import com.wdaking.springbootproducer.producer.ActivityOtMsgSender;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Version 1.0
 * @Author: wangjian
 * @Date: 2019-07-14 13:07
 */
@Service
public class ActivityOtMsgService {

    private static final Logger logger = LoggerFactory.getLogger(ActivityOtMsgService.class);

    @Autowired
    private ActivityOtMsgMapper activityOtMsgMapper;
    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;
    @Autowired
    private ActivityOtMsgSender activityOtMsgSender;

    @Transactional(rollbackFor = Exception.class)
    public void creatActivityOtMsg(ActivityOtMsg activityOtMsg) {
        // 插入业务数据
        activityOtMsgMapper.insert(activityOtMsg);
        // 插入消息记录表数据
        BrokerMessageLog brokerMessageLog = new BrokerMessageLog();
        // 消息唯一ID
        brokerMessageLog.setMessageId(activityOtMsg.getMessageId());
        // 保存消息整体 转为JSON 格式存储入库
        brokerMessageLog.setMessage(FastJsonConvertUtil.convertObjectToJSON(activityOtMsg));
        // 设置消息状态为0 表示发送中
        brokerMessageLog.setStatus("0");
        // 设置消息未确认超时时间窗口为 一分钟 （当前时间加一分钟）
        brokerMessageLog.setNextRetry(DateUtils.addMinutes(new Date(), Constants.ORDER_TIMEOUT));
        brokerMessageLog.setCreateTime(new Date());
        brokerMessageLog.setUpdateTime(new Date());
        brokerMessageLogMapper.insertSelective(brokerMessageLog);
        try {
            sendMsg(activityOtMsg, 10000L);
        } catch (Exception e) {
            logger.error("activityOtMsg send message fail, error info:[{}], activity ot message:[{}], timestamp:[{}]",
                    e,
                    JSONObject.toJSONString(activityOtMsg),
                    new Date());
        }
    }

    private void sendMsg(ActivityOtMsg activityOtMsg, long ttl) throws Exception {
        activityOtMsgSender.sendActivityOtMsg(activityOtMsg, ttl);
    }
}
