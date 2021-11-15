package com.killb.service.impl;

import com.killb.service.SysMenuService;
import com.killb.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.killb.domain.SysMenu;
import com.killb.mapper.SysMenuMapper;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {


    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysMenuMapper sysMenuMapper;

    /**
     * 功能描述: <br>
     * 〈通过用户id查询菜单〉
     *
     * @Param: [userId]
     * @return: java.util.List<com.killb.domain.SysMenu>
     * @Author: xiaozhang666
     * @Date: 2021/7/20 16:50
     */
    @Override
    public List<SysMenu> getMenusByUserId(Long userId) {
        // 1 如果该用户是超级管理员->>拥有所有的菜单
        if (sysRoleService.isSuperAdmin(userId)) {
            return list(); // 查询所有
        }
        // 2 如果该用户不是超级管理员->>查询角色->查询菜单
        return sysMenuMapper.selectMenusByUserId(userId);
    }
}
