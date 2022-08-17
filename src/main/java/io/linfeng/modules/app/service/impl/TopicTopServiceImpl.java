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
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.Query;

import io.linfeng.modules.app.dao.TopicTopDao;
import io.linfeng.modules.app.entity.TopicTopEntity;
import io.linfeng.modules.app.service.TopicTopService;


@Service("topicTopService")
public class TopicTopServiceImpl extends ServiceImpl<TopicTopDao, TopicTopEntity> implements TopicTopService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TopicTopEntity> page = this.page(
                new Query<TopicTopEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<TopicTopEntity> findByTopicId(Integer topicId) {
        LambdaQueryWrapper<TopicTopEntity> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(TopicTopEntity::getTopicId, topicId);
        return baseMapper.selectList(lambdaQueryWrapper);
    }

}