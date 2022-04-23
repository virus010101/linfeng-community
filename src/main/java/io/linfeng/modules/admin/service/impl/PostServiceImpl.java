package io.linfeng.modules.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.linfeng.common.exception.LinfengException;
import io.linfeng.common.response.PostDetailResponse;
import io.linfeng.common.response.PostListResponse;
import io.linfeng.common.response.TopicDetailResponse;
import io.linfeng.common.utils.*;
import io.linfeng.modules.admin.entity.*;
import io.linfeng.modules.admin.service.*;
import io.linfeng.modules.app.entity.PostCollectionEntity;
import io.linfeng.modules.app.entity.TopicAdminEntity;
import io.linfeng.modules.app.entity.UserTopicEntity;
import io.linfeng.modules.app.form.*;
import io.linfeng.modules.app.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private TopicService topicService;
    @Autowired
    private PostCollectionService postCollectionService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private DiscussService discussService;
    @Autowired
    private MessageService messageService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PostEntity> page = this.page(
                new Query<PostEntity>().getPage(params),
                new QueryWrapper<>()
        );
        List<PostEntity> data = page.getRecords();

        List<PostListResponse> responseList=new ArrayList<>();
        data.forEach(l->{
            PostListResponse response=new PostListResponse();
            BeanUtils.copyProperties(l,response);
            if(response.getDiscussId()>0){
                DiscussEntity discussEntity = discussService.getById(response.getDiscussId());
                response.setDiscussTitle(discussEntity.getTitle());
            }
            response.setTopicName(topicService.getById(response.getTopicId()).getTopicName());
            response.setCollectionCount(postCollectionService.collectCount(response.getId()));
            response.setCommentCount(commentService.getCountByTopicId(response.getId()));
            response.setUserInfo(appUserService.getById(response.getUid()));
            String jsonString = l.getMedia();
            List<String> list = JsonUtils.JsonToList(jsonString);
            response.setMedia(list);
            responseList.add(response);
        });
        PageUtils pageUtils=new PageUtils(page);
        pageUtils.setList(responseList);
        return pageUtils;
    }

    @Override
    public Integer findTopicPostCount(Integer topicId) {
        LambdaQueryWrapper<PostEntity> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(PostEntity::getTopicId,topicId);
        return baseMapper.selectCount(lambdaQueryWrapper);
    }

    /**
     * 选取圈子中热度最高的三条动态的首图作为展示
     * @param id
     * @return
     */
    @Override
    public List<String> findThreeMedia(Integer id) {
        QueryWrapper<PostEntity> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("topic_id",id);
        queryWrapper.eq("type",1);
        queryWrapper.orderByDesc("read_count");
        queryWrapper.last("limit 10");
        List<PostEntity> postEntityList = baseMapper.selectList(queryWrapper);
        List<String> imageList=new ArrayList<>();
        for (int i = 0; i < postEntityList.size() ; i++) {
            if(!postEntityList.get(i).getMedia().equals("")){
                List<String> jsonToList = JsonUtils.JsonToList(postEntityList.get(i).getMedia());
                if(jsonToList.size()>0){
                    if(imageList.size()>2){
                        break;
                    }else{
                        imageList.add(jsonToList.get(0));
                    }
                }
            }
        }
        return imageList;
    }


    @Override
    @Transactional
    public void deleteByAdmin(List<Integer> ids) {
        //通知用户消息违规被删除了
        ids.forEach(postId->{
            PostEntity post = this.getById(postId);
            String content = StrUtil.format(Constant.ADMIN_POST_DOWN,post.getTitle());
            messageService.sendMessage(0,post.getUid(),postId,Constant.PUSHARTICLE,content,Constant.TITLE_VIOLATION);
        });
        boolean remove = this.removeByIds(ids);
        if(!remove){
            throw new LinfengException("批量删除失败");
        }
    }

}