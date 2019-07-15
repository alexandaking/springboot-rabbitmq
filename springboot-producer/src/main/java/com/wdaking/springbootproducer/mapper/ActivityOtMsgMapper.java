package com.wdaking.springbootproducer.mapper;

import com.wdaking.entity.ActivityOtMsg;

/**
 * @Version 1.0
 * @Author: wangjian
 * @Date: 2019-07-14 11:44
 */
public interface ActivityOtMsgMapper {

    int insert(ActivityOtMsg activityOtMsg);
    int deleteByPrimaryKey(Integer id);
    int insertSelective(ActivityOtMsg activityOtMsg);
    ActivityOtMsg selectByPrimaryKey(Integer id);
    int updateByPrimaryKeySelective(ActivityOtMsg activityOtMsg);
    int updateByPrimaryKey(ActivityOtMsg activityOtMsg);
}
