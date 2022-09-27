/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 商业版演示站点: https://www.linfeng.tech
 * 商业版购买联系技术客服
 * QQ:  3582996245
 * 可正常分享和学习源码，不得专卖或非法牟利！
 * Copyright (c) 2021-2023 linfeng all rights reserved.
 * 版权所有 ，侵权必究！
 * -----------------------------------
 */
package io.linfeng.modules.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.linfeng.common.exception.LinfengException;
import io.linfeng.common.response.*;
import io.linfeng.common.utils.*;
import io.linfeng.modules.admin.entity.PostEntity;
import io.linfeng.modules.admin.entity.SystemEntity;
import io.linfeng.modules.admin.service.*;
import io.linfeng.modules.app.dao.FollowDao;
import io.linfeng.modules.app.entity.FollowEntity;
import io.linfeng.modules.app.form.*;
import io.linfeng.modules.app.service.FollowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.modules.admin.dao.AppUserDao;
import io.linfeng.modules.admin.entity.AppUserEntity;

import javax.servlet.http.HttpServletRequest;


@Service
@Slf4j
public class AppUserServiceImpl extends ServiceImpl<AppUserDao, AppUserEntity> implements AppUserService {


    @Autowired
    private PostService postService;

    @Autowired
    private AppUserDao userDao;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private FollowService followService;

    @Autowired
    private FollowDao followDao;

