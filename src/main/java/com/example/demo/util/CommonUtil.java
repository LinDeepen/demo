package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 共用逻辑工具类
 */
@Component
public class CommonUtil {

    @Autowired
    RedisUtil redisUtil;

    /**
     * 11位手机号脱敏中间四位处理
     *
     * @param phone 手机号
     * @param cache 是否需要缓存原手机号
     * @param key   缓存的key (拼接规则：当前数据id + “—” + 当前数据要隐藏的数据字段名称 )
     */
    public String encryptPhone(String phone, boolean cache, String key) {
        if (cache) {
            redisUtil.setCacheObject("private:info:" + key, phone, 12 * 60 * 60);
        }
        if (phone != null && phone.length() == 11) {
            return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
        }
        return phone;
    }

    /**
     * 身份证号脱敏处理
     *
     * @param idCard 身份证号
     * @param cache  是否需要缓存原身份证号
     * @param key    缓存的key (拼接规则：当前数据id + “—” + 当前数据要隐藏的数据字段名称 )
     */
    public String encryptIDCard(String idCard, boolean cache, String key) {
        if (cache) {
            redisUtil.setCacheObject("private:info:" + key, idCard, 12 * 60 * 60);
        }
        if (idCard != null && idCard.length() == 18) {
            return idCard.substring(0, 4) + "**********" + idCard.substring(idCard.length() - 4);
        }
        return idCard;
    }

    //功能测试
    public static void main(String[] args) {
        String phone = "13900004239";
        String idCard = "440582000000007033";
        CommonUtil commonUtil = new CommonUtil();
        String enPhone = commonUtil.encryptPhone(phone, false, "1" + phone);
        String enIdCard = commonUtil.encryptIDCard(idCard, false, "1" + idCard);

        System.out.println("脱敏后的手机号：" + enPhone);
        System.out.println("脱敏后的身份证号：" + enIdCard);
    }
}
