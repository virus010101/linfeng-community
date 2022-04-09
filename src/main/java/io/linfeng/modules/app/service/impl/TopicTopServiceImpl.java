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
                new QueryWrapper<TopicTopEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<TopicTopEntity> findByTopicId(Integer topicId) {
        LambdaQueryWrapper<TopicTopEntity> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(TopicTopEntity::getTopicId, topicId);
        List<TopicTopEntity> list = baseMapper.selectList(lambdaQueryWrapper);
        return list;
    }

}