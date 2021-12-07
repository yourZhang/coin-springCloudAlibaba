package com.killb.config;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;

import java.io.IOException;

/**
 * @program: coin-exchangs
 * @description: 远程异常处理
 * @author: xiaozhang666
 * @create: 2021-12-01 09:58
 **/
//public class FeignErrorDecoder implements ErrorDecoder {
//    @Override
//    public Exception decode(String methodKey, Response response) {
//        try {
//            // 这里直接拿到我们抛出的异常信息
//            String message = Util.toString(response.body().asReader());
//            try {
//                JSONObject jsonObject = JSONObject.parseObject(message);
//                return new ApiException("");
//            } catch (ApiException e) {
//                e.printStackTrace();
//            }
//        } catch (IOException ignored) {
//        }
//        return decode(methodKey, response);
//    }
//}
