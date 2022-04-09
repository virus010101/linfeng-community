package io.linfeng.modules.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.linfeng.common.response.TopicDetailResponse;
import io.linfeng.common.utils.AppPageUtils;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.modules.admin.entity.AppUserEntity;
import io.linfeng.modules.admin.entity.TopicEntity;
import io.linfeng.modules.app.form.TopicAddForm;
import io.linfeng.modules.app.form.TopicUpdateForm;
import io.linfeng.modules.app.form.UserJoinTopicForm;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author linfeng
 * @email 3582996245@qq.com
 * @date 2022-01-21 17:01:12
 */
public interface TopicService extends IService<TopicEntity> {

    PageUtils queryPage(Map<String, Object> params);

    AppPageUtils queryByPage(Map<String, Object> params);


}

