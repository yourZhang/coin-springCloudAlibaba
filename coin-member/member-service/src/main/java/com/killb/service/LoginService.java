package com.killb.service;

import com.killb.model.LoginForm;
import com.killb.model.LoginUser;

/**
 * @program: coin-exchangs
 * @description: 登录接口
 * @author: xiaozhang666
 * @create: 2021-11-29 15:11
 **/
public interface LoginService {
    /**
     * 会员的登录
     * @param loginForm
     * 登录的表单参数
     * @return
     * 登录的结果
     */
    LoginUser login(LoginForm loginForm);

}
