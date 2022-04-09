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

import io.linfeng.modules.admin.entity.TopicEntity;
import io.linfeng.modules.admin.service.TopicService;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.R;



/**
 * 
 *
 * @author linfeng
 * @email 2445465217@qq.com
 * @date 2022-01-21 17:01:12
 */
@RestController
@RequestMapping("admin/topic")
public class TopicController {
    @Autowired
    private TopicService topicService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("admin:topic:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = topicService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("admin:topic:info")
    public R info(@PathVariable("id") Integer id){
		TopicEntity topic = topicService.getById(id);

        return R.ok().put("topic", topic);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("admin:topic:save")
    public R save(@RequestBody TopicEntity topic){
		topicService.save(topic);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("admin:topic:update")
    public R update(@RequestBody TopicEntity topic){
		topicService.updateById(topic);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("admin:topic:delete")
    public R delete(@RequestBody Integer[] ids){
		topicService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
