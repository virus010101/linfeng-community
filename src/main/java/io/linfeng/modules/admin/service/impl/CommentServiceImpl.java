/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 商业版详情查看: https://www.linfeng.tech
 * 商业版购买联系技术客服
 * QQ:  3582996245
 * 可正常分享和学习源码，不得转卖或非法牟利！
 * Copyright (c) 2021-2023 linfeng all rights reserved.
 * 版权所有 ，侵权必究！
 * -----------------------------------
 */
package io.linfeng.modules.admin.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.linfeng.common.utils.Constant;
import io.linfeng.common.vo.AppCommentResponse;
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
        QueryWrapper<CommentEntity> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(CommentEntity::getId);
        IPage<CommentEntity> page = this.page(
                new Query<CommentEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
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
                    .set(CommentEntity::getStatus,  Constant.COMMENT_DOWN)
                    .eq(CommentEntity::getPid, id)
                    .update();
        });
    }


    @Override
    public Integer getCountByPostId(Integer id) {
        return this.lambdaQuery()
                .eq(CommentEntity::getStatus,  Constant.COMMENT_NORMAL)
                .eq(CommentEntity::getPostId, id)
                .count();
    }

    @Override
    public AppPageUtils queryCommentPage(Integer postId, Integer page) {
        Page<CommentEntity> commentPage = new Page<>(page,10);
        QueryWrapper<CommentEntity> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().eq(CommentEntity::getPostId,postId);
        queryWrapper.lambda().eq(CommentEntity::getStatus, Constant.COMMENT_NORMAL);
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

    /**
     * 获取昨天评论数
     * @return
     */
    @Override
    public Integer getYesterdayCount() {
        DateTime yesterday = DateUtil.yesterday();
        return this.lambdaQuery()
                .ge(CommentEntity::getCreateTime,yesterday)
                .eq(CommentEntity::getStatus,  Constant.COMMENT_NORMAL)
                .count();
    }

    @Override
    public Integer getAllCount() {
        return this.lambdaQuery()
                .eq(CommentEntity::getStatus, Constant.COMMENT_NORMAL)
                .count();
    }

}