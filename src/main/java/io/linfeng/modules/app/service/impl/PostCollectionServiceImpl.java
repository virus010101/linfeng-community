package io.linfeng.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.linfeng.modules.admin.entity.AppUserEntity;
import io.linfeng.modules.app.form.AddCollectionForm;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.Query;

import io.linfeng.modules.app.dao.PostCollectionDao;
import io.linfeng.modules.app.entity.PostCollectionEntity;
import io.linfeng.modules.app.service.PostCollectionService;


@Service("postCollectionService")
public class PostCollectionServiceImpl extends ServiceImpl<PostCollectionDao, PostCollectionEntity> implements PostCollectionService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PostCollectionEntity> page = this.page(
                new Query<PostCollectionEntity>().getPage(params),
                new QueryWrapper<PostCollectionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public Integer collectCount(Integer postId) {
        Integer count = baseMapper.selectCount(new LambdaQueryWrapper<PostCollectionEntity>()
                .eq(PostCollectionEntity::getPostId, postId));

        return count;
    }

    @Override
    public Boolean isCollection(Integer uid, Integer postId) {
        PostCollectionEntity entity = baseMapper.selectOne(
                new LambdaQueryWrapper<PostCollectionEntity>()
                .eq(PostCollectionEntity::getPostId, postId)
                .eq(PostCollectionEntity::getUid, uid));
        if(entity!=null){
            return true;
        }
        return false;
    }

    @Override
    public void cancelCollection(AddCollectionForm request, AppUserEntity user) {
        baseMapper.delete(new LambdaQueryWrapper<PostCollectionEntity>()
                .eq(PostCollectionEntity::getPostId,request.getId())
                .eq(PostCollectionEntity::getUid,user.getUid()));
    }

    @Override
    public List<Integer> getPostListByUid(Integer uid) {
        LambdaQueryWrapper<PostCollectionEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(PostCollectionEntity::getPostId);
        lambdaQueryWrapper.in(PostCollectionEntity::getUid, uid);
        List<PostCollectionEntity> postCollectionEntities = baseMapper.selectList(lambdaQueryWrapper);
        List<Integer> collect = postCollectionEntities.stream().map(PostCollectionEntity::getPostId).collect(Collectors.toList());
        return collect;
    }

}