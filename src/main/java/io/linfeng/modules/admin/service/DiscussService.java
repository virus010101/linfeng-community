package io.linfeng.modules.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.linfeng.common.response.DiscussDetailResponse;
import io.linfeng.common.utils.AppPageUtils;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.modules.admin.entity.AppUserEntity;
import io.linfeng.modules.admin.entity.DiscussEntity;
import io.linfeng.modules.app.form.DiscussAddForm;
import io.linfeng.modules.app.form.DiscussDeleteForm;
import io.linfeng.modules.app.form.DiscussListForm;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author linfeng
 * @email 3582996245@qq.com
 * @date 2022-01-24 13:44:37
 */
public interface DiscussService extends IService<DiscussEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<DiscussEntity> getListByTopicId(Integer topicId);

}

