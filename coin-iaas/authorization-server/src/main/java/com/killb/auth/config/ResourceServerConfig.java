package com.killb.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * @program: coin-exchangs
 * @description: 将授权服务器变为资源服务器
 * @author: xiaozhang666
 * @create: 2021-06-29 16:51
 **/
@EnableResourceServer
@Configuration
public class ResourceServerConfig  extends ResourceServerConfigurerAdapter {
}
