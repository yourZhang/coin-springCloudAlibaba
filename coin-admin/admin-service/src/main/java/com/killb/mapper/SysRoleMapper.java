package com.killb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.killb.domain.SysRole;

public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * 获取用户角色Code的实现
     * @param userId
     * @return
     */
    String getUserRoleCode(Long userId);

}