package com.killb.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

/**
 * @program: coin-exchangs
 * @description: 授权服务器的配置
 * @author: xiaozhang666
 * @create: 2021-06-22 17:08
 **/
@Configuration
@EnableAuthorizationServer
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 功能描述: <br>
     * 〈设置授权管理器和UserDetailsService〉
     *
     * @Param: [endpoints]
     * @return: void
     * @Author: xiaozhang666
     * @Date: 2021/6/23 15:32
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(new InMemoryTokenStore())
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }

    /**
     * 功能描述: <br>
     * 〈配置第三方客户端〉
     *
     * @Param: [clients]
     * @return: void
     * @Author: xiaozhang666
     * @Date: 2021/6/23 15:31
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("coin-api")
                .secret(passwordEncoder.encode("coin-secret"))
                .scopes("all")
                .authorizedGrantTypes("password", "refresh")
                .accessTokenValiditySeconds(24 * 7200)
                .refreshTokenValiditySeconds(7 * 24 * 7200);
    }
}
