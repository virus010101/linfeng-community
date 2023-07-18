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

import io.linfeng.modules.admin.entity.SystemEntity;
import io.linfeng.modules.admin.service.SystemService;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.R;



/**
 * 
 * @author linfeng
 * @email 2445465217@qq.com
 * @date 2022-01-19 16:29:48
 */
@Api(tags = "管理端——获取配置接口")
@RestController
@RequestMapping("admin/system")
public class SystemController {
    @Autowired
    private SystemService systemService;

    /**
     * 列表
     */
    @ApiOperation("后台——配置分页列表")
    @GetMapping("/list")
    @RequiresPermissions("admin:system:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = systemService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @ApiOperation("后台——根据id获取配置详情")
    @GetMapping("/info/{config}")
    @RequiresPermissions("admin:system:info")
    public R info(@PathVariable("config") String config){
		SystemEntity system = systemService.getById(config);

        return R.ok().put("system", system);
    }

    /**
     * 保存
     */
    @ApiOperation("后台——配置保存")
    @PostMapping("/save")
    @RequiresPermissions("admin:system:save")
    public R save(@RequestBody SystemEntity system){
		systemService.save(system);

        return R.ok();
    }

    /**
     * 修改
     */
    @ApiOperation("后台——配置修改")
    @PostMapping("/update")
    @RequiresPermissions("admin:system:update")
    public R update(@RequestBody SystemEntity system){
		systemService.updateById(system);

        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation("后台——配置删除")
    @PostMapping("/delete")
    @RequiresPermissions("admin:system:delete")
    public R delete(@RequestBody String[] configs){
		systemService.removeByIds(Arrays.asList(configs));

        return R.ok();
    }

}
