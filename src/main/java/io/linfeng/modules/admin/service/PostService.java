package io.linfeng.modules.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.linfeng.common.response.PostDetailResponse;
import io.linfeng.common.utils.AppPageUtils;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.modules.admin.entity.AppUserEntity;
import io.linfeng.modules.admin.entity.PostEntity;
import io.linfeng.modules.app.form.*;
import io.swagger.models.auth.In;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author linfeng
 * @email 3582996245@qq.com
 * @date 2022-01-23 20:49:55
 */
public interface PostService extends IService<PostEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Integer findTopicPostCount(Integer topicId);

    List<String> findThreeMedia(Integer id);

    void deleteByAdmin(List<Integer> integers);
}

