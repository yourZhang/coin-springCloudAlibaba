package com.killb.service.impl;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.killb.domain.SysMenu;
import com.killb.domain.SysPrivilege;
import com.killb.domain.SysRoleMenu;
import com.killb.model.RolePrivilegesParam;
import com.killb.service.SysMenuService;
import com.killb.service.SysPrivilegeService;
import com.killb.service.SysRoleMenuService;
import com.killb.service.SysRolePrivilegeService;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.killb.mapper.SysRolePrivilegeMapper;
import com.killb.domain.SysRolePrivilege;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysRolePrivilegeServiceImpl extends ServiceImpl<SysRolePrivilegeMapper, SysRolePrivilege> implements SysRolePrivilegeService {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysPrivilegeService sysPrivilegeService;

    @Autowired
    private SysRolePrivilegeService sysRolePrivilegeService;

    /**
     * 查询角色的权限
     *
     * @param roleId
     * @return
     */
    @Override
    public List<SysMenu> findSysMenuAndPrivileges(Long roleId) {
        List<SysMenu> list = sysMenuService.list(); // 查询所有的菜单
        // 我们在页面显示的是二级菜单,以及二级菜单包含的权限
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<SysMenu> rootMenus = list.stream() //todo  没有父级id的都是父级菜单,此处拿到所有FijiMenu
                .filter(sysMenu -> sysMenu.getParentId() == null)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(rootMenus)) {
            return Collections.emptyList();
        }
        // 查询所有的二级菜单
        List<SysMenu> subMenus = new ArrayList<>();
        for (SysMenu rootMenu : rootMenus) {
            //todo 遍历所有fuji 装填二级和对应权限
            subMenus.addAll(getChildMenus(rootMenu.getId(), roleId, list));
        }
        return subMenus;
    }

    /**
     * 查询菜单的子菜单 (递归)
     *
     * @param parentId 父菜单的ID
     * @param roleId   当前查询的角色的ID
     * @return
     */
    private List<SysMenu> getChildMenus(Long parentId, Long roleId, List<SysMenu> sources) {
        List<SysMenu> childs = new ArrayList<>();
        for (SysMenu source : sources) {
            if (source.getParentId() == parentId) { // 找儿子
                childs.add(source);
                source.setChilds(getChildMenus(source.getId(), roleId, sources)); // 给该儿子设置儿子
                //todo 拿到菜单对应的权限
                List<SysPrivilege> sysPrivileges = sysPrivilegeService.getAllSysPrivilege(source.getId(), roleId);
                source.setPrivileges(sysPrivileges); // 该儿子可能包含权限
            }
        }
        return childs;
    }


    /**
     * 给角色授权权限
     *
     * @param rolePrivilegesParam
     * @return
     */
    @Autowired
    private SysRoleMenuService sysRoleMenuService;


    @Transactional
    @Override
    public boolean grantPrivileges(RolePrivilegesParam rolePrivilegesParam) {
        Long roleId = rolePrivilegesParam.getRoleId(); // 角色Id
        //1 先删除之前该角色的权限
        sysRolePrivilegeService.remove(new LambdaQueryWrapper<SysRolePrivilege>().eq(SysRolePrivilege::getRoleId, roleId));
        //todo 先删除以前拥有的菜单
        sysRoleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));
        // 移除之前的值成功
        List<Long> privilegeIds = rolePrivilegesParam.getPrivilegeIds();
        if (!CollectionUtils.isEmpty(privilegeIds)) {
            List<SysRolePrivilege> sysRolePrivileges = new ArrayList<>();
            for (Long privilegeId : privilegeIds) {
                SysRolePrivilege sysRolePrivilege = new SysRolePrivilege();
                sysRolePrivilege.setRoleId(rolePrivilegesParam.getRoleId());
                sysRolePrivilege.setPrivilegeId(privilegeId);
                sysRolePrivileges.add(sysRolePrivilege);
            }
            // 2 新增新的值
            boolean b = sysRolePrivilegeService.saveBatch(sysRolePrivileges);
            //todo 1  有了对应的权限就该有对应的菜单了 添加新菜单
            //查询权限所拥有的菜单项id
            List<SysPrivilege> sysPrivileges = sysPrivilegeService.listByIds(privilegeIds);
            List<@NotNull Long> menuIdList = sysPrivileges.stream().map(SysPrivilege::getMenuId).distinct().collect(Collectors.toList());
            List<SysRoleMenu> sysRoleMenus = new ArrayList<>();
            menuIdList.forEach((v) -> {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setMenuId(v);
                sysRoleMenu.setRoleId(roleId);
                sysRoleMenus.add(sysRoleMenu);
            });
            //todo 2 还要添加父级菜单 不然前端无法展示
            //通过菜单id查询父级菜单id
            List<SysMenu> parentIdList = sysMenuService.listByIds(menuIdList);
            List<Long> collect = parentIdList.stream().map(SysMenu::getParentId).distinct().collect(Collectors.toList());
            collect.forEach((v) -> {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setMenuId(v);
                sysRoleMenu.setRoleId(roleId);
                sysRoleMenus.add(sysRoleMenu);
            });
            boolean b1 = sysRoleMenuService.saveBatch(sysRoleMenus);
            //todo 3 2011year11月20号发现漏洞 子菜单应该全部携带，递归查询所有子菜单并建立关系
            List<Long> menuIds = selectPrentMenu(menuIdList);
            List<SysRoleMenu> sysRoleMenus2 = new ArrayList<>();
            menuIds.forEach((v) -> {
                SysRoleMenu sysRoleMenu = new SysRoleMenu();
                sysRoleMenu.setMenuId(v);
                sysRoleMenu.setRoleId(roleId);
                sysRoleMenus2.add(sysRoleMenu);
            });
            boolean b2 = sysRoleMenuService.saveBatch(sysRoleMenus2);
            return b && b1 && b2;
        }
        // 2 新增该角色的权限
        return true;
    }

    /**
     * 功能描述: <br>
     * 〈递归查询所有子菜单〉
     *
     * @Param: [menuIdList]
     * @return: void
     * @Author: xiaozhang666
     * @Date: 2021/11/30 15:11
     */
    private List<Long> selectPrentMenu(List<Long> menuIdList) {
        if (menuIdList.size() == 0) {
            return Collections.EMPTY_LIST;
        }
        List<Long> idList = new ArrayList<>();
        menuIdList.forEach(v -> {
            List<SysMenu> list = sysMenuService.list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, v));
            List<Long> collect = list.stream().map(SysMenu::getId).collect(Collectors.toList());
            idList.addAll(collect);
        });
        idList.addAll(selectPrentMenu(idList));
        return idList;
    }

}
