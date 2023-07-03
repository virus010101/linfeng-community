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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.linfeng.modules.admin.entity.AppUserEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.Query;
import io.linfeng.modules.app.dao.FollowDao;
import io.linfeng.modules.app.entity.FollowEntity;
import io.linfeng.modules.app.service.FollowService;


@Service("followService")
public class FollowServiceImpl extends ServiceImpl<FollowDao, FollowEntity> implements FollowService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<FollowEntity> page = this.page(
                new Query<FollowEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public Integer getFollowCount(Integer uid) {
        return this.lambdaQuery()
                .eq(FollowEntity::getUid, uid)
                .count();
    }

    @Override
    public Integer getFans(Integer uid) {

        return this.lambdaQuery()
                .eq(FollowEntity::getFollowUid,uid)
                .count();
    }

    @Override
    public boolean isFollowOrNot(Integer uid, Integer id) {
        LambdaQueryWrapper<FollowEntity> queryWrapper= Wrappers.lambdaQuery();
        queryWrapper.eq(FollowEntity::getUid,uid);
        queryWrapper.eq(FollowEntity::getFollowUid,id);
        return baseMapper.selectCount(queryWrapper) != 0;
    }

    @Override
    public List<Integer> getFollowUid(AppUserEntity user) {
        List<FollowEntity> list = this.lambdaQuery().eq(FollowEntity::getUid, user.getUid()).list();
        return list.stream().map(FollowEntity::getFollowUid).collect(Collectors.toList());
    }

    @Override
    public Integer isFollow(Integer uid, Integer followUid) {
        LambdaQueryWrapper<FollowEntity> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(FollowEntity::getUid, uid);
        lambdaQueryWrapper.eq(FollowEntity::getFollowUid, followUid);
        if(baseMapper.selectCount(lambdaQueryWrapper) == 0){
            return 0;
        }
        LambdaQueryWrapper<FollowEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(FollowEntity::getUid, followUid);
        wrapper.eq(FollowEntity::getFollowUid, uid);
        return baseMapper.selectCount(wrapper) == 0 ? 2 : 1;
    }

    @Override
    public List<Integer> getFansList(Integer uid) {
        List<FollowEntity> list = this.lambdaQuery()
                .eq(FollowEntity::getFollowUid, uid)
                .orderByDesc(FollowEntity::getId)
                .list();
        if(list.isEmpty()){
            return new ArrayList<>();
        }
        return list.stream().map(FollowEntity::getUid).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getFollowUids(AppUserEntity user) {
        List<FollowEntity> list = this.lambdaQuery()
                .eq(FollowEntity::getUid, user.getUid())
                .list();
        return list.stream().map(FollowEntity::getFollowUid).collect(Collectors.toList());
    }


}