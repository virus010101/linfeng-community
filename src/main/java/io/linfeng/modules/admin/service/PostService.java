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

    void deleteByAdmin(List<Integer> integers);

    Integer getPostNumByUid(Integer uid);

    AppPageUtils lastPost(Integer page);

    AppPageUtils followUserPost(Integer page, AppUserEntity user);

    void addCollection(AddCollectionForm request, AppUserEntity user);

    AppPageUtils myPost(Integer page, AppUserEntity user);

    AppPageUtils myCollectPost(Integer page,AppUserEntity user);

    PostDetailResponse detail(Integer id);

    void addComment(AddCommentForm request, AppUserEntity user);

    Integer addPost(AddPostForm request, AppUserEntity user);

    AppPageUtils queryPageList(PostListForm request, AppUserEntity user);
}

