package com.killb.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.killb.domain.Notice;
import com.killb.model.R;
import com.killb.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;

/**
 * @program: coin-exchangs
 * @description: 公告管理
 * @author: xiaozhang666
 * @create: 2021-11-15 10:42
 **/
@RestController
@RequestMapping("/notices")
@Api(tags = "公告管理")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 分页查询
     */
    @GetMapping
    @ApiOperation(value = "分页查询公告")
    @PreAuthorize("hasAuthority('notice_query')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页显示的条数"),
            @ApiImplicitParam(name = "title", value = "公告的标题"),
            @ApiImplicitParam(name = "startTime", value = "公告的创建开始时间"),
            @ApiImplicitParam(name = "endTime", value = "公告的创建结束时间时间"),
    })
    public R<Page<Notice>> findByPage(@ApiIgnore Page<Notice> page, String title, String startTime, String endTime, Integer status) {
        page.addOrder(OrderItem.desc("last_update_time"));
        return R.ok(noticeService.findByPage(page, title, startTime, endTime, status));
    }


    @PostMapping("/delete")
    @ApiOperation(value = "删除一个公告")
    @PreAuthorize("hasAuthority('notice_delete')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "要删除的id的集合")
    })
    public R delete(@RequestBody String[] ids) {
        if (ids == null || ids.length == 0) {
            return R.fail("删除时需要给id的值");
        }
        boolean b = noticeService.removeByIds(Arrays.asList(ids));
        if (b) {
            return R.ok();
        }
        return R.fail("删除失败");
    }


    @PostMapping("/updateStatus")
    @ApiOperation(value = "启用/禁用一个公告")
    @PreAuthorize("hasAuthority('notice_update')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要启用/禁用一个公告的公告Id"),
            @ApiImplicitParam(name = "status", value = "要设置的公告状态")

    })
    public R updateStatus(Long id, Integer status) {
        Notice notice = new Notice();
        notice.setId(id);
        notice.setStatus(status);
        boolean b = noticeService.updateById(notice); // 局部的修改：不为null 修改
        if (b) {
            return R.ok("修改成功");
        }
        return R.fail("修改失败");
    }

    @PostMapping
    @ApiOperation(value = "新增一个公告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "notice" ,value = "notice的json数据")
    })
    @PreAuthorize("hasAuthority('notice_create')")

    public R add(@RequestBody @Validated Notice notice){
        notice.setStatus(1);
        boolean save = noticeService.save(notice); //
        if(save){
            return R.ok();
        }
        return  R.fail("新增公告失败") ;
    }



    @PatchMapping
    @ApiOperation(value = "修改一个公告")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "notice" ,value = "notice的json数据")
    })
    @PreAuthorize("hasAuthority('notice_update')")
    public R update(@RequestBody  @Validated  Notice notice){
        boolean update = noticeService.updateById(notice);
        if(update){
            return R.ok();
        }
        return  R.fail("修改公告失败") ;

    }


}
