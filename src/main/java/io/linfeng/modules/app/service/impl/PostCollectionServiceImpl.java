/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 可正常分享和学习源码，不得用于非法牟利！
 * 商业版购买联系技术客服 QQ: 3582996245
 * Copyright (c) 2021-2023 linfeng all rights reserved.
 * 演示站点:https://www.linfeng.tech
 * 版权所有，侵权必究！
 * -----------------------------------
 */
package io.linfeng.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.linfeng.modules.admin.entity.AppUserEntity;
import io.linfeng.modules.app.param.AddCollectionForm;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.Query;

import io.linfeng.modules.app.dao.PostCollectionDao;
import io.linfeng.modules.app.entity.PostCollectionEntity;
import io.linfeng.modules.app.service.PostCollectionService;


@Service("postCollectionService")
public class PostCollectionServiceImpl extends ServiceImpl<PostCollectionDao, PostCollectionEntity> implements PostCollectionService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PostCollectionEntity> page = this.page(
                new Query<PostCollectionEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public Integer collectCount(Integer postId) {

        return this.lambdaQuery()
                .eq(PostCollectionEntity::getPostId,postId)
                .count();
    }

    @Override
    public Boolean isCollection(Integer uid, Integer postId) {
        PostCollectionEntity entity = baseMapper.selectOne(
                new LambdaQueryWrapper<PostCollectionEntity>()
                .eq(PostCollectionEntity::getPostId, postId)
                .eq(PostCollectionEntity::getUid, uid));
        return Optional.ofNullable(entity).isPresent();
    }

    @Override
    public void cancelCollection(AddCollectionForm request, AppUserEntity user) {
        baseMapper.delete(new LambdaQueryWrapper<PostCollectionEntity>()
                .eq(PostCollectionEntity::getPostId,request.getId())
                .eq(PostCollectionEntity::getUid,user.getUid()));
    }

    @Override
    public List<Integer> getPostListByUid(Integer uid) {
        LambdaQueryWrapper<PostCollectionEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(PostCollectionEntity::getPostId);
        lambdaQueryWrapper.in(PostCollectionEntity::getUid, uid);
        List<PostCollectionEntity> postCollectionEntities = baseMapper.selectList(lambdaQueryWrapper);
        return postCollectionEntities.stream().map(PostCollectionEntity::getPostId).collect(Collectors.toList());
    }

}