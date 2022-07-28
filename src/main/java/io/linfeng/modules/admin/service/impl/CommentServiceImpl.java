package io.linfeng.modules.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.linfeng.common.response.AppCommentResponse;
import io.linfeng.common.utils.AppPageUtils;
import io.linfeng.modules.admin.entity.AppUserEntity;
import io.linfeng.modules.admin.service.AppUserService;
import io.linfeng.modules.app.service.CommentThumbsService;
import io.linfeng.modules.app.utils.LocalUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
import org.springframework.transaction.annotation.Transactional;


@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentDao, CommentEntity> implements CommentService {

    @Autowired
    private LocalUser localUser;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private CommentThumbsService commentThumbsService;

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
        return baseMapper.selectCount(new LambdaQueryWrapper<CommentEntity>()
                .eq(CommentEntity::getStatus,1)
                .eq(CommentEntity::getPostId, id));
    }

    @Override
    public List<CommentEntity> getByPid(Long pid) {
        return baseMapper.selectList(
                new LambdaQueryWrapper<CommentEntity>()
                        .eq(CommentEntity::getPid, pid));
    }


    /**
     * 管理端批量删除评论
     * @param list
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByAdmin(List<Long> list) {
        list.forEach(id->{
            this.removeById(id);
            //子评论更改展示状态为屏蔽
            this.lambdaUpdate()
                    .set(CommentEntity::getStatus, 0)
                    .eq(CommentEntity::getPid, id)
                    .update();
        });
    }


    @Override
    public Integer getCountByPostId(Integer id) {
        return baseMapper.selectCount(
                new LambdaQueryWrapper<CommentEntity>()
                        .eq(CommentEntity::getStatus, 1)
                        .eq(CommentEntity::getPostId, id));
    }

    @Override
    public AppPageUtils queryCommentPage(Integer postId, Integer page) {
        Page<CommentEntity> commentPage = new Page<>(page,10);
        QueryWrapper<CommentEntity> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().eq(CommentEntity::getPostId,postId);
        queryWrapper.lambda().eq(CommentEntity::getStatus,1);
        Page<CommentEntity> pages = baseMapper.selectPage(commentPage,queryWrapper);
        AppPageUtils appPage=new AppPageUtils(pages);
        List<CommentEntity> data = (List<CommentEntity>) appPage.getData();
        List<AppCommentResponse> responseList=new ArrayList<>();
        AppUserEntity user = localUser.getUser();
        data.forEach(l->{
            if(l.getPid()==0){
                AppCommentResponse response=new AppCommentResponse();
                BeanUtils.copyProperties(l,response);
                response.setUserInfo(appUserService.getById(response.getUid()));
                response.setThumbs(commentThumbsService.getThumbsCount(l.getId()));
                if(user==null){
                    response.setIsThumbs(false);
                }else{
                    response.setIsThumbs(commentThumbsService.isThumbs(user.getUid(),l.getId()));
                }
                responseList.add(response);
            }

        });
        appPage.setData(responseList);
        return appPage;
    }
}