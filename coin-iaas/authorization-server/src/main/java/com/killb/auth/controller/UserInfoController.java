package com.killb.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @program: coin-exchangs
 * @description: 使用token申请user信息
 * @author: xiaozhang666
 * @create: 2021-06-29 16:48
 **/
@RestController
public class UserInfoController {

    /**
     * 功能描述: <br>
     * 〈返回user对象〉
     *
     * @Param: [principal]
     * @return: java.security.Principal
     * @Author: xiaozhang666
     * @Date: 2021/6/29 16:49
     */
    @GetMapping("/user/info")
    public Principal usrInfo(Principal principal) { // 此处的principal 由OAuth2.0 框架自动注入
        // 原理就是：利用Context概念，将授权用户放在线程里面，利用ThreadLocal来获取当前的用户对象
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return principal;
    }


    @GetMapping("/test/jdbc")
    public String usrInfo() {
        return null;
    }

}
