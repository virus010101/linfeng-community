package io.linfeng.modules.admin.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.linfeng.common.exception.LinfengException;
import io.linfeng.modules.admin.entity.PostEntity;
import io.linfeng.modules.admin.service.PostService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.linfeng.modules.admin.entity.DiscussEntity;
import io.linfeng.modules.admin.service.DiscussService;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.R;



/**
 * 
 *
 * @author linfeng
 * @email 2445465217@qq.com
 * @date 2022-01-24 13:44:37
 */
@RestController
@RequestMapping("admin/discuss")
public class DiscussController {

    @Autowired
    private DiscussService discussService;
    @Autowired
    private PostService postService;


    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("admin:discuss:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = discussService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("admin:discuss:info")
    public R info(@PathVariable("id") Integer id){
		DiscussEntity discuss = discussService.getById(id);

        return R.ok().put("discuss", discuss);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("admin:discuss:save")
    public R save(@RequestBody DiscussEntity discuss){
		discussService.save(discuss);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("admin:discuss:update")
    public R update(@RequestBody DiscussEntity discuss){
		discussService.updateById(discuss);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("admin:discuss:delete")
    public R delete(@RequestBody Integer[] ids){
        List<Integer> list = Arrays.asList(ids);
        list.forEach(id->{
            List<PostEntity> entityList = postService.lambdaQuery().eq(PostEntity::getDiscussId, id).list();
            if(entityList.size()>0){
                throw new LinfengException("请先删除该话题下的帖子");
            }
            discussService.removeById(id);
        });
        return R.ok();
    }

}
