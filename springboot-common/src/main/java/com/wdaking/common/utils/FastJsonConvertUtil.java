package com.wdaking.common.utils;


import com.alibaba.fastjson.JSON;

/**
 * JSON 转换工具
 * @Version 1.0
 * @Author: wangjian
 * @Date: 2019-07-14 13:23
 */
public class FastJsonConvertUtil {

    public static <T> String convertObjectToJSON(T value) {
        if(value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz == int.class || clazz == Integer.class) {
            return ""+value;
        }else if(clazz == String.class) {
            return (String)value;
        }else if(clazz == long.class || clazz == Long.class) {
            return ""+value;
        }else {
            return JSON.toJSONString(value);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T convertJSONToObject(String message, Class<T> clazz){
        if(message == null || message.length() <= 0 || clazz == null) {
            return null;
        }
        if(clazz == int.class || clazz == Integer.class) {
            return (T)Integer.valueOf(message);
        }else if(clazz == String.class) {
            return (T)message;
        }else if(clazz == long.class || clazz == Long.class) {
            return  (T)Long.valueOf(message);
        }else {
            return JSON.toJavaObject(JSON.parseObject(message), clazz);
        }
    }
}
