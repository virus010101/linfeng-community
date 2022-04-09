package io.linfeng.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.modules.app.entity.TopicTopEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author linfeng
 * @email 3582996245@qq.com
 * @date 2022-01-23 20:33:29
 */
public interface TopicTopService extends IService<TopicTopEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<TopicTopEntity> findByTopicId(Integer id);
}

