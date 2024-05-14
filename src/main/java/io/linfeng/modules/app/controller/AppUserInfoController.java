/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 可正常分享和学习源码，不得用于非法牟利！
 * 商业版购买联系技术客服 QQ: 3582996245
 * Copyright (c) 2021-2023 linfeng all rights reserved.
 * 演示站点:https://www.linfeng.tech
 * 版权所有，侵权必究！
 * -----------------------------------
 */
package io.linfeng.modules.app.controller;

import io.linfeng.common.vo.AppUserInfoResponse;
import io.linfeng.common.vo.AppUserRankResponse;
import io.linfeng.common.vo.AppUserResponse;
import io.linfeng.common.utils.AppPageUtils;
import io.linfeng.common.utils.R;
import io.linfeng.modules.admin.entity.AppUserEntity;
import io.linfeng.modules.admin.service.AppUserService;
import io.linfeng.modules.app.annotation.Login;
import io.linfeng.modules.app.annotation.LoginUser;
import io.linfeng.modules.app.param.*;
import io.linfeng.modules.app.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * APP登录接口
 *
 * @author linfeng
 * @date 2022/6/9 22:40
 */
@RestController
@RequestMapping("/app/user")
@Api(tags = "APP登录接口")
public class AppUserInfoController {


    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AppUserService appUserService;

    @Value("${sms.open}")
    private boolean isOpen;

    @PostMapping("/sendSmsCode")
    @ApiOperation("测试发送验证码")
    public R sendSmsCode(@RequestBody SendCodeForm param) {
        String code = appUserService.sendSmsCode(param);
        if (isOpen) {
            //TODO
            //send Aliyun Sms code
        }
        return R.ok("测试验证码:" + code);
    }


    /**
     * 手机验证码登录
     */
    @PostMapping("/smsLogin")
    @ApiOperation("手机验证码登录")
    public R smsLogin(@RequestBody SmsLoginForm form, HttpServletRequest request) {

        //用户登录
        Integer userId = appUserService.smsLogin(form, request);

        //生成token
        String token = jwtUtils.generateToken(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("expire", jwtUtils.getExpire());

        return R.ok(map);
    }

    /**
     * 微信小程序登录
     */
    @PostMapping("/miniWxlogin")
    @ApiOperation("手机验证码登录")
    public R miniWxLogin(@RequestBody WxLoginForm form) {

        //用户登录
        Integer userId = appUserService.miniWxLogin(form);

        //生成token
        String token = jwtUtils.generateToken(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("expire", jwtUtils.getExpire());

        return R.ok(map);
    }


    @Login
    @GetMapping("/userInfo")
    @ApiOperation("获取用户信息")
    public R userInfo(@ApiIgnore @LoginUser AppUserEntity user) {

        AppUserResponse response = appUserService.getUserInfo(user);
        return R.ok().put("result", response);
    }

    @Login
    @PostMapping("/cancelFollow")
    @ApiOperation("取消关注用户")
    public R cancelFollow(@ApiIgnore @LoginUser AppUserEntity user,
                          @RequestBody AddFollowForm request) {
        appUserService.cancelFollow(request, user);
        return R.ok("取消关注用户成功");
    }

    @Login
    @PostMapping("/userInfoEdit")
    @ApiOperation("用户修改个人信息")
    public R userInfoEdit(@ApiIgnore  @LoginUser AppUserEntity user, @RequestBody AppUserUpdateForm appUserUpdateForm) {
        appUserService.updateAppUserInfo(appUserUpdateForm, user);
        return R.ok("修改成功");
    }


    @Login
    @PostMapping("/addFollow")
    @ApiOperation("关注用户")
    public R addFollow(@ApiIgnore  @LoginUser AppUserEntity user, @RequestBody AddFollowForm request) {
        appUserService.addFollow(request, user);
        return R.ok("关注用户成功");
    }




    @Login
    @GetMapping("/userFans")
    @ApiOperation("我的粉丝分页列表")
    public R userFans(@RequestParam("page") Integer page, @ApiIgnore @LoginUser AppUserEntity user) {

        AppPageUtils pages = appUserService.userFans(page, user.getUid());
        return R.ok().put("result", pages);
    }

    @Login
    @GetMapping("/follow")
    @ApiOperation("我的关注分页列表")
    public R follow(@RequestParam("page") Integer page,
                    @ApiIgnore @LoginUser AppUserEntity user) {

        AppPageUtils pages = appUserService.follow(page, user);
        return R.ok().put("result", pages);
    }


    @Login
    @PostMapping("/userInfoById")
    @ApiOperation("用户个人主页信息")
    public R userInfoById(@RequestBody AppUserInfoForm request,
                          @ApiIgnore @LoginUser AppUserEntity user) {
        AppUserInfoResponse response = appUserService.findUserInfoById(request.getUid(), user);

        return R.ok().put("result", response);
    }



    @PostMapping("/userRank")
    @ApiOperation("发帖达人列表")
    public R userRank() {
        List<AppUserRankResponse> list=appUserService.userRank();
        return R.ok().put("result",list);
    }

}
