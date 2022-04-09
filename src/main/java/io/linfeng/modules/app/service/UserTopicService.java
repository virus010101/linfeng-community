package io.linfeng.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.modules.app.entity.UserTopicEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author linfeng
 * @email 3582996245@qq.com
 * @date 2022-01-23 21:24:46
 */
public interface UserTopicService extends IService<UserTopicEntity> {

    PageUtils queryPage(Map<String, Object> params);

}

