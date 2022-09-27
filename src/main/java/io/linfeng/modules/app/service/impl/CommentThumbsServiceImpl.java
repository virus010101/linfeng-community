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

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.linfeng.common.exception.LinfengException;
import io.linfeng.common.utils.DateUtil;
import io.linfeng.modules.admin.entity.AppUserEntity;
import io.linfeng.modules.app.form.AddThumbsForm;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.Query;

import io.linfeng.modules.app.dao.CommentThumbsDao;
import io.linfeng.modules.app.entity.CommentThumbsEntity;
import io.linfeng.modules.app.service.CommentThumbsService;


@Service("commentThumbsService")
public class CommentThumbsServiceImpl extends ServiceImpl<CommentThumbsDao, CommentThumbsEntity> implements CommentThumbsService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CommentThumbsEntity> page = this.page(
                new Query<CommentThumbsEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public Boolean isThumbs(Integer uid, Long id) {
        CommentThumbsEntity one = baseMapper.selectOne(new LambdaQueryWrapper<CommentThumbsEntity>()
                .eq(CommentThumbsEntity::getCId, id)
                .eq(CommentThumbsEntity::getUid, uid));

        return one!=null;
    }

    @Override
    public Integer getThumbsCount(Long id) {
        return this.lambdaQuery()
                .eq(CommentThumbsEntity::getCId, id)
                .count();
    }

    @Override
    public void addThumbs(AddThumbsForm request, AppUserEntity user) {
        CommentThumbsEntity one=baseMapper.selectOne(new LambdaQueryWrapper<CommentThumbsEntity>()
        .eq(CommentThumbsEntity::getCId,request.getId())
        .eq(CommentThumbsEntity::getUid,user.getUid()));
        if(ObjectUtil.isNotNull(one)){
            throw new LinfengException("请勿重复点赞");
        }
        CommentThumbsEntity ct=new CommentThumbsEntity();
        ct.setUid(user.getUid());
        ct.setCId(request.getId());
        ct.setCreateTime(DateUtil.nowDateTime());
        this.save(ct);
    }

    @Override
    public void cancelThumbs(AddThumbsForm request, AppUserEntity user) {
        baseMapper.delete(new LambdaQueryWrapper<CommentThumbsEntity>()
                .eq(CommentThumbsEntity::getCId,request.getId())
                .eq(CommentThumbsEntity::getUid,user.getUid()));
    }


}