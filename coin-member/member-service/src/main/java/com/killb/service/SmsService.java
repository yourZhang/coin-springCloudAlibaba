package com.killb.service;

import com.killb.domain.Sms;

/**
 * @program: coin-exchangs
 * @description: 短信发送服务
 * @author: xiaozhang666
 * @create: 2021-12-03 09:50
 **/
public interface SmsService {

    /**
     * 短信的发现
     * @param sms
     *  短信
     * @return
     * 是否发送成功
     */
    boolean sendSms(Sms sms);



}
