/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 商业版详情查看: https://www.linfeng.tech
 * 商业版购买联系技术客服
 * QQ: 3582996245
 * 可正常分享和学习源码，不得转卖或非法牟利！
 * Copyright (c) 2021-2023 linfeng all rights reserved.
 * 版权所有 ，侵权必究！
 * -----------------------------------
 */
package io.linfeng.modules.admin.controller;

import java.util.Arrays;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.linfeng.modules.admin.entity.CommentEntity;
import io.linfeng.modules.admin.service.CommentService;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.R;



/**
 * 
 * 评论管理
 * @author linfeng
 * @email 2445465217@qq.com
 * @date 2022-01-24 21:29:22
 */
@Api(tags = "管理端——评论管理")
@RestController
@RequestMapping("admin/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;



    @GetMapping("/list")
    @RequiresPermissions("admin:comment:list")
    @ApiOperation("评论列表")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = commentService.queryPage(params);

        return R.ok().put("page", page);
    }



    @GetMapping("/info/{id}")
    @RequiresPermissions("admin:comment:info")
    @ApiOperation("评论详情")
    public R info(@PathVariable("id") Long id){
		CommentEntity comment = commentService.getById(id);

        return R.ok().put("comment", comment);
    }



    @PostMapping("/update")
    @RequiresPermissions("admin:comment:update")
    @ApiOperation("评论修改")
    public R update(@RequestBody CommentEntity comment){
		commentService.updateById(comment);

        return R.ok();
    }


    @PostMapping("/delete")
    @RequiresPermissions("admin:comment:delete")
    @ApiOperation("评论删除")
    public R delete(@RequestBody Long[] ids){
        commentService.deleteByAdmin(Arrays.asList(ids));
        return R.ok();
    }

}
