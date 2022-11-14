/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 演示站点: https://www.linfeng.tech
 * 可正常分享和学习源码，不得专卖或非法牟利！
 * 商业版购买联系技术客服  QQ: 3582996245
 * Copyright (c) 2021-2023 linfeng all rights reserved.
 * 版权所有 ，侵权必究！
 * -----------------------------------
 */
package io.linfeng.modules.admin.controller;

import java.util.Arrays;
import java.util.Map;

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
@RestController
@RequestMapping("admin/category")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("admin:category:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{cateId}")
    @RequiresPermissions("admin:category:info")
    public R info(@PathVariable("cateId") Integer cateId){
		CategoryEntity category = categoryService.getById(cateId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("admin:category:save")
    public R save(@RequestBody CategoryEntity category){

		categoryService.saveCategory(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("admin:category:update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateById(category);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @RequiresPermissions("admin:category:delete")
    public R delete(@RequestBody Integer[] cateIds){
		categoryService.deleteByIdList(Arrays.asList(cateIds));

        return R.ok();
    }

}
