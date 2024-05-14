/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 演示站点: https://www.linfeng.tech
 * 可正常分享和学习源码，不得转卖或非法牟利！
 * 商业版购买联系技术客服  QQ: 3582996245
 * Copyright (c) 2021-2023 linfeng all rights reserved.
 * 版权所有 ，侵权必究！
 * -----------------------------------
 */
package io.linfeng.modules.admin.controller;

import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.linfeng.modules.admin.entity.AppUserEntity;
import io.linfeng.modules.admin.service.AppUserService;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.R;



/**
 * 
 * 管理端——会员管理
 * @author linfeng
 * @email 2445465217@qq.com
 * @date 2022-01-20 12:10:43
 */
@Api(tags = "管理端——会员管理")
@RestController
@RequestMapping("admin/user")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;


    @GetMapping("/list")
    @RequiresPermissions("admin:user:list")
    @ApiOperation("用户列表")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = appUserService.queryPage(params);

        return R.ok().put("page", page);
    }



    @GetMapping("/info/{uid}")
    @RequiresPermissions("admin:user:info")
    @ApiOperation("用户详情")
    public R info(@PathVariable("uid") Integer uid){
		AppUserEntity user = appUserService.getById(uid);

        return R.ok().put("user", user);
    }





    @PostMapping("/update")
    @RequiresPermissions("admin:user:update")
    @ApiOperation("用户修改")
    public R update(@RequestBody AppUserEntity user){
		appUserService.updateById(user);

        return R.ok();
    }




    @GetMapping("/ban/{id}")
    @RequiresPermissions("admin:user:update")
    @ApiOperation("用户禁用")
    public R ban(@PathVariable("id") Integer id){
		appUserService.ban(id);

        return R.ok();
    }


    @GetMapping("/openBan/{id}")
    @RequiresPermissions("admin:user:update")
    @ApiOperation("用户解除禁用")
    public R openBan(@PathVariable("id") Integer id){
		appUserService.openBan(id);

        return R.ok();
    }

}
