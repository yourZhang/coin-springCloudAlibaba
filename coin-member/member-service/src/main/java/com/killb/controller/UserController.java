package com.killb.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.killb.domain.User;
import com.killb.domain.UserAuthAuditRecord;
import com.killb.domain.UserAuthInfo;
import com.killb.domain.UserBank;
import com.killb.dto.UserDto;
import com.killb.feign.UserServiceFeign;
import com.killb.model.*;
import com.killb.service.UserAuthAuditRecordService;
import com.killb.service.UserAuthInfoService;
import com.killb.service.UserBankService;
import com.killb.service.UserService;
import com.killb.vo.UseAuthInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Collections;
import java.util.List;

/**
 * @program: coin-exchangs
 * @description: 会员控制器
 * @author: xiaozhang666
 * @create: 2021-11-25 14:26
 **/
@RestController
@RequestMapping("/users")
@Api(tags = "会员的控制器")
public class UserController implements UserServiceFeign {

    @Autowired
    private UserService userService;

    @GetMapping
    @ApiOperation(value = "查询会员的列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数"),
            @ApiImplicitParam(name = "mobile", value = "会员的手机号"),
            @ApiImplicitParam(name = "userId", value = "会员的Id,精确查询"),
            @ApiImplicitParam(name = "userName", value = "会员的名称"),
            @ApiImplicitParam(name = "realName", value = "会员的真实名称"),
            @ApiImplicitParam(name = "status", value = "会员的状态")

    })
    @PreAuthorize("hasAuthority('user_query')")
    public R<Page<User>> findByPage(@ApiIgnore Page<User> page,
                                    String mobile,
                                    Long userId,
                                    String userName,
                                    String realName,
                                    Integer status
    ) {
        page.addOrder(OrderItem.desc("last_update_time"));
        Page<User> userPage = userService.findByPage(page, mobile,
                userId, userName,
                realName, status,
                null);
        return R.ok(userPage);
    }


    @PostMapping("/status")
    @ApiOperation(value = "修改用户的状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "会员的id"),
            @ApiImplicitParam(name = "status", value = "会员的状态"),
    })
    @PreAuthorize("hasAuthority('user_update')")
    public R updateStatus(Long id, Byte status) {
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        boolean updateById = userService.updateById(user);
        if (updateById) {
            return R.ok("更新成功");
        }
        return R.fail("更新失败");
    }


    @PatchMapping
    @ApiOperation(value = "修改用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "会员的json数据"),
    })
    @PreAuthorize("hasAuthority('user_update')")
    public R updateStatus(@RequestBody @Validated User user) {
        boolean updateById = userService.updateById(user);
        if (updateById) {
            return R.ok("更新成功");
        }
        return R.fail("更新失败");
    }
    //==============================================用户的更多功能-======================

    @GetMapping("/info")
    @ApiOperation(value = "获取用户的详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户的Id")
    })
    @PreAuthorize("hasAuthority('user_query')")
    public R<User> userInfo(Long id) {
        return R.ok(userService.getById(id));
    }

    /**
     * 功能描述: <br>
     * 〈查询该用户邀请的用户列表〉
     *
     * @Param: [page, userId]
     * @return: com.killb.model.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page < com.killb.domain.User>>
     * @Author: xiaozhang666
     * @Date: 2021/11/25 15:48
     */
    @GetMapping("/directInvites")
    @ApiOperation(value = "查询该用户邀请的用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "该用户的Id"),
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数"),

    })
    public R<Page<User>> getDirectInvites(@ApiIgnore Page<User> page, Long userId) {
        Page<User> userPage = userService.findDirectInvitePage(page, userId);
        return R.ok(userPage);
    }


    //=====================================实名认证==============================================
    @GetMapping("/auths")
    @ApiOperation(value = "查询用户的审核列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数"),
            @ApiImplicitParam(name = "mobile", value = "会员的手机号"),
            @ApiImplicitParam(name = "userId", value = "会员的Id,精确查询"),
            @ApiImplicitParam(name = "userName", value = "会员的名称"),
            @ApiImplicitParam(name = "realName", value = "会员的真实名称"),
            @ApiImplicitParam(name = "reviewsStatus", value = "会员的状态")

    })
    public R<Page<User>> findUserAuths(
            @ApiIgnore Page<User> page,
            String mobile,
            Long userId,
            String userName,
            String realName,
            Integer reviewsStatus
    ) {
        Page<User> userPage = userService.findByPage(page, mobile, userId, userName, realName, null, reviewsStatus);
        return R.ok(userPage);
    }

    /**
     * 询用户的认证详情
     * {
     * user:
     * userAuthInfoList:[]
     * userAuditRecordList:[]
     * }
     */
    //todo  以下两个接口是用户认证审核的，等写完了用户功能再回头看
    @Autowired
    private UserAuthInfoService userAuthInfoService;

    @Autowired
    private UserAuthAuditRecordService userAuthAuditRecordService;

    @GetMapping("/auth/info")
    @ApiOperation(value = "查询用户的认证详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户的Id")
    })
    public R<UseAuthInfoVo> getUseAuthInfo(Long id) {

        User user = userService.getById(id);
        List<UserAuthAuditRecord> userAuthAuditRecordList = null;
        List<UserAuthInfo> userAuthInfoList = null;
        if (user != null) {
            // 用户的审核记录
            Integer reviewsStatus = user.getReviewsStatus();
            if (reviewsStatus == null || reviewsStatus == 0) { // 待审核
                userAuthAuditRecordList = Collections.emptyList(); // 用户没有审核记录
                //
                userAuthInfoList = userAuthInfoService.getUserAuthInfoByUserId(id);
            } else {
                userAuthAuditRecordList = userAuthAuditRecordService.getUserAuthAuditRecordList(id);
                // 查询用户的认证详情列表-> 用户的身份信息
                UserAuthAuditRecord userAuthAuditRecord = userAuthAuditRecordList.get(0);// 之前我们查询时,是按照认证的日志排序的,第0 个值,就是最近被认证的一个值
                Long authCode = userAuthAuditRecord.getAuthCode(); // 认证的唯一标识
                userAuthInfoList = userAuthInfoService.getUserAuthInfoByCode(authCode);
            }
        }
        return R.ok(new UseAuthInfoVo(user, userAuthInfoList, userAuthAuditRecordList));
    }


    /**
     * 审核的本质:
     * 在于对一组图片(唯一Code)的认可,符合条件,审核通过
     *
     * @return
     */
    @PostMapping("/auths/status")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户的ID"),
            @ApiImplicitParam(name = "authStatus", value = "用户的审核状态"),
            @ApiImplicitParam(name = "authCode", value = "一组图片的唯一标识"),
            @ApiImplicitParam(name = "remark", value = "审核拒绝的原因"),
    })
    public R updateUserAuthStatus(@RequestParam(required = true) Long id,
                                  @RequestParam(required = true) Byte authStatus,
                                  @RequestParam(required = true) Long authCode,
                                  String remark) {
        // 审核: 1 修改user 里面的reviewStatus
        // 2 在authAuditRecord 里面添加一条记录
        userService.updateUserAuthStatus(id, authStatus, authCode, remark);
        return R.ok();
    }


    //=======================================查询用户信息========================================
    @GetMapping("/current/info")
    @ApiOperation(value = "获取当前登录用户对象的信息")
    public R<User> currentUserInfo() {
        String idStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.getById(Long.valueOf(idStr));
        user.setPassword("****");
        user.setPaypassword("***");
        user.setAccessKeyId("****");
        user.setAccessKeySecret("******");
        return R.ok(user);
    }


    //实名认证
    @PostMapping("/authAccount")
    @ApiOperation(value = "用户的实名认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "", value = "")
    })
    public R identifyCheck(@RequestBody UserAuthForm userAuthForm) {
        String idStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        boolean isOk = userService.identifyVerify(Long.valueOf(idStr), userAuthForm);
        if (isOk) {
            return R.ok();
        }
        return R.fail("认证失败");
    }
    //todo ===============用户高级实名认证 ============== 未写 上传正反面oss进行认证


    //========================修改手机号================
    @PostMapping("/updatePhone")
    @ApiOperation(value = "修改手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "updatePhoneParam", value = "updatePhoneParam 的json数据")
    })
    public R updatePhone(@RequestBody UpdatePhoneParam updatePhoneParam) {
        String idStr = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        boolean isOk = userService.updatePhone(Long.valueOf(idStr), updatePhoneParam);
        if (isOk) {
            return R.ok();
        }
        return R.fail("修改失败");
    }

    /**
     * 功能描述: <br>
     * 〈校验手机号〉
     *
     * @Param: [mobile, countryCode]
     * @return: com.killb.model.R
     * @Author: xiaozhang666
     * @Date: 2021/12/3 15:05
     */
    @GetMapping("/checkTel")
    @ApiOperation(value = "检查新的手机号是否可用,如可用,则给该新手机发送验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "新的手机号"),
            @ApiImplicitParam(name = "countryCode", value = "手机号的区域")
    })
    public R checkNewPhone(@RequestParam(required = true) String mobile, @RequestParam(required = true) String countryCode) {
        boolean isOk = userService.checkNewPhone(mobile, countryCode);
        return isOk ? R.ok() : R.fail("新的手机号校验失败");
    }


    //=====================修改用户密码=========================

    @PostMapping("/updateLoginPassword")
    @ApiOperation(value = "修改用户的登录密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "updateLoginParam", value = "修改用户的登录密码")
    })
    public R updateLoginPwd(@RequestBody @Validated UpdateLoginParam updateLoginParam) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        boolean isOk = userService.updateUserLoginPwd(userId, updateLoginParam);
        if (isOk) {
            return R.ok();
        }
        return R.fail("修改失败");
    }


    @PostMapping("/updatePayPassword")
    @ApiOperation(value = "修改用户的交易密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "updateLoginParam", value = "修改用户的交易密码")
    })
    public R updatePayPwd(@RequestBody @Validated UpdateLoginParam updateLoginParam) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        boolean isOk = userService.updateUserPayPwd(userId, updateLoginParam);
        if (isOk) {
            return R.ok();
        }
        return R.fail("修改失败");
    }


    @PostMapping("/setPayPassword")
    @ApiOperation(value = "重新设置交易密码")
    public R setPayPassword(@RequestBody @Validated UnsetPayPasswordParam unsetPayPasswordParam) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        boolean isOk = userService.unsetPayPassword(userId, unsetPayPasswordParam);
        if (isOk) {
            return R.ok();
        }
        return R.fail("重置失败");
    }

    /**
     * 功能描述: <br>
     * 〈用户的邀请列表〉
     *
     * @Param: []
     * @return: com.killb.model.R<java.util.List < com.killb.domain.User>>
     * @Author: xiaozhang666
     * @Date: 2021/12/3 17:00
     */
    @GetMapping("/invites")
    @ApiOperation(value = "用户的邀请列表")
    public R<List<User>> getUserInvites() {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        List<User> users = userService.getUserInvites(userId);
        return R.ok(users);
    }

    @PostMapping("/setPassword")
    @ApiOperation(value = "用户重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "unSetPasswordParam", value = "unSetPasswordParam json")
    })
    public R unsetPassword(@RequestBody @Validated UnSetPasswordParam unSetPasswordParam) {
        boolean isOk = userService.unsetLoginPwd(unSetPasswordParam);
        if (isOk) {
            return R.ok();
        }
        return R.fail("重置失败");
    }



    //==============================远程调用======================
    @Override
    public List<UserDto> getBasicUsers(List<Long> ids) {
        List<UserDto> userDtos = userService.getBasicUsers(ids);
        return userDtos;

    }


    //用户注册=====================================
    @PostMapping("/register")
    @ApiOperation(value = "用户的注册")
    public R register(@RequestBody RegisterParam registerParam) {
        boolean isOk = userService.register(registerParam);
        if (isOk) {
            return R.ok();
        }
        return R.fail("注册失败");
    }

}
