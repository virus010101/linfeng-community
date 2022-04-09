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

import io.linfeng.modules.admin.entity.LinkEntity;
import io.linfeng.modules.admin.service.LinkService;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.R;



/**
 * 
 *
 * @author linfeng
 * @email 2445465217@qq.com
 * @date 2022-01-26 14:05:38
 */
@RestController
@RequestMapping("admin/link")
public class LinkController {
    @Autowired
    private LinkService linkService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("admin:link:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = linkService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("admin:link:info")
    public R info(@PathVariable("id") Integer id){
		LinkEntity link = linkService.getById(id);

        return R.ok().put("link", link);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("admin:link:save")
    public R save(@RequestBody LinkEntity link){
		linkService.save(link);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("admin:link:update")
    public R update(@RequestBody LinkEntity link){
		linkService.updateById(link);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("admin:link:delete")
    public R delete(@RequestBody Integer[] ids){
		linkService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
