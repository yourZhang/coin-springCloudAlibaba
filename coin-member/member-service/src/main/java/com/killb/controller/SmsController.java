package com.killb.controller;

import com.killb.domain.Sms;
import com.killb.model.R;
import com.killb.service.SmsService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;


    @PostMapping("/sendTo")
    @ApiOperation(value = "发送短信")
    @ApiImplicitParams({
            @ApiImplicitParam(name="sms",value = "smsjson数据")
    })
    public R sendSms(@RequestBody @Validated Sms sms){
        boolean isOk = smsService.sendSms(sms) ;
        if(isOk){
            return R.ok() ;
        }
        return R.fail("发送失败") ;
    }
}
