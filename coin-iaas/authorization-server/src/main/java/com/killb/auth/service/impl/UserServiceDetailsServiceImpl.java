package com.killb.auth.service.impl;

import com.killb.auth.common.LoginConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.util.StringUtils;
import org.springframework.security.core.userdetails.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: coin-exchangs
 * @description: 用户类
 * @author: xiaozhang666
 * @create: 2021-07-01 15:14
 **/
@Service
public class UserServiceDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String loginType = requestAttributes.getRequest().getParameter("login_type");
        if (StringUtils.isEmpty(loginType)) {
            throw new AuthenticationServiceException("请添加login_type参数");
        }
        String grantType = requestAttributes.getRequest().getParameter("grant_type"); // refresh_token 进行纠正
        if (LoginConstant.REFRESH_TYPE.equals(grantType.toUpperCase())) {
            username = adjustUsername(username, loginType);
        }
        UserDetails userDetails = null;
        switch (loginType) {
            case LoginConstant.ADMIN_TYPE: // 管理员登录
                userDetails = loadSysUserByUsername(username);
                break;
            case LoginConstant.MEMBER_TYPE: // 会员登录
                userDetails = loadMemberUserByUsername(username);
                break;
            default:
                throw new AuthenticationServiceException("暂不支持的登录方式" + loginType);
        }
        return userDetails;
    }

    /**
     * 纠正用户的名称
     *
     * @param username  用户的id
     * @param loginType admin_type  member_type
     * @return
     */
    private String adjustUsername(String username, String loginType) {
        if (LoginConstant.ADMIN_TYPE.equals(loginType)) {
            // 管理员的纠正方式
            return jdbcTemplate.queryForObject(LoginConstant.QUERY_ADMIN_USER_WITH_ID,String.class ,username);
        }
        if (LoginConstant.MEMBER_TYPE.equals(loginType)) {
            // 会员的纠正方式
            return jdbcTemplate.queryForObject(LoginConstant.QUERY_MEMBER_USER_WITH_ID,String.class ,username);
        }
        return username;
    }

    /**
     * 功能描述: <br>
     * 〈后台人员的登录〉
     *
     * @Param: [username]
     * @return: org.springframework.security.core.userdetails.UserDetails
     * @Author: xiaozhang666
     * @Date: 2021/7/1 15:36
     */
    private UserDetails loadSysUserByUsername(String username) {
        // 1 使用用户名查询用户
        return jdbcTemplate.queryForObject(LoginConstant.QUERY_ADMIN_SQL, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                if (rs.wasNull()) {
                    throw new UsernameNotFoundException("用户名" + username + "不存在");
                }
                long id = rs.getLong("id"); // 用户的id
                String password = rs.getString("password"); // 用户的密码
                int status = rs.getInt("status");
                return new User(   // 3 封装成一个UserDetails对象，返回
                        String.valueOf(id), //使用id->username
                        password,
                        status == 1,
                        true,
                        true,
                        true,
                        getSysUserPermissions(id)
                );
            }
        }, username);
    }


    /**
     * 功能描述: <br>
     * 〈2 查询这个用户对应的权限
     * * 通过用户的id 查询用户的权限〉
     *
     * @Param: [id]
     * @return: java.util.Collection<? extends org.springframework.security.core.GrantedAuthority>
     * @Author: xiaozhang666
     * @Date: 2021/7/1 15:36
     */
    private Collection<? extends GrantedAuthority> getSysUserPermissions(long id) {
        // 1 当用户为超级管理员时，他拥有所有的权限数据
        String roleCode = jdbcTemplate.queryForObject(LoginConstant.QUERY_ROLE_CODE_SQL, String.class, id);
        List<String> permissions = null; // 权限的名称
        if (LoginConstant.ADMIN_ROLE_CODE.equals(roleCode)) { // 超级用户
            permissions = jdbcTemplate.queryForList(LoginConstant.QUERY_ALL_PERMISSIONS, String.class);
        } else { // 2 普通用户，需要使用角色->权限数据
            permissions = jdbcTemplate.queryForList(LoginConstant.QUERY_PERMISSION_SQL, String.class, id);
        }
        if (permissions == null || permissions.isEmpty()) {
            return Collections.emptySet();
        }
        return permissions.stream()
                .distinct() // 去重
                .map(perm -> new SimpleGrantedAuthority(perm))
                .collect(Collectors.toSet());
    }

    /**
     * 功能描述: <br>
     * 〈会员的登录〉
     *
     * @Param: [username]
     * @return: org.springframework.security.core.userdetails.UserDetails
     * @Author: xiaozhang666
     * @Date: 2021/7/1 15:36
     */
    private UserDetails loadMemberUserByUsername(String username) {
        return jdbcTemplate.queryForObject(LoginConstant.QUERY_MEMBER_SQL, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                if (rs.wasNull()) {
                    throw new UsernameNotFoundException("用户：" + username + "不存在");
                }
                long id = rs.getLong("id"); // 会员的id
                String password = rs.getString("password");// 会员的登录密码
                int status = rs.getInt("status"); // 会员的状态
                return new User(
                        String.valueOf(id),
                        password,
                        status == 1,
                        true,
                        true,
                        true,
                        Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
                );
            }
        }, username, username);
    }
}
