package com.killb.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.killb.service.ConfigService;
import com.killb.model.Config;
import com.killb.model.R;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * @program: coin-exchangs
 * @description: 配置参数管理
 * @author: xiaozhang666
 * @create: 2021-11-22 16:42
 **/
@RestController
@RequestMapping("/configs")
@Api(tags = "后台参数配置")
public class ConfigController {

    @Resource(type = ConfigService.class)
    private ConfigService configService;


    @GetMapping
    @ApiOperation(value = "条件分页查询后台参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "后台参数类型"),
            @ApiImplicitParam(name = "code", value = "后台参数code"),
            @ApiImplicitParam(name = "name", value = "后台参数名称"),
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数"),
    })
    @PreAuthorize("hasAuthority('config_query')")
    public R<Page<Config>> findByPage(@ApiIgnore Page<Config> page, String type, String code, String name) {
        Page<Config> configPage = configService.findByPage(page, type, name, code);
        return R.ok(configPage);
    }


    @PostMapping
    @ApiOperation(value = "新增一个参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "config",value = "config 的json数据")
    })
    @PreAuthorize("hasAuthority('config_create')")
    public R add(@RequestBody @Validated Config config){
        boolean save = configService.save(config);
        if(save){
            return R.ok() ;
        }
        return R.fail("新增失败") ;
    }


    @PatchMapping
    @ApiOperation(value = "修改一个参数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "config",value = "config 的json数据")
    })
    @PreAuthorize("hasAuthority('config_update')")
    public R update(@RequestBody @Validated  Config config){
        boolean update = configService.updateById(config);
        if(update){
            return R.ok() ;
        }
        return R.fail("修改失败") ;
    }

}
