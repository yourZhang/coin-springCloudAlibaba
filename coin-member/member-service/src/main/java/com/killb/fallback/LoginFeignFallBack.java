package com.killb.fallback;

import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.killb.feign.JwtToken;
import com.killb.feign.OAuth2FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * @program: coin-exchangs
 * @description: 登录兜底方法
 * @author: xiaozhang666
 * @create: 2021-12-01 10:13
 **/
@Component
public class LoginFeignFallBack implements OAuth2FeignClient {
    @Override
    public ResponseEntity<JwtToken> getToken(String grantType, String username, String password,
                                             String loginType, String basicToken) {

        return new ResponseEntity<JwtToken>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
