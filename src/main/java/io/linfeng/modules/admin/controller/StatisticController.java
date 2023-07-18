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

import io.linfeng.common.utils.R;
import io.linfeng.modules.admin.service.AppUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台前端首页数据统计
 *
 * @author linfeng
 * @date 2022/4/17 16:49
 */
@RestController
@RequestMapping("admin/statistics")
@Api(tags = "管理端——数据统计")
public class StatisticController {

    @Autowired
    private AppUserService userService;


    @GetMapping("/home")
    @ApiOperation("后台前端首页数据统计")
    public R index() {

        return R.ok().put("result", userService.indexDate());
    }
}
