package com.killb.service;

import com.killb.model.WebLog;

/**
 * @program: coin-exchangs
 * @description: 测试接口
 * @author: xiaozhang666
 * @create: 2021-07-13 16:15
 **/
public interface TestService {
    /*
    * 通过username 查询 webLog
    * */
    WebLog get(String username);
}
