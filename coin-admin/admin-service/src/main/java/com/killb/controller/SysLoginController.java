package com.killb.controller;

import com.killb.model.LoginResult;
import com.killb.service.SysLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: coin-exchangs
 * @description: 登录相关
 * @author: xiaozhang666
 * @create: 2021-07-20 16:11
 **/
@RestController
@Api(tags = "登录接口")
public class SysLoginController {

    @Autowired
    private SysLoginService loginService;

    @PostMapping("/login")
    @ApiOperation(value = "后台管理人员登录")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "username", value = "用户名称"),
                    @ApiImplicitParam(name = "password", value = "用户的密码"),
            }
    )
    public LoginResult login(
            @RequestParam(required = true) String username, // 用户名称
            @RequestParam(required = true) String password  // 用户的密码
    ) {
        return loginService.login(username, password);
    }

}
