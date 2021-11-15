package com.killb.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.killb.domain.SysPrivilege;
import com.killb.domain.SysRole;
import com.killb.domain.SysRolePrivilege;
import com.killb.domain.SysUserRole;
import com.killb.mapper.SysRolePrivilegeMapper;
import com.killb.model.R;
import com.killb.service.SysRolePrivilegeService;
import com.killb.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;

/**
 * @program: coin-exchangs
 * @description: 角色管理
 * @author: xiaozhang666
 * @create: 2021-08-04 16:13
 **/
@RestController
@RequestMapping("/roles")
@Api(tags = "角色管理")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRolePrivilegeService sysRolePrivilegeService;

    @GetMapping
    @ApiOperation(value = "条件分页查询")
    @PreAuthorize("hasAuthority('sys_role_query')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页显示的大小"),
            @ApiImplicitParam(name = "name", value = "角色名称"),
    })
    public R<Page<SysRole>> findByPage(@ApiIgnore Page<SysRole> page, String name) {
        page.addOrder(OrderItem.desc("last_update_time"));
        return R.ok(sysRoleService.findByPage(page, name));
    }


    @PostMapping
    @ApiOperation(value = "新增一个角色")
    @PreAuthorize("hasAuthority('sys_role_create')")
    public R add(@RequestBody @Validated SysRole sysRole) {
        boolean save = sysRoleService.save(sysRole);
        if (save) {
            return R.ok();
        }
        return R.fail("新增失败");
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除一个角色数据")
    @PreAuthorize("hasAuthority('sys_role_delete')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "要删除角色的id的集合")
    })
    public R delete(@RequestBody String[] ids) {
        if (ids == null || ids.length == 0) {
            return R.fail("要删除的数据不能为null");
        }
        boolean b = sysRoleService.removeByIds(Arrays.asList(ids));
        boolean remove = sysRolePrivilegeService.remove(new LambdaQueryWrapper<SysRolePrivilege>().in(SysRolePrivilege::getRoleId, Arrays.asList(ids)));
        if (b && remove) {
            return R.ok();
        }
        return R.fail("删除失败");
    }

}
