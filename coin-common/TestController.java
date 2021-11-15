package com.killb.controller;

import com.killb.model.R;
import com.killb.model.WebLog;
import com.killb.service.impl.TestServiceImpl;
import io.swagger.annotations.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @program: coin-exchangs
 * @description: 测试请求
 * @author: xiaozhang666
 * @create: 2021-07-09 15:49
 **/
@RestController
@Api(tags = "CoinCommon里面测试的接口")
public class TestController {

//    @Autowired
//    private ObjectMapper objectMapper ;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/common/test")
    @ApiOperation(value = "测试方法", authorizations = {@Authorization("Authorization")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "param", value = "参数1", dataType = "String", paramType = "query", example = "paramValue"),
            @ApiImplicitParam(name = "param1", value = "参数2", dataType = "String", paramType = "query", example = "paramValue")
    })
    public R<String> testMethod(String param, String param1) {
        return R.ok("ok");
    }

    /**
     * 功能描述: <br>
     * 〈测试jackson格式化时间〉
     *
     * @Param: []
     * @return: com.killb.model.R<java.util.Date>
     * @Author: xiaozhang666
     * @Date: 2021/7/13 15:56
     */
    @GetMapping("date/test")
    @ApiOperation(value = "日期格式化测试", authorizations = {@Authorization("Authorization")})
    public R<Date> testDate() {
        return R.ok(new Date());
    }


    @GetMapping("redis/test")
    @ApiOperation(value = "redis测试", authorizations = {@Authorization("Authorization")})
    public R<Date> redisTest() {
        WebLog webLog = new WebLog();
        webLog.setResult("ok");
        webLog.setMethod("*.redisTest()");
        webLog.setUsername("1101");
        redisTemplate.opsForValue().set("com.killb.domain.WebLog", webLog);
        return R.ok(new Date());
    }

    /**
     * 功能描述: <br>
     * 〈测试 jetCache〉
     *
     * @Param: [username]
     * @return: com.killb.model.R<java.lang.String>
     * @Author: xiaozhang666
     * @Date: 2021/7/13 16:19
     */
    @Autowired
    private TestServiceImpl testService;

    @GetMapping("/jetcache/test")
    @ApiOperation(value = "jetCache缓存的测试", authorizations = {@Authorization("Authorization")})
    public R<String> testJetCache(String username) {
        WebLog webLog = testService.get(username);
        System.out.println(webLog);
        return R.ok("OK");
    }

}
