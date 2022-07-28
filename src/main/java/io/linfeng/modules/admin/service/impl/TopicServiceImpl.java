package io.linfeng.modules.admin.service.impl;

import io.linfeng.common.utils.*;
import io.linfeng.modules.admin.service.*;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.linfeng.modules.admin.dao.TopicDao;
import io.linfeng.modules.admin.entity.TopicEntity;


@Service("topicService")
public class TopicServiceImpl extends ServiceImpl<TopicDao, TopicEntity> implements TopicService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TopicEntity> page = this.page(
                new Query<TopicEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }


    @Override
    public AppPageUtils queryByPage(Map<String, Object> params) {
        Integer classId = Integer.valueOf((String) params.get(Constant.CLASSID));
        QueryWrapper<TopicEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(TopicEntity::getCateId, classId);

        IPage<TopicEntity> page = this.page(
                new Query<TopicEntity>().getPage(params),
                queryWrapper
        );
        return new AppPageUtils(page);
    }


}