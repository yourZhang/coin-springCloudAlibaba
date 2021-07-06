package com.killb.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @program: coin-exchangs
 * @description: web安全配置
 * @author: xiaozhang666
 * @create: 2021-06-23 15:26
 **/
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 功能描述: <br>
     * 〈配置资源放行〉
     *
     * @Param: [http]
     * @return: void
     * @Author: xiaozhang666
     * @Date: 2021/6/23 15:29
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // 关闭scrf
        http.authorizeRequests().anyRequest().authenticated();
    }

    /**
     * 功能描述: <br>
     * 〈注入验证管理器〉
     *
     * @Param: []
     * @return: org.springframework.security.authentication.AuthenticationManager
     * @Author: xiaozhang666
     * @Date: 2021/6/23 15:29
     */
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 功能描述: <br>
     * 〈注入密码验证管理器〉
     *
     * @Param: []
     * @return: org.springframework.security.crypto.password.PasswordEncoder
     * @Author: xiaozhang666
     * @Date: 2021/6/23 15:28
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 功能描述: <br>
     * 〈创建一个测试的UserDetail〉
     *
     * @Param: []
     * @return: org.springframework.security.core.userdetails.UserDetailsService
     * @Author: xiaozhang666
     * @Date: 2021/6/23 15:30
     */
//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
//        User user = new User("admin", "123456", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
//        inMemoryUserDetailsManager.createUser(user);
//        return inMemoryUserDetailsManager;
//    }
    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("123456");
        System.out.println(encode);
    }
}
