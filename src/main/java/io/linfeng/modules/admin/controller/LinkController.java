/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 商业版详情查看: https://www.linfengtech.cn
 * 商业版购买联系技术客服
 * QQ: 3582996245
 * 可正常分享和学习源码，不得转卖或非法牟利！
 * Copyright (c) 2021-2025 linfeng all rights reserved.
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

import io.linfeng.modules.admin.entity.LinkEntity;
import io.linfeng.modules.admin.service.LinkService;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.R;



/**
 * 管理端——轮播图管理
 *
 * @author linfeng
 * @email 2445465217@qq.com
 * @date 2022-01-26 14:05:38
 */
@Api(tags = "管理端——轮播图管理")
@RestController
@RequestMapping("admin/link")
public class LinkController {
    @Autowired
    private LinkService linkService;


    @GetMapping("/list")
    @RequiresPermissions("admin:link:list")
    @ApiOperation("轮播图列表")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = linkService.queryPage(params);

        return R.ok().put("page", page);
    }



    @GetMapping("/info/{id}")
    @RequiresPermissions("admin:link:info")
    @ApiOperation("轮播图信息")
    public R info(@PathVariable("id") Integer id){
		LinkEntity link = linkService.getById(id);

        return R.ok().put("link", link);
    }


    @PostMapping("/save")
    @RequiresPermissions("admin:link:save")
    @ApiOperation("轮播图保存")
    public R save(@RequestBody LinkEntity link){
		linkService.save(link);

        return R.ok();
    }


    @PostMapping("/update")
    @RequiresPermissions("admin:link:update")
    @ApiOperation("轮播图修改")
    public R update(@RequestBody LinkEntity link){
		linkService.updateById(link);

        return R.ok();
    }


    @PostMapping("/delete")
    @RequiresPermissions("admin:link:delete")
    @ApiOperation("轮播图删除")
    public R delete(@RequestBody Integer[] ids){
		linkService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
