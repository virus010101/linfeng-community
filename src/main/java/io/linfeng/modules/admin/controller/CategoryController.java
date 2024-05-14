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

import java.util.Arrays;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.linfeng.modules.admin.entity.CategoryEntity;
import io.linfeng.modules.admin.service.CategoryService;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.R;



/**
 * 
 * @author linfeng
 * @email 2445465217@qq.com
 * @date 2022-01-21 14:32:52
 */
@Api(tags = "管理端——分类管理")
@RestController
@RequestMapping("admin/category")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;


    @GetMapping("/list")
    @RequiresPermissions("admin:category:list")
    @ApiOperation("分类列表")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryService.queryPage(params);

        return R.ok().put("page", page);
    }



    @GetMapping("/info/{cateId}")
    @RequiresPermissions("admin:category:info")
    @ApiOperation("分类详情")
    public R info(@PathVariable("cateId") Integer cateId){
		CategoryEntity category = categoryService.getById(cateId);

        return R.ok().put("category", category);
    }


    @PostMapping("/save")
    @RequiresPermissions("admin:category:save")
    @ApiOperation("分类保存")
    public R save(@RequestBody CategoryEntity category){

		categoryService.saveCategory(category);

        return R.ok();
    }


    @PostMapping("/update")
    @RequiresPermissions("admin:category:update")
    @ApiOperation("分类修改")
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateCategory(category);
        return R.ok();
    }


    @PostMapping("/delete")
    @RequiresPermissions("admin:category:delete")
    @ApiOperation("分类删除")
    public R delete(@RequestBody Integer[] cateIds){
		categoryService.deleteByIdList(Arrays.asList(cateIds));

        return R.ok();
    }

}
