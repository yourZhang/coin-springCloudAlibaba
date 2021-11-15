package com.killb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.killb.domain.SysPrivilege;

import java.util.List;
import java.util.Set;

public interface SysPrivilegeMapper extends BaseMapper<SysPrivilege> {
    /**
     * 使用角色Id 查询权限
     * @param roleId
     * @return
     */
    List<SysPrivilege> selectByRoleId(Long roleId);

    /**
     * 使用角色的ID 查询权限的id
     * @param roleId
     * @return
     */
    Set<Long> getPrivilegesByRoleId(Long roleId);

}