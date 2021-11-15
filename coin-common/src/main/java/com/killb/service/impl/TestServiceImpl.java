package com.killb.service.impl;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.killb.model.WebLog;
import com.killb.service.TestService;
import org.springframework.stereotype.Service;

/**
 * @program: coin-exchangs
 * @description:
 * @author: xiaozhang666
 * @create: 2021-07-13 16:16
 **/
@Service
public class TestServiceImpl implements TestService {


    /**
     * 功能描述: <br>
     * 〈通过用户name查询webLob〉
     *
     * @Param: [username]
     * @return: com.killb.model.WebLog
     * @Author: xiaozhang666
     * @Date: 2021/7/13 16:17
     */
    @Override
    @Cached(name = "com.killb.service.impl.TestServiceImpl", key = "#username", cacheType = CacheType.BOTH)
    public WebLog get(String username) {
        WebLog webLog = new WebLog();
        webLog.setUsername(username);
        webLog.setResult("ok");
        return webLog;
    }
}
