package com.killb.controller;

import com.killb.model.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: coin-exchangs
 * @description: 测试服务q
 * @author: xiaozhang666
 * @create: 2021-11-25 10:36
 **/
@RestController
@Api(tags = "会员系统接口的测试")
public class MemberTestController {

    @ApiOperation(value = "这是一个测试接口",authorizations = {@Authorization("Authorization")})
    @GetMapping("/member/test")
    public R<String> test(){
        return R.ok("测试成功");
    }

}
