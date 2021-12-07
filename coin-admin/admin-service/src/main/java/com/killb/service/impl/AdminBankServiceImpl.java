package com.killb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.killb.dto.AdminBankDto;
import com.killb.service.AdminBankService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.killb.mapper.AdminBankMapper;
import com.killb.domain.AdminBank;
import org.springframework.util.StringUtils;

@Service
public class AdminBankServiceImpl extends ServiceImpl<AdminBankMapper, AdminBank> implements AdminBankService {

    /**
     * 条件分页查询公司银行卡
     *
     * @param page     分页参数
     * @param bankCard 银行卡卡号
     * @return
     */
    @Override
    public Page<AdminBank> findByPage(Page<AdminBank> page, String bankCard) {
        return page(page,new LambdaQueryWrapper<AdminBank>()
                .like(!StringUtils.isEmpty(bankCard),AdminBank::getBankCard ,bankCard));
    }

    @Override
    public List<AdminBankDto> getAllAdminBanks() {
        return null;
    }
}
