package com.wdaking.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * 消息记录的日志
 * @Version 1.0
 * @Author: wangjian
 * @Date: 2019-07-12 17:11
 */
@Data
public class BrokerMessageLog implements Serializable {

    private static final long serialVersionUID = -5511971866490756675L;

    private String messageId;

    private String message;

    private Integer tryCount;

    private String status;

    private Date nextRetry;

    private Date createTime;

    private Date updateTime;
}
