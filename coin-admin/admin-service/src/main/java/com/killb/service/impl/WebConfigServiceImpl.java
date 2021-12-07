package com.killb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.killb.service.WebConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.killb.mapper.WebConfigMapper;
import com.killb.domain.WebConfig;
import org.springframework.util.StringUtils;

@Service
public class WebConfigServiceImpl extends ServiceImpl<WebConfigMapper, WebConfig> implements WebConfigService {
    /**
     * 条件分页查询
     *
     * @param page 分页参数
     * @param name webConfig的名称
     * @param type webConfig的类型
     * @return
     */
    @Override
    public Page<WebConfig> findByPage(Page<WebConfig> page, String name, String type) {

        return page(page, new LambdaQueryWrapper<WebConfig>()
                .like(!StringUtils.isEmpty(name), WebConfig::getName, name)
                .eq(!StringUtils.isEmpty(type), WebConfig::getType, type)
        );
    }

    /**
     * 查询所有的web_banner
     *
     * @return
     */
    @Override
    public List<WebConfig> findWebBanners() {
        return list(new LambdaQueryWrapper<WebConfig>()
                .eq(WebConfig::getType, "WEB_BANNER")
                .eq(WebConfig::getStatus, 1)
                .orderByAsc(WebConfig::getSort)
        );
    }


    /**
     * 查询PC端的banner
     *
     * @return
     */
    @Override
    public List<WebConfig> getPcBanners() {
        return list(new LambdaQueryWrapper<WebConfig>()
                .eq(WebConfig::getType, "WEB_BANNER")
                .eq(WebConfig::getStatus, 1)
                .orderByAsc(WebConfig::getSort)
        );
    }
}
