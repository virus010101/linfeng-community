package io.linfeng.modules.app.service.impl;

import org.springframework.stereotype.Service;

import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.Query;

import io.linfeng.modules.app.dao.UserTopicDao;
import io.linfeng.modules.app.entity.UserTopicEntity;
import io.linfeng.modules.app.service.UserTopicService;


@Service("userTopicService")
public class UserTopicServiceImpl extends ServiceImpl<UserTopicDao, UserTopicEntity> implements UserTopicService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserTopicEntity> page = this.page(
                new Query<UserTopicEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

}