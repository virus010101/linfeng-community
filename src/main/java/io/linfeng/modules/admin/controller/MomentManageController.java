package io.linfeng.modules.admin.controller;

import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.R;
import io.linfeng.modules.app.entity.MomentEntity;
import io.linfeng.modules.app.service.MomentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

/**
 * @author linfeng
 * @date 2022/1/5 16:56
 */
@RestController
@RequestMapping("admin/moment")
@Api(tags = "管理端——动态接口")
public class MomentManageController {


    @Autowired
    private MomentService momentService;

    /**
     * 列表
     */
    @ApiOperation("后台——动态分页列表")
    @GetMapping("/list")
    @RequiresPermissions("app:moment:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = momentService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @ApiOperation("后台——根据id获取动态")
    @GetMapping("/info/{id}")
    @RequiresPermissions("app:moment:info")
    public R info(@PathVariable("id") Long id){
        MomentEntity moment = momentService.getById(id);

        return R.ok().put("moment", moment);
    }

    /**
     * 保存
     */
    @ApiOperation("后台——保存动态")
    @PostMapping("/save")
    @RequiresPermissions("app:moment:save")
    public R save(@RequestBody MomentEntity moment){
        momentService.save(moment);

        return R.ok();
    }

    /**
     * 修改
     */
    @ApiOperation("后台——修改动态")
    @PostMapping("/update")
    @RequiresPermissions("app:moment:update")
    public R update(@RequestBody MomentEntity moment){
        momentService.updateById(moment);

        return R.ok();
    }

    /**
     * 删除
     */
    @ApiOperation("后台——删除动态")
    @PostMapping("/delete")
    @RequiresPermissions("app:moment:delete")
    public R delete(@RequestBody Long[] ids){
        momentService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
