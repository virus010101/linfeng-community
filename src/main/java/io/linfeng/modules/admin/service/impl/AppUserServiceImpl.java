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

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.linfeng.common.enums.GenderStatus;
import io.linfeng.common.exception.LinfengException;
import io.linfeng.common.vo.*;
import io.linfeng.common.utils.*;
import io.linfeng.modules.admin.entity.PostEntity;
import io.linfeng.modules.admin.entity.SystemEntity;
import io.linfeng.modules.admin.service.*;
import io.linfeng.modules.app.dao.FollowDao;
import io.linfeng.modules.app.entity.FollowEntity;
import io.linfeng.modules.app.param.*;
import io.linfeng.modules.app.service.FollowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.modules.admin.dao.AppUserDao;
import io.linfeng.modules.admin.entity.AppUserEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;


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

    @Autowired
    private CommentService commentService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<AppUserEntity> queryWrapper = new QueryWrapper<>();
        //模糊查询
        String key = (String) params.get("key");
        if (!ObjectUtil.isEmpty(key)) {
            queryWrapper.lambda()
                    .like(AppUserEntity::getUsername, key)
                    .or()
                    .like(AppUserEntity::getMobile, key);
        }
        queryWrapper.lambda().orderByDesc(AppUserEntity::getUid);
        IPage<AppUserEntity> page = this.page(
                new Query<AppUserEntity>().getPage(params),
                queryWrapper
        );
        return new PageUtils(page);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ban(Integer id) {
        Integer status = this.lambdaQuery().eq(AppUserEntity::getUid, id).one().getStatus();
        if(status.equals(Constant.USER_BANNER)){
            throw new LinfengException(Constant.USER_BANNER_MSG);
        }
        boolean update = this.lambdaUpdate()
                .set(AppUserEntity::getStatus, Constant.USER_BANNER)
                .set(AppUserEntity::getUpdateTime, new Date())
                .eq(AppUserEntity::getUid, id)
                .update();
        if(!update){
            throw new LinfengException("封禁失败");
        }
        redisUtils.delete(RedisKeys.getUserKey(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void openBan(Integer id) {
        Integer status = this.lambdaQuery().eq(AppUserEntity::getUid, id).one().getStatus();
        if (status.equals(Constant.USER_NORMAL)) {
            throw new LinfengException(Constant.USER_BANNER_MSG);
        }
        boolean update = this.lambdaUpdate()
                .set(AppUserEntity::getStatus, Constant.USER_NORMAL)
                .set(AppUserEntity::getUpdateTime,new Date())
                .eq(AppUserEntity::getUid, id)
                .update();
        if(!update){
            throw new LinfengException("解除失败");
        }
        redisUtils.delete(RedisKeys.getUserKey(id));
    }

    @Override
    public HomeRateResponse indexDate() {
        String today = cn.hutool.core.date.DateUtil.date().toString("yyyy-MM-dd");
        String yesterday = cn.hutool.core.date.DateUtil.yesterday().toString("yyyy-MM-dd");

        HomeRateResponse response = new HomeRateResponse();
        response.setTotalPostOfReview(0);
        response.setTotalPost(this.getTotalPostNum());
        response.setNewUserNum(this.getRegisterNumByDate(today));
        response.setYesterdayNewUserNum(this.getRegisterNumByDate(yesterday));
        response.setTotalUser(this.getTotalUserNum());
        response.setYesterdayCommentCount(commentService.getYesterdayCount());
        response.setCommentCount(commentService.getAllCount());
        return response;
    }

    /**
     * 注册/登录
     * @param form 手机验证码登录dto
     * @param request
     * @return 用户ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer smsLogin(SmsLoginForm form, HttpServletRequest request) {
        AppUserEntity appUserEntity = this.lambdaQuery()
                .eq(AppUserEntity::getMobile, form.getMobile())
                .one();
        String codeKey = Constant.SMS_PREFIX + form.getMobile();
        String s = redisUtils.get(codeKey);
        if (io.linfeng.common.utils.ObjectUtil.isEmpty(s)) {
            throw new LinfengException("请先发送验证码");
        }
        if (!s.equals(form.getCode())) {
            throw new LinfengException("验证码错误");
        }
        String ip = IPUtils.getIpAddr(request);
        String cityInfo = IPUtils.getCityInfo(ip);
        if (ObjectUtil.isNotNull(appUserEntity)) {
            //登录
            if (appUserEntity.getStatus().equals(Constant.USER_BANNER)) {
                throw new LinfengException(Constant.USER_BANNER_MSG,Constant.USER_BANNER_CODE);
            }
            appUserEntity.setCity(cityInfo);
            this.updateById(appUserEntity);
            return appUserEntity.getUid();
        } else {
            //注册
            AppUserEntity appUser = new AppUserEntity();
            appUser.setMobile(form.getMobile());
            appUser.setGender(GenderStatus.UNKNOWN.getValue());
            appUser.setAvatar(Constant.DEAULT_HEAD);
            appUser.setUsername(generateRandomName(Constant.H5));
            appUser.setCreateTime(DateUtil.nowDateTime());
            appUser.setUpdateTime(DateUtil.nowDateTime());
            appUser.setCity(cityInfo);
            List<String> list = new ArrayList<>();
            list.add(Constant.DEFAULT_TAG);
            appUser.setTagStr(JSON.toJSONString(list));
            baseMapper.insert(appUser);
            AppUserEntity user = this.lambdaQuery()
                    .eq(AppUserEntity::getMobile, form.getMobile())
                    .one();
            if(ObjectUtil.isNull(user)){
                throw new LinfengException("注册失败");
            }
            //其他业务处理
            return user.getUid();
        }


    }

    @Override
    public String sendSmsCode(SendCodeForm param) {
        String code = RandomUtil.randomNumbers(Constant.SMS_SIZE);
        String codeKey = Constant.SMS_PREFIX + param.getMobile();
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
    @Transactional(rollbackFor = Exception.class)
    public void updateAppUserInfo(AppUserUpdateForm appUserUpdateForm, AppUserEntity user) {
        if (!ObjectUtil.isEmpty(appUserUpdateForm.getAvatar())) {
            user.setAvatar(appUserUpdateForm.getAvatar());
        }
        if(!ObjectUtil.isEmpty(appUserUpdateForm.getGender())){
            user.setGender(appUserUpdateForm.getGender());
        }
        if(!ObjectUtil.isEmpty(appUserUpdateForm.getUsername())){
            user.setUsername(appUserUpdateForm.getUsername());
        }
        if(!ObjectUtil.isEmpty(appUserUpdateForm.getIntro())){
            user.setIntro(appUserUpdateForm.getIntro());
        }
        baseMapper.updateById(user);
        redisUtils.delete(RedisKeys.getUserKey(user.getUid()));
    }

    @Override
    public void addFollow(AddFollowForm request, AppUserEntity user) {
//        if (request.getId().equals(user.getUid())) {
//            throw new LinfengException("不能关注自己哦");
//        }
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
        // 获取用户的粉丝ID列表
        List<Integer> fansUidList = followService.getFansList(uid);
        
        // 如果没有粉丝，直接返回空结果
        if (fansUidList.isEmpty()) {
            return new AppPageUtils(new ArrayList<>(), 0, 20, currPage);
        }
        
        // 分页查询粉丝用户信息
        Page<AppUserEntity> page = new Page<>(currPage, 20);
        Page<AppUserEntity> userPage = this.lambdaQuery()
                .in(AppUserEntity::getUid, fansUidList)
                .page(page);
        
        // 转换为响应对象并设置关注状态
        List<AppUserBasicInfoResponse> responseList = userPage.getRecords().stream()
                .map(userEntity -> {
                    AppUserBasicInfoResponse response = new AppUserBasicInfoResponse();
                    BeanUtils.copyProperties(userEntity, response);
                    response.setHasFollow(followService.isFollow(uid, response.getUid()));
                    return response;
                })
                .collect(Collectors.toList());
        
        // 构建分页响应
        AppPageUtils result = new AppPageUtils(userPage);
        result.setData(responseList);
        return result;
    }

    @Override
    public AppPageUtils followList(Integer currPage, AppUserEntity user) {
        // 获取用户关注的用户ID列表
        List<Integer> followUidList = followService.getFollowUids(user);
        
        // 如果没有关注任何用户，直接返回空结果
        if (followUidList.isEmpty()) {
            return new AppPageUtils(new ArrayList<>(), 0, 20, currPage);
        }
        
        // 分页查询关注的用户信息
        Page<AppUserEntity> page = new Page<>(currPage, 20);
        Page<AppUserEntity> userPage = this.lambdaQuery()
                .in(AppUserEntity::getUid, followUidList)
                .page(page);
        
        // 转换为响应对象并设置关注状态
        List<AppUserBasicInfoResponse> responseList = userPage.getRecords().stream()
                .map(userEntity -> {
                    AppUserBasicInfoResponse response = new AppUserBasicInfoResponse();
                    BeanUtils.copyProperties(userEntity, response);
                    response.setHasFollow(followService.isFollow(user.getUid(), response.getUid()));
                    return response;
                })
                .collect(Collectors.toList());
        
        // 构建分页响应
        AppPageUtils result = new AppPageUtils(userPage);
        result.setData(responseList);
        return result;
    }

    @Override
    public AppUserInfoResponse findUserInfoById(Integer uid, AppUserEntity user) {
        if(uid==0){
            uid=user.getUid();
        }
        AppUserEntity userEntity = this.getById(uid);
        if(ObjectUtil.isNull(userEntity)){
            throw new LinfengException("用户不存在");
        }
        AppUserInfoResponse response = new AppUserInfoResponse();
        BeanUtils.copyProperties(userEntity, response);
        response.setFans(followService.getFans(userEntity.getUid()));
        response.setFollow(followService.getFollowCount(userEntity.getUid()));
        response.setIsFollow(followService.isFollowOrNot(user.getUid(), uid));
        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer miniWxLogin(WxLoginForm form) {

        String openId = getOpenId(form.getCode());
        if(io.linfeng.common.utils.ObjectUtil.isEmpty(openId)){
            throw new LinfengException("请在后台管理端配置appId和密钥");
        }
        //根据openId获取数据库信息 判断用户是否登录
        AppUserEntity user = this.lambdaQuery()
                .eq(AppUserEntity::getOpenid, openId)
                .one();
        if (ObjectUtil.isNotNull(user)) {
            if(user.getStatus().equals(Constant.USER_BANNER)){
                throw new LinfengException(Constant.USER_BANNER_MSG,Constant.USER_BANNER_CODE);
            }
            //其他业务todo
            return user.getUid();
        } else {
            //新注册用户
            AppUserEntity appUser = new AppUserEntity();
            appUser.setGender(GenderStatus.UNKNOWN.getValue());
            appUser.setAvatar(Constant.DEAULT_HEAD);
            appUser.setUsername(generateRandomName(Constant.WXAPP));
            appUser.setCreateTime(DateUtil.nowDateTime());
            appUser.setUpdateTime(DateUtil.nowDateTime());
            appUser.setOpenid(openId);
            List<String> list = new ArrayList<>();
            list.add(Constant.DEFAULT_TAG);
            appUser.setTagStr(JSON.toJSONString(list));
            baseMapper.insert(appUser);
            AppUserEntity users = this.lambdaQuery()
                    .eq(AppUserEntity::getOpenid, openId)
                    .one();
            if(ObjectUtil.isNull(users)){
                throw new LinfengException("注册失败");
            }
            //其他业务todo
            return users.getUid();
        }
    }

    /**
     * 查询本周发帖活跃用户
     * @return 发帖达人列表
     */
    @Override
    public List<AppUserRankResponse> userRank() {
        DateTime week = cn.hutool.core.date.DateUtil.beginOfWeek(new Date());

        List<PostEntity> postList = postService.lambdaQuery()
                .gt(PostEntity::getCreateTime, week)
                .list();
        if(postList.isEmpty()){
            return new ArrayList<>();
        }
        Map<Integer, Long> collect = postList.stream().collect(Collectors.groupingBy(PostEntity::getUid, Collectors.counting()));
        Map<Integer, Long> sorted = collect
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                        LinkedHashMap::new));
        List<Integer> userIdList = new ArrayList<>(sorted.keySet());
        List<AppUserEntity> batchUser = userDao.getBatchUser(userIdList);
        Map<Integer, AppUserEntity> userMap = batchUser.stream()
                .collect(Collectors.toMap(AppUserEntity::getUid, Function.identity()));
        List<AppUserRankResponse> list=new ArrayList<>();
        sorted.forEach((k,v)->{
            AppUserEntity appUser = userMap.get(k);
            if(appUser!=null){
                AppUserRankResponse response=new AppUserRankResponse();
                BeanUtils.copyProperties(appUser,response);
                response.setPostNumber(v.intValue());
                list.add(response);
            }
        });
        return list;
    }


    @Override
    public List<AppUserEntity> getBatchUser(List<Integer> uid) {
        if(uid.isEmpty()){
            return new ArrayList<>();
        }
        return userDao.getBatchUser(uid);
    }

    private Integer getTotalUserNum() {
        return this.lambdaQuery().select(AppUserEntity::getUid).count();
    }

    private Integer getTotalPostNum() {
        return postService.lambdaQuery().select(PostEntity::getId).count();
    }

    private Integer getRegisterNumByDate(String date) {
        QueryWrapper<AppUserEntity> wrapper = Wrappers.query();
        wrapper.select("uid");
        wrapper.apply("date_format(create_time, '%Y-%m-%d') = {0}", date);
        return userDao.selectCount(wrapper);
    }

    private String getOpenId(String code){
        SystemEntity system = systemService.lambdaQuery()
                .eq(SystemEntity::getConfig, ConfigConstant.MINIAPP)
                .one();
        if(system == null){
            throw new LinfengException("后台配置项不存在");
        }
        String appId = system.getValue();
        String secret = system.getExtend();
        String grant_type = "authorization_code";
        String params = "appid=" + appId + "&secret=" + secret + "&js_code=" + code + "&grant_type=" + grant_type;
        String res = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
        JSONObject json = JSON.parseObject(res);
        return (String) json.get("openid");
    }


    /**
     * 生成随机用户名
     * @param type 终端类型
     * @return 随机用户名
     */
    private String generateRandomName(String type){
        String name;
        if(type.equals(Constant.H5)){
            name = "LF_" + RandomUtil.randomNumbers(8);
        }else if(type.equals(Constant.WXAPP)){
            name = "LF_wx" + RandomUtil.randomNumbers(8);
        }else{
            name = "LF_" + RandomUtil.randomNumbers(6);
        }
        return name;
    }

}