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

import io.linfeng.modules.admin.entity.PostEntity;
import io.linfeng.modules.admin.service.PostService;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.R;



/**
 * 
 * 帖子管理
 * @author linfeng
 * @email 2445465217@qq.com
 * @date 2022-01-23 20:49:55
 */
@Api(tags = "管理端——帖子管理")
@RestController
@RequestMapping("admin/post")
public class PostController {
    @Autowired
    private PostService postService;


    @GetMapping("/list")
    @RequiresPermissions("admin:post:list")
    @ApiOperation("帖子列表")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = postService.queryPage(params);

        return R.ok().put("page", page);
    }



    @GetMapping("/info/{id}")
    @RequiresPermissions("admin:post:info")
    @ApiOperation("帖子信息")
    public R info(@PathVariable("id") Integer id){
		PostEntity post = postService.getById(id);

        return R.ok().put("post", post);
    }


    @PostMapping("/save")
    @RequiresPermissions("admin:post:save")
    @ApiOperation("帖子保存")
    public R save(@RequestBody PostEntity post){
		postService.save(post);

        return R.ok();
    }


    @PostMapping("/update")
    @RequiresPermissions("admin:post:update")
    @ApiOperation("帖子修改")
    public R update(@RequestBody PostEntity post){
		postService.updateById(post);

        return R.ok();
    }


    @PostMapping("/delete")
    @RequiresPermissions("admin:post:delete")
    @ApiOperation("帖子删除")
    public R delete(@RequestBody Integer[] ids){
        postService.deleteByAdmin(Arrays.asList(ids));
        return R.ok();
    }
	

}
