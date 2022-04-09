package io.linfeng.modules.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.linfeng.modules.admin.service.AppUserService;
import io.linfeng.modules.app.service.CommentThumbsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.Query;

import io.linfeng.modules.admin.dao.CommentDao;
import io.linfeng.modules.admin.entity.CommentEntity;
import io.linfeng.modules.admin.service.CommentService;


@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentDao, CommentEntity> implements CommentService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CommentEntity> page = this.page(
                new Query<CommentEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public Integer getCountByTopicId(Integer id) {
        Integer count = baseMapper.selectCount(new LambdaQueryWrapper<CommentEntity>()
                .eq(CommentEntity::getStatus,1)
                .eq(CommentEntity::getPostId, id));

        return count;
    }

    @Override
    public List<CommentEntity> getByPid(Long pid) {
        List<CommentEntity> commentList = baseMapper.selectList(
                new LambdaQueryWrapper<CommentEntity>()
                        .eq(CommentEntity::getPid, pid));
        return commentList;
    }


}