package io.linfeng.modules.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.linfeng.common.utils.Constant;
import io.linfeng.common.utils.DateUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.Query;

import io.linfeng.modules.admin.dao.MessageDao;
import io.linfeng.modules.admin.entity.MessageEntity;
import io.linfeng.modules.admin.service.MessageService;
import org.springframework.transaction.annotation.Transactional;


@Service("messageService")
public class MessageServiceImpl extends ServiceImpl<MessageDao, MessageEntity> implements MessageService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MessageEntity> page = this.page(
                new Query<MessageEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    /**
     * 消息异步发送
     * @param fromUid 发送者uid
     * @param toUid  接收者uid
     * @param postId 帖子id
     * @param type 1为点赞，2为评论  3为收藏 4为关注  5为推送文章 6私聊
     * @param content  发送内容
     * @param title 发送标题
     */
    public void sendMessage(Integer fromUid,Integer toUid,Integer postId,Integer type,String content,String title){
        if(fromUid.equals(toUid)){
            return;
        }
        MessageEntity message=new MessageEntity();
        message.setContent(content);
        message.setPostId(postId);
        message.setFromUid(fromUid);
        message.setTitle(title);
        message.setCreateTime(DateUtil.nowDateTime());
        message.setToUid(toUid);
        message.setType(type);
        this.save(message);
    }

    /**
     * 消息同步发送 用于用户消息私聊
     * @param fromUid 发送者uid
     * @param toUid  接收者uid
     * @param postId 帖子id
     * @param type 1为点赞，2为评论  3为收藏 4为关注  5为推送文章 6私聊
     * @param content  发送内容
     * @param title 发送标题
     */
    @Transactional(rollbackFor = Exception.class)
    public void sendMessageNotAsync(Integer fromUid,Integer toUid,Integer postId,Integer type,String content,String title){
        if(fromUid.equals(toUid)){
            return;
        }
        MessageEntity message=new MessageEntity();
        message.setContent(content);
        message.setPostId(postId);
        message.setFromUid(fromUid);
        message.setTitle(title);
        message.setCreateTime(DateUtil.nowDateTime());
        message.setToUid(toUid);
        message.setType(type);
        this.save(message);
    }

    @Override
    public Boolean status(Integer type, Integer uid) {
        List<Integer> list = new ArrayList<>();
        list.add(Constant.STAR);
        list.add(Constant.COLLECT);
        UpdateWrapper<MessageEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status",1);
        updateWrapper.eq("to_uid",uid);
        if(type==1){
            updateWrapper.in("type",list);
        }else if(type==2){
            updateWrapper.eq("type",Constant.WATCH);
        }else if(type==3){
            updateWrapper.eq("type",Constant.COMMENT);
        }else if(type==5){
            updateWrapper.eq("type",Constant.PUSHARTICLE);
        }else if(type==6){
            updateWrapper.eq("type",Constant.CHAT);
        }
        return update(updateWrapper);
    }


}