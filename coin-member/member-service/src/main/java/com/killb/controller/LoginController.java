package com.killb.controller;

import com.killb.model.LoginForm;
import com.killb.model.LoginUser;
import com.killb.model.R;
import com.killb.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: coin-exchangs
 * @description: 登录接口
 * @author: xiaozhang666
 * @create: 2021-11-29 15:08
 **/
@RestController
@Api(tags = "登录的控制器")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    @ApiOperation(value = "会员的登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginForm", value = "登录的表单参数")
    })
    public R<LoginUser> login(@RequestBody @Validated LoginForm loginForm) {
        LoginUser loginUser = loginService.login(loginForm);
        return R.ok(loginUser);
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println("e10adc3949ba59abbe56e057f20f883e".equals("e10adc3949ba59abbe56e057f20f883e"));
        String password = bCryptPasswordEncoder.encode("e10adc3949ba59abbe56e057f20f883e");
        System.out.println(password);
    }

}