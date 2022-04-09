package io.linfeng.modules.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.Query;

import io.linfeng.modules.admin.dao.DiscussDao;
import io.linfeng.modules.admin.entity.DiscussEntity;
import io.linfeng.modules.admin.service.DiscussService;


@Service("discussService")
public class DiscussServiceImpl extends ServiceImpl<DiscussDao, DiscussEntity> implements DiscussService {



    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<DiscussEntity> page = this.page(
                new Query<DiscussEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<DiscussEntity> getListByTopicId(Integer topicId) {
        QueryWrapper<DiscussEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(DiscussEntity::getTopicId, topicId);
        Page<DiscussEntity> pageModel = new Page<>(1, 5);
        IPage<DiscussEntity> pageList = this.baseMapper.selectPage(pageModel, wrapper);
        return pageList.getRecords();
    }



}