    @Autowired
    private SystemService systemService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<AppUserEntity> queryWrapper = new QueryWrapper<>();
        //模糊查询
        String key = (String) params.get("key");
        if (!WechatUtil.isEmpty(key)) {
            params.put("page", "1");//如果是查询分页重置为第一页
            queryWrapper.like("username", key).or().like("mobile", key);
        }
        queryWrapper.lambda().orderByDesc(AppUserEntity::getUid);
        IPage<AppUserEntity> page = this.page(
                new Query<AppUserEntity>().getPage(params),
                queryWrapper
        );
        return new PageUtils(page);
    }


    @Override
    public void ban(Integer id) {
        Integer status = this.lambdaQuery().eq(AppUserEntity::getUid, id).one().getStatus();
        if (status.equals(Constant.USER_BANNER)) {
            throw new LinfengException("该用户已被禁用");
        }
        this.lambdaUpdate()
                .set(AppUserEntity::getStatus, 1)
                .eq(AppUserEntity::getUid, id)
                .update();
    }

    @Override
    public void openBan(Integer id) {
        Integer status = this.lambdaQuery().eq(AppUserEntity::getUid, id).one().getStatus();
        if (status.equals(Constant.USER_NORMAL)) {
            throw new LinfengException("该用户已解除禁用");
        }
        this.lambdaUpdate()
                .set(AppUserEntity::getStatus, 0)
                .eq(AppUserEntity::getUid, id)
                .update();
    }

    @Override
    public HomeRateResponse indexDate() {
        String today = cn.hutool.core.date.DateUtil.date().toString("yyyy-MM-dd");
        String yesterday = cn.hutool.core.date.DateUtil.yesterday().toString("yyyy-MM-dd");
//        Integer count = postService.lambdaQuery().eq(PostEntity::getStatus, Constant.POST_REVIEWED).count();
        Integer postCount = postService.lambdaQuery().select(PostEntity::getId).count();
        HomeRateResponse response = new HomeRateResponse();
        response.setTotalPostOfReview(0);
        response.setTotalPost(postCount);
        response.setNewUserNum(this.getRegisterNumByDate(today));
        response.setYesterdayNewUserNum(this.getRegisterNumByDate(yesterday));
        response.setTotalUser(this.getTotalNum());
        return response;
    }

    @Override
    public Integer smsLogin(SmsLoginForm form, HttpServletRequest request) {
        AppUserEntity appUserEntity = this.lambdaQuery().eq(AppUserEntity::getMobile, form.getMobile()).one();
        String codeKey = "code_" + form.getMobile();
        String s = redisUtils.get(codeKey);
        if (!s.equals(form.getCode())) {
            throw new LinfengException("验证码错误");
        }
        if (ObjectUtil.isNotNull(appUserEntity)) {
            //登录
            if (appUserEntity.getStatus() == 1) {
                throw new LinfengException("该账户已被禁用");
            }
            return appUserEntity.getUid();
        } else {
            //注册
            AppUserEntity appUser = new AppUserEntity();
            appUser.setMobile(form.getMobile());
            appUser.setGender(0);
            appUser.setAvatar(Constant.DEAULT_HEAD);
            appUser.setUsername("LF_" + RandomUtil.randomNumbers(8));
            appUser.setCreateTime(DateUtil.nowDateTime());
            appUser.setUpdateTime(DateUtil.nowDateTime());
            List<String> list = new ArrayList<>();
            list.add("新人");
            appUser.setTagStr(JSON.toJSONString(list));
            baseMapper.insert(appUser);
            AppUserEntity user = this.lambdaQuery().eq(AppUserEntity::getMobile, form.getMobile()).one();
            return user.getUid();
        }


    }

    @Override
    public String sendSmsCode(SendCodeForm param) {
        String code = RandomUtil.randomNumbers(6);
        String codeKey = "code_" + param.getMobile();
        String s = redisUtils.get(codeKey);
        if (ObjectUtil.isNotNull(s)) {
            return s;
        }
        redisUtils.set(codeKey, code, 60 * 5);
        return code;
    }

    @Override
    public AppUserResponse getUserInfo(AppUserEntity user) {

        AppUserResponse response = new AppUserResponse();
        BeanUtils.copyProperties(user, response);
        Integer follow = followService.getFollowCount(user.getUid());
        Integer fans = followService.getFans(user.getUid());
        Integer postNum = postService.getPostNumByUid(user.getUid());
        response.setFans(fans);
        response.setPostNum(postNum);
        response.setFollow(follow);
        return response;
    }

    @Override
    public void updateAppUserInfo(AppUserUpdateForm appUserUpdateForm, AppUserEntity user) {
        if (!WechatUtil.isEmpty(appUserUpdateForm.getAvatar())) {
            user.setAvatar(appUserUpdateForm.getAvatar());
        }
        baseMapper.updateById(user);
        redisUtils.delete("userId:" + user.getUid());
    }

    @Override
    public void addFollow(AddFollowForm request, AppUserEntity user) {
        if (request.getId().equals(user.getUid())) {
            throw new LinfengException("不能关注自己哦");
        }
        boolean isFollow = followService.isFollowOrNot(user.getUid(), request.getId());
        if (isFollow) {
            throw new LinfengException("不要重复关注哦");
        }
        FollowEntity followEntity = new FollowEntity();
        followEntity.setUid(user.getUid());
        followEntity.setFollowUid(request.getId());
        followEntity.setCreateTime(DateUtil.nowDateTime());
        followService.save(followEntity);
        //TODO 消息通知
    }

    @Override
    public void cancelFollow(AddFollowForm request, AppUserEntity user) {
        followDao.cancelFollow(user.getUid(), request.getId());
    }

    @Override
    public AppPageUtils userFans(Integer currPage, Integer uid) {
        List<Integer> uidList = followService.getFansList(uid);
        if (uidList.isEmpty()) {
            return new AppPageUtils(null, 0, 10, currPage);
        }
        Page<AppUserEntity> page = new Page<>(currPage, 10);
        QueryWrapper<AppUserEntity> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.lambda().in(AppUserEntity::getUid, uidList);
        Page<AppUserEntity> page1 = this.page(page, queryWrapper1);

        AppPageUtils pages = new AppPageUtils(page1);
        List<?> data = pages.getData();
        List<TopicUserResponse> responseList = new ArrayList<>();
        data.forEach(l -> {
            TopicUserResponse topicUserResponse = new TopicUserResponse();
            BeanUtils.copyProperties(l, topicUserResponse);
            Integer follow = followService.isFollow(uid, topicUserResponse.getUid());
            topicUserResponse.setHasFollow(follow);
            responseList.add(topicUserResponse);
        });
        pages.setData(responseList);
        return pages;
    }

    @Override
    public AppPageUtils follow(Integer currPage, AppUserEntity user) {
        List<Integer> followUids = followService.getFollowUids(user);
        if (followUids.isEmpty()) {
            return new AppPageUtils(null, 0, 10, currPage);
        }
        Page<AppUserEntity> page = new Page<>(currPage, 10);
        QueryWrapper<AppUserEntity> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.lambda().in(AppUserEntity::getUid, followUids);
        Page<AppUserEntity> page1 = this.page(page, queryWrapper1);


        AppPageUtils pages = new AppPageUtils(page1);
        List<?> data = pages.getData();
        List<TopicUserResponse> responseList = new ArrayList<>();
        data.forEach(l -> {
            TopicUserResponse topicUserResponse = new TopicUserResponse();
            BeanUtils.copyProperties(l, topicUserResponse);
            Integer follow = followService.isFollow(user.getUid(), topicUserResponse.getUid());
            topicUserResponse.setHasFollow(follow);
            responseList.add(topicUserResponse);
        });
        pages.setData(responseList);
        return pages;
    }

    @Override
    public AppUserInfoResponse findUserInfoById(Integer uid, AppUserEntity user) {
        AppUserEntity userEntity = this.getById(uid);
        AppUserInfoResponse response = new AppUserInfoResponse();
        BeanUtils.copyProperties(userEntity, response);
        return response;
    }

    @Override
    public Integer miniWxLogin(WxLoginForm form) {

        SystemEntity system = systemService.lambdaQuery().eq(SystemEntity::getConfig, "miniapp").one();

        //小程序唯一标识   (在微信小程序管理后台获取)
        String appId = system.getValue();
        //小程序的 app secret (在微信小程序管理后台获取)
        String secret = system.getExtend();
        //授权（必填）
        String grant_type = "authorization_code";
        //https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code
        //向微信服务器 使用登录凭证 code 获取 session_key 和 openid
        String params = "appid=" + appId + "&secret=" + secret + "&js_code=" + form.getCode() + "&grant_type=" + grant_type;
        //发送请求
        String sr = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
        //解析相应内容（转换成json对象）
        JSONObject json = JSON.parseObject(sr);
        //用户的唯一标识（openId）
        String openId = (String) json.get("openid");
        //根据openId获取数据库信息 判断用户是否登录
        AppUserEntity user = this.lambdaQuery().eq(AppUserEntity::getOpenid, openId).one();
        if (ObjectUtil.isNotNull(user)) {
            //已登录用户
            if (user.getStatus() == 1) {
                throw new LinfengException("该账户已被禁用");
            }
            //其他业务todo
            return user.getUid();
        } else {
            //新注册用户
            AppUserEntity appUser = new AppUserEntity();
            appUser.setGender(0);
            appUser.setAvatar(form.getAvatar());
            appUser.setUsername(form.getUsername());
            appUser.setCreateTime(DateUtil.nowDateTime());
            appUser.setUpdateTime(DateUtil.nowDateTime());
            appUser.setOpenid(openId);
            List<String> list = new ArrayList<>();
            list.add("新人");
            appUser.setTagStr(JSON.toJSONString(list));
            baseMapper.insert(appUser);
            AppUserEntity users = this.lambdaQuery().eq(AppUserEntity::getOpenid, openId).one();
            return users.getUid();
        }
    }


    private Integer getTotalNum() {
        return this.lambdaQuery().select(AppUserEntity::getUid).count();
    }

    private Integer getRegisterNumByDate(String date) {
        QueryWrapper<AppUserEntity> wrapper = Wrappers.query();
        wrapper.select("uid");
        wrapper.apply("date_format(create_time, '%Y-%m-%d') = {0}", date);
        return userDao.selectCount(wrapper);
    }

}