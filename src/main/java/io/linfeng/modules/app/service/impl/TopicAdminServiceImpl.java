package io.linfeng.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.Query;

import io.linfeng.modules.app.dao.TopicAdminDao;
import io.linfeng.modules.app.entity.TopicAdminEntity;
import io.linfeng.modules.app.service.TopicAdminService;


@Service("topicAdminService")
public class TopicAdminServiceImpl extends ServiceImpl<TopicAdminDao, TopicAdminEntity> implements TopicAdminService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TopicAdminEntity> page = this.page(
                new Query<TopicAdminEntity>().getPage(params),
                new QueryWrapper<TopicAdminEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public Boolean isAdmin(Integer uid, Integer id) {
        LambdaQueryWrapper<TopicAdminEntity> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(TopicAdminEntity::getUid, uid);
        lambdaQueryWrapper.eq(TopicAdminEntity::getTopicId, id);
        Integer num = baseMapper.selectCount(lambdaQueryWrapper);
        return num > 0;
    }

}