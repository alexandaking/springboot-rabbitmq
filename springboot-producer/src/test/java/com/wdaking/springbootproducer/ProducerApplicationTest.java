package com.wdaking.springbootproducer;

import com.wdaking.springbootproducer.producer.TestSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Version 1.0
 * @Author: wangjian
 * @Date: 2019-07-12 15:21
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProducerApplicationTest {

    @Autowired
    private TestSender sender;

    @Test
    public void testSender(){
        sender.send("第一个消息");
    }
}
