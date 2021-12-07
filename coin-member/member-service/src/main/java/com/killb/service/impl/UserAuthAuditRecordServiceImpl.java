package com.killb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.killb.mapper.UserAuthAuditRecordMapper;
import com.killb.domain.UserAuthAuditRecord;
import com.killb.service.UserAuthAuditRecordService;

@Service
public class UserAuthAuditRecordServiceImpl extends ServiceImpl<UserAuthAuditRecordMapper, UserAuthAuditRecord> implements UserAuthAuditRecordService {
    /**
     * 获取一个用户的审核记录
     *
     * @param id
     * @return
     */
    @Override
    public List<UserAuthAuditRecord> getUserAuthAuditRecordList(Long id) {
        return list(new LambdaQueryWrapper<UserAuthAuditRecord>()
                .eq(UserAuthAuditRecord::getUserId, id)
                .orderByDesc(UserAuthAuditRecord::getCreated)
                .last("limit 3")
        );
    }

}
