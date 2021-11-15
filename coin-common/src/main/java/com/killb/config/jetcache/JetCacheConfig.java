package com.killb.config.jetcache;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.context.annotation.Configuration;

/**
 * @program: coin-exchangs
 * @description: ali-jetcache注解缓存
 * @author: xiaozhang666
 * @create: 2021-07-06 16:39
 **/
@Configuration
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = "com.killb.service.impl")
public class JetCacheConfig {
}
