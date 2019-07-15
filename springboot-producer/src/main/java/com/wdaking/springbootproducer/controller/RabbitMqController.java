package com.wdaking.springbootproducer.controller;

import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Version 1.0
 * @Author: wangjian
 * @Date: 2019-07-11 20:11
 * Copyright 本内容仅限于杭州阿拉丁信息科技股份有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestController
@EnableAspectJAutoProxy(proxyTargetClass = true)
@RequestMapping(value = "/wdaking/inner/rabbit/")
public class RabbitMqController {

}
