package com.wdaking.springbootproducer;

import com.wdaking.entity.ActivityOtMsg;
import com.wdaking.springbootproducer.service.ActivityOtMsgService;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.UUID;

/**
 * @Version 1.0
 * @Author: wangjian
 * @Date: 2019-07-12 15:21
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProducerApplicationTest {

    @Autowired
    private ActivityOtMsgService activityOtMsgService;

    @Test
    public void testSender() throws Exception {
        ActivityOtMsg activityOtMsg = new ActivityOtMsg();
        activityOtMsg.setId(1);
        activityOtMsg.setActivityName("第一个消息");
        activityOtMsg.setActivityOverTime(DateUtils.addMinutes(new Date(), 2));
        activityOtMsg.setMessageId(System.currentTimeMillis()+"$"+ UUID.randomUUID().toString());
        activityOtMsgService.creatActivityOtMsg(activityOtMsg);
    }
}
