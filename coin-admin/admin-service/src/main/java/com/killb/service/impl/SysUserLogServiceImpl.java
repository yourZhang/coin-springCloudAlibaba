package com.killb.service.impl;

import com.killb.service.SysUserLogService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.killb.mapper.SysUserLogMapper;
import com.killb.domain.SysUserLog;
@Service
public class SysUserLogServiceImpl extends ServiceImpl<SysUserLogMapper, SysUserLog> implements SysUserLogService {

}
