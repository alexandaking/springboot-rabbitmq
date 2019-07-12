package com.wdaking.entity;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * 活动需要发送的消息实体
 * @Version 1.0
 * @Author: wangjian
 * @Date: 2019-07-12 17:08
 */
@Data
public class ActivityOverMsg implements Serializable {

    private static final long serialVersionUID = -2586011075570331300L;
    private long id;
    private String activityName;
    private Data activityOverTime;
    private String messageId;

}
