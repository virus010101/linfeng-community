package io.linfeng.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.modules.app.entity.TopicAdminEntity;

import java.util.Map;

/**
 * 
 *
 * @author linfeng
 * @email 3582996245@qq.com
 * @date 2022-01-23 20:33:29
 */
public interface TopicAdminService extends IService<TopicAdminEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Boolean isAdmin(Integer uid, Integer id);
}

