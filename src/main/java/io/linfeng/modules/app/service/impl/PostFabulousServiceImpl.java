package io.linfeng.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.Query;

import io.linfeng.modules.app.dao.PostFabulousDao;
import io.linfeng.modules.app.entity.PostFabulousEntity;
import io.linfeng.modules.app.service.PostFabulousService;


@Service("postFabulousService")
public class PostFabulousServiceImpl extends ServiceImpl<PostFabulousDao, PostFabulousEntity> implements PostFabulousService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PostFabulousEntity> page = this.page(
                new Query<PostFabulousEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public Boolean isThumb(Integer uid, Integer id) {
        PostFabulousEntity entity = baseMapper.selectOne(
                new LambdaQueryWrapper<PostFabulousEntity>()
                        .eq(PostFabulousEntity::getPostId, id)
                        .eq(PostFabulousEntity::getUid, uid));
        return entity != null;
    }

}