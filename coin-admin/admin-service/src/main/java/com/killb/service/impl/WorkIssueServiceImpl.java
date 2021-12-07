package com.killb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.killb.dto.UserDto;
import com.killb.feign.UserServiceFeign;
import com.killb.service.WorkIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.killb.mapper.WorkIssueMapper;
import com.killb.domain.WorkIssue;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class WorkIssueServiceImpl extends ServiceImpl<WorkIssueMapper, WorkIssue> implements WorkIssueService {

    @Autowired
    private UserServiceFeign userServiceFeign;

    /**
     * 条件分页查询工单列表
     *
     * @param page      分页参数
     * @param status    工单的状态
     * @param startTime 查询的工单创建起始时间
     * @param endTime   查询的工单创建截至时间
     * @return
     */
    @Override
    public Page<WorkIssue> findByPage(Page<WorkIssue> page, Integer status, String startTime, String endTime) {
        Page<WorkIssue> pageData = page(page, new LambdaQueryWrapper<WorkIssue>()
                .eq(status != null, WorkIssue::getStatus, status)
                .between(
                        !StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime),
                        WorkIssue::getCreated,
                        startTime, endTime + " 23:59:59")
        );
        if (CollectionUtils.isEmpty(pageData.getRecords())) {
            return pageData;
        }
        List<Long> userIds = pageData.getRecords()
                .stream()
                .map(WorkIssue::getUserId)
                .collect(Collectors.toList());
        List<UserDto> userDtos = userServiceFeign.getBasicUsers(userIds);
        if (CollectionUtils.isEmpty(userDtos)) {
            return pageData;
        }
        Map<Long, UserDto> idMappings = userDtos.stream().collect(Collectors.toMap(UserDto::getId, userDto -> userDto));
        pageData.getRecords().forEach(workIssue -> {
            UserDto userDto = idMappings.get(workIssue.getUserId());
            workIssue.setUsername(userDto == null ? "测试用户" : userDto.getUsername());
            workIssue.setRealName(userDto == null ? "测试用户" : userDto.getRealName());
        });
        return pageData;
    }


    /**
     * 前台系统查询客户工单
     *
     * @param page
     * @return
     */
    @Override
    public Page<WorkIssue> getIssueList(Page<WorkIssue> page, Long userId) {
        return page(page, new LambdaQueryWrapper<WorkIssue>()
                        .eq(WorkIssue::getUserId, userId)
//                                            .eq(WorkIssue::getStatus,1)
        );
    }
}
