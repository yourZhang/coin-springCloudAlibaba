package com.killb.controller;

import com.killb.domain.SysUser;

import com.killb.model.R;

import com.killb.service.SysUserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: coin-exchangs
 * @description: 测试接口
 * @author: xiaozhang666
 * @create: 2021-07-16 16:14
 **/
@RestController
@Api(tags = "admin-service-test接口")
public class TestController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/user/info/{id}")
    @ApiOperation(value = "使用用户的id查询用户", authorizations = {@Authorization("Authorization")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户的id值", paramType = "path", type = "Long")
    })
    public R<SysUser> sysUser(@PathVariable("id") Long id) {
        SysUser sysUser = sysUserService.getById(id);
        return R.ok(sysUser);
    }

}
