package com.killb.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.killb.domain.SysUserLog;
import com.killb.model.R;
import com.killb.service.SysUserLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @program: coin-exchangs
 * @description: 系统日志
 * @author: xiaozhang666
 * @create: 2021-11-15 10:30
 **/
@RestController
@Api(tags = "用户的操作记录查询")
@RequestMapping("/sysUserLog")
public class SysUserLogController {


    @Autowired
    private SysUserLogService sysUserLogService ;

    @GetMapping
    @ApiOperation(value = "分页查询用户的操作记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page" ,value = "分页查询")
    })
    public R<Page<SysUserLog>> findByPage(@ApiIgnore Page<SysUserLog> page){
        page.addOrder(OrderItem.desc("created")) ;
        return R.ok(sysUserLogService.page(page)) ;
    }


}
