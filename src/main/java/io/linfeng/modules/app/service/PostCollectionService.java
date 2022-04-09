package io.linfeng.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.modules.admin.entity.AppUserEntity;
import io.linfeng.modules.app.entity.PostCollectionEntity;
import io.linfeng.modules.app.form.AddCollectionForm;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author linfeng
 * @email 3582996245@qq.com
 * @date 2022-01-24 20:49:32
 */
public interface PostCollectionService extends IService<PostCollectionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Integer collectCount(Integer postId);

    Boolean isCollection(Integer uid,Integer postId);

    void cancelCollection(AddCollectionForm request, AppUserEntity user);

    List<Integer> getPostListByUid(Integer uid);
}

