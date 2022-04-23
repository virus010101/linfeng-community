package io.linfeng.modules.admin.controller;

import io.linfeng.common.utils.R;
import io.linfeng.modules.admin.service.AppUserService;
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
public class StatisticController {

    @Autowired
    private AppUserService userService;


    @GetMapping("/home")
    public R index() {

        return R.ok().put("result", userService.indexDate());
    }
}
