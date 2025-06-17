/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 商业版详情查看: https://www.linfengtech.cn
 * 商业版购买联系技术客服
 * QQ:  3582996245
 * 可正常分享和学习源码，不得转卖或非法牟利！
 * Copyright (c) 2021-2025 linfeng all rights reserved.
 * 版权所有 ，侵权必究！
 * -----------------------------------
 */
package io.linfeng.modules.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.linfeng.common.exception.LinfengException;
import io.linfeng.common.vo.AppUserInfoShortResponse;
import io.linfeng.common.vo.PostDetailResponse;
import io.linfeng.common.vo.PostListResponse;
import io.linfeng.common.utils.*;
import io.linfeng.modules.admin.entity.*;
import io.linfeng.modules.admin.service.*;
import io.linfeng.modules.app.entity.PostCollectionEntity;
import io.linfeng.modules.app.param.*;
import io.linfeng.modules.app.service.*;
import io.linfeng.modules.app.utils.LocalUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.linfeng.modules.admin.dao.PostDao;
import org.springframework.transaction.annotation.Transactional;


@Service("postService")
public class PostServiceImpl extends ServiceImpl<PostDao, PostEntity> implements PostService {


    @Autowired
    private AppUserService appUserService;
    @Autowired
    private PostCollectionService postCollectionService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LocalUser localUser;
    @Autowired
    private FollowService followService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PostEntity> queryWrapper=new QueryWrapper<>();
        String key = (String)params.get("key");
        String status = (String)params.get("status");
        queryWrapper.lambda()
                .like(!ObjectUtil.isEmpty(status),PostEntity::getContent, key)
                .or()
                .like(!ObjectUtil.isEmpty(status),PostEntity::getTitle,key);
        queryWrapper.lambda().orderByDesc(PostEntity::getId);
        IPage<PostEntity> page = this.page(
                new Query<PostEntity>().getPage(params),
                queryWrapper
        );
        List<PostEntity> data = page.getRecords();

        List<PostListResponse> responseList=new ArrayList<>();
        data.forEach(l->{
            PostListResponse response=new PostListResponse();
            BeanUtils.copyProperties(l,response);
            response.setCollectionCount(postCollectionService.collectCount(response.getId()));
            response.setCommentCount(commentService.getCountByPostId(response.getId()));
            AppUserEntity userInfo = appUserService.getById(response.getUid());
            AppUserInfoShortResponse userInfoVo=new AppUserInfoShortResponse();
            BeanUtils.copyProperties(userInfo,userInfoVo);
            response.setUserInfo(userInfoVo);
            response.setMedia(JsonUtils.JsonToList(l.getMedia()));
            responseList.add(response);
        });
        PageUtils pageUtils=new PageUtils(page);
        pageUtils.setList(responseList);
        return pageUtils;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByAdmin(List<Integer> ids) {

        boolean remove = this.removeByIds(ids);
        if (!remove) {
            throw new LinfengException("批量删除失败");
        }
    }

    @Override
    public Integer getPostNumByUid(Integer uid) {
        return this.lambdaQuery()
                .eq(PostEntity::getUid,uid)
                .count();
    }

    @Override
    public AppPageUtils lastPost(Integer currPage) {
        Page<PostEntity> page=new Page<>(currPage,10);
        QueryWrapper<PostEntity> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("post_top","id");
        AppUserEntity user = localUser.getUser();
        if(user == null){
            return this.mapPostList(page,queryWrapper,0);
        }
        return this.mapPostList(page,queryWrapper,user.getUid());
    }

