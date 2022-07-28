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
        return this.lambdaQuery().eq(FollowEntity::getUid, uid).count();
    }

    @Override
    public Integer getFans(Integer uid) {

        return this.lambdaQuery().eq(FollowEntity::getFollowUid,uid).count();
    }

    @Override
    public boolean isFollowOrNot(Integer uid, Integer id) {
        LambdaQueryWrapper<FollowEntity> queryWrapper= Wrappers.lambdaQuery();
        queryWrapper.eq(FollowEntity::getUid,uid);
        queryWrapper.eq(FollowEntity::getFollowUid,id);
        Integer num = baseMapper.selectCount(queryWrapper);
        if(num==0){
            return false;
        }
        return true;
    }

    @Override
    public List<Integer> getFollowUid(AppUserEntity user) {
        List<FollowEntity> list = this.lambdaQuery().eq(FollowEntity::getUid, user.getUid()).list();
        List<Integer> collect = list.stream().map(FollowEntity::getFollowUid).collect(Collectors.toList());
        return collect;
    }

    @Override
    public Integer isFollow(Integer uid, Integer followUid) {
        LambdaQueryWrapper<FollowEntity> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(FollowEntity::getUid, uid);
        lambdaQueryWrapper.eq(FollowEntity::getFollowUid, followUid);
        Integer num = baseMapper.selectCount(lambdaQueryWrapper);
        if(num == 0){
            return 0;
        }
        LambdaQueryWrapper<FollowEntity> lambdaQueryWrapper2 = Wrappers.lambdaQuery();
        lambdaQueryWrapper2.eq(FollowEntity::getUid, followUid);
        lambdaQueryWrapper2.eq(FollowEntity::getFollowUid, uid);
        Integer num2 = baseMapper.selectCount(lambdaQueryWrapper2);
        if(num2==0){
            return 2;
        }
        return 1;
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
        List<Integer> collect = list.stream().map(FollowEntity::getUid).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<Integer> getFollowUids(AppUserEntity user) {
        List<FollowEntity> list = this.lambdaQuery().eq(FollowEntity::getUid, user.getUid()).list();
        List<Integer> collect = list.stream().map(FollowEntity::getFollowUid).collect(Collectors.toList());
        return collect;
    }


}