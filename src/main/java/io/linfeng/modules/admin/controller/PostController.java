package io.linfeng.modules.admin.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.linfeng.modules.admin.entity.PostEntity;
import io.linfeng.modules.admin.service.PostService;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.R;



/**
 * 
 *帖子管理
 * @author linfeng
 * @email 2445465217@qq.com
 * @date 2022-01-23 20:49:55
 */
@RestController
@RequestMapping("admin/post")
public class PostController {
    @Autowired
    private PostService postService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("admin:post:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = postService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("admin:post:info")
    public R info(@PathVariable("id") Integer id){
		PostEntity post = postService.getById(id);

        return R.ok().put("post", post);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("admin:post:save")
    public R save(@RequestBody PostEntity post){
		postService.save(post);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @RequiresPermissions("admin:post:update")
    public R update(@RequestBody PostEntity post){
		postService.updateById(post);

        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @RequiresPermissions("admin:post:delete")
    public R delete(@RequestBody Integer[] ids){
        postService.deleteByAdmin(Arrays.asList(ids));
        return R.ok();
    }

}