    @Override
    public AppPageUtils followUserPost(Integer page, AppUserEntity user) {
        List<Integer> list=followService.getFollowUid(user);
        if(list.isEmpty()){
            return null;
        }
        QueryWrapper<PostEntity> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().in(PostEntity::getUid,list);
        queryWrapper.orderByDesc("post_top","id");
        Page<PostEntity> pages=new Page<>(page,10);
        return this.mapPostList(pages,queryWrapper,user.getUid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCollection(AddCollectionForm request, AppUserEntity user) {
        Boolean collection = postCollectionService.isCollection(user.getUid(), request.getId());
        if(collection){
            throw new LinfengException("请勿重复点赞");
        }
        PostCollectionEntity pc=new PostCollectionEntity();
        pc.setPostId(request.getId());
        pc.setUid(user.getUid());
        postCollectionService.save(pc);
        //TODO 消息通知

    }

    @Override
    public AppPageUtils myPost(Integer page, AppUserEntity user) {
        QueryWrapper<PostEntity> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().eq(PostEntity::getUid,user.getUid());
        queryWrapper.lambda().orderByDesc(PostEntity::getId);
        Page<PostEntity> pages = new Page<>(page,10);
        return this.mapPostList(pages, queryWrapper, user.getUid());
    }

    @Override
    public AppPageUtils myCollectPost(Integer page,AppUserEntity user) {
        List<Integer> postIdList=postCollectionService.getPostListByUid(user.getUid());
        if(postIdList.size()==0){
            return new AppPageUtils(null, 0, 10, 1);
        }
        QueryWrapper<PostEntity> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().in(PostEntity::getId,postIdList);
        queryWrapper.lambda().orderByDesc(PostEntity::getId);
        Page<PostEntity> pages = new Page<>(page,10);
        return this.mapPostList(pages, queryWrapper, user.getUid());
    }

    @Override
    public PostDetailResponse detail(Integer id) {
        PostEntity post = this.getById(id);
        if(ObjectUtil.isNull(post)){
            throw new LinfengException("该帖子不存在或已删除");
        }
        updatePv(post);
        PostDetailResponse response=new PostDetailResponse();
        BeanUtils.copyProperties(post,response);
        AppUserEntity userInfo = appUserService.getById(post.getUid());
        AppUserInfoShortResponse userInfoVo=new AppUserInfoShortResponse();
        BeanUtils.copyProperties(userInfo,userInfoVo);
        response.setUserInfo(userInfoVo);
        AppUserEntity user = localUser.getUser();
        if(ObjectUtil.isNull(user)){
            response.setIsFollow(false);
            response.setIsCollection(false);
        }else{
            response.setIsFollow(followService.isFollowOrNot(user.getUid(), post.getUid()));
            response.setIsCollection(postCollectionService.isCollection(user.getUid(),post.getId()));
        }
        response.setCollectionCount(postCollectionService.collectCount(post.getId()));
        response.setCommentCount(commentService.getCountByPostId(post.getId()));
        response.setMedia(JsonUtils.JsonToList(post.getMedia()));//文件处理

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addComment(AddCommentForm request, AppUserEntity user) {
        checkUserStatus(user);
        CommentEntity commentEntity=new CommentEntity();
        BeanUtils.copyProperties(request,commentEntity);
        commentEntity.setCreateTime(DateUtil.nowDateTime());
        commentEntity.setUid(user.getUid().longValue());
        commentService.save(commentEntity);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer addPost(AddPostForm request, AppUserEntity user) {
        checkUserStatus(user);
        PostEntity post=new PostEntity();
        BeanUtils.copyProperties(request,post);
        post.setMedia(JSON.toJSONString(request.getMedia()));
        post.setUid(user.getUid());
        post.setCreateTime(DateUtil.nowDateTime());
        if(this.save(post)){
            return post.getId();
        }
        return 0;
    }

    @Override
    public AppPageUtils queryPageList(PostListForm request, AppUserEntity user) {
        AppPageUtils appPage;
        Page<PostEntity> page = new Page<>(request.getPage(), 10);
        QueryWrapper<PostEntity> queryWrapper = new QueryWrapper<>();
        if(request.getClassId()!=null){
            if(request.getClassId()==0){
                queryWrapper.lambda().orderByDesc(PostEntity::getReadCount);
                appPage = this.mapPostList(page, queryWrapper, 0);
            }else{
                queryWrapper.lambda().eq(PostEntity::getCut,request.getClassId());
                queryWrapper.lambda().orderByDesc(PostEntity::getId);
                appPage = this.mapPostList(page, queryWrapper, 0);
            }
        }else{
            if (ObjectUtil.isNotNull(request.getOrder())) {
                if (request.getOrder().equals(Constant.ORDER_DESC_READCOUNT)) {
                    queryWrapper.lambda().orderByDesc(PostEntity::getReadCount);
                } else if (request.getOrder().equals(Constant.ORDER_DESC_ID)) {
                    queryWrapper.lambda().orderByDesc(PostEntity::getId);
                }
            } else {
                queryWrapper.orderByDesc("post_top","id");
            }

            if (ObjectUtil.isNotNull(request.getUid())) {
                if(request.getUid()==0){
                    queryWrapper.lambda().eq(PostEntity::getUid, user.getUid());
                }else{
                    queryWrapper.lambda().eq(PostEntity::getUid, request.getUid());
                }
                appPage = this.mapPostList(page, queryWrapper, request.getUid());
            } else {
                appPage = this.mapPostList(page, queryWrapper, user.getUid());
            }
        }


        return appPage;
    }

    @Override
    public void deleteMyPost(DeletePostForm request, AppUserEntity user) {
        PostEntity post = this.getById(request.getId());
        if(post==null){
            throw new LinfengException("帖子不存在");
        }
        if(!post.getUid().equals(user.getUid())){
            throw new LinfengException("不能删除别人帖子");
        }
        this.removeById(request.getId());
        //关联业务处理todo
    }

    /**
     * 更新浏览量
     * @param post 帖子
     */
    void updatePv(PostEntity post){
        post.setReadCount(post.getReadCount()+1);
        baseMapper.updateById(post);
    }

    /**
     * 检查用户状态
     * @param user 用户
     */
    private void checkUserStatus(AppUserEntity user){
        if(user.getStatus().equals(Constant.USER_BANNER)){
            throw new LinfengException(Constant.USER_BANNER_MSG,Constant.USER_BANNER_CODE);
        }
    }

    /**
     * 组装帖子分页
     * @param page 分页参数
     * @param queryWrapper 查询条件
     * @param uid 用户id，未登录传0
     * @return 组装后的分页数据
     */
    public AppPageUtils mapPostList(Page<PostEntity> page, QueryWrapper<PostEntity> queryWrapper, Integer uid) {
        Page<PostEntity> pages = baseMapper.selectPage(page, queryWrapper);
        AppPageUtils appPage = new AppPageUtils(pages);

        List<PostEntity> posts = (List<PostEntity>) appPage.getData();
        if (posts == null || posts.isEmpty()) {
            appPage.setData(Collections.emptyList());
            return appPage;
        }

        // 批量获取用户信息
        Map<Integer, AppUserEntity> userMap = buildUserMap(posts);

        // 转换为响应对象
        List<PostListResponse> responseList = posts.stream()
                .map(post -> buildPostResponse(post, userMap, uid))
                .collect(Collectors.toList());

        appPage.setData(responseList);
        return appPage;
    }

    /**
     * 构建用户映射
     */
    private Map<Integer, AppUserEntity> buildUserMap(List<PostEntity> posts) {
        List<Integer> uidList = posts.stream()
                .map(PostEntity::getUid)
                .distinct()
                .collect(Collectors.toList());

        List<AppUserEntity> appUserList = appUserService.getBatchUser(uidList);
        return appUserList.stream()
                .collect(Collectors.toMap(AppUserEntity::getUid, Function.identity()));
    }

    /**
     * 构建帖子响应对象
     */
    private PostListResponse buildPostResponse(PostEntity post, Map<Integer, AppUserEntity> userMap, Integer uid) {
        PostListResponse response = new PostListResponse();
        BeanUtils.copyProperties(post, response);

        // 设置统计信息
        response.setCollectionCount(postCollectionService.collectCount(post.getId()));
        response.setCommentCount(commentService.getCountByPostId(post.getId()));

        // 设置用户信息
        AppUserEntity userInfo = userMap.get(post.getUid());
        if (userInfo != null) {
            AppUserInfoShortResponse userInfoVo=new AppUserInfoShortResponse();
            BeanUtils.copyProperties(userInfo,userInfoVo);
            response.setUserInfo(userInfoVo);
        }

        // 设置点赞状态
        boolean isCollection = uid != 0 && postCollectionService.isCollection(uid, post.getId());
        response.setIsCollection(isCollection);

        // 文件信息转化
        response.setMedia(JsonUtils.JsonToList(post.getMedia()));

        return response;
    }
}