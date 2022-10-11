/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 商业版演示站点: https://www.linfeng.tech
 * 商业版购买联系技术客服
 * QQ: 3582996245
 * 可正常分享和学习源码，不得专卖或非法牟利！
 * Copyright (c) 2021-2023 linfeng all rights reserved.
 * 版权所有 ，侵权必究！
 * -----------------------------------
 */
package io.linfeng.modules.admin.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.linfeng.modules.admin.entity.SensitiveEntity;
import io.linfeng.modules.admin.service.SensitiveService;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.R;



/**
 * 敏感词库信息表
 *
 * @author linfeng
 * @email 2445465217@qq.com
 * @date 2022-01-28 13:40:57
 */
@RestController
@RequestMapping("admin/sensitive")
public class SensitiveController {
    @Autowired
    private SensitiveService sensitiveService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("admin:sensitive:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = sensitiveService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("admin:sensitive:info")
    public R info(@PathVariable("id") Long id){
		SensitiveEntity sensitive = sensitiveService.getById(id);

        return R.ok().put("sensitive", sensitive);
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("admin:sensitive:update")
    public R update(@RequestBody SensitiveEntity sensitive){
		sensitiveService.updateById(sensitive);

        return R.ok();
    }



}
