package io.linfeng.modules.app.utils;

import io.jsonwebtoken.Claims;
import io.linfeng.common.utils.RedisUtils;
import io.linfeng.modules.admin.entity.AppUserEntity;
import io.linfeng.modules.admin.service.AppUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 获取登录用户信息，如果未登录返回空
 * @author linfeng
 * @date 2022/6/9 22:38
 */
@Component
public class LocalUser {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private AppUserService userService;


    public AppUserEntity getUser(){
        //获取request
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        //获取token
        String token = request.getHeader(jwtUtils.getHeader());
        if(StringUtils.isBlank(token)){
            token = request.getParameter(jwtUtils.getHeader());
        }
        //凭证为空
        if(StringUtils.isBlank(token)){
            return null;
        }
        Claims claims = jwtUtils.getClaimByToken(token);
        //这里要判空 不然会出现无效token干扰
        if(claims==null){
            return null;
        }
        long userId = Long.parseLong(claims.getSubject());
        AppUserEntity userInfo = redisUtils.get("userId:" + userId, AppUserEntity.class);
        if (userInfo != null) {
            return userInfo;
        }
        //重新获取用户信息
        AppUserEntity user = userService.getById(userId);
        redisUtils.set("userId:" + userId, user, 7200);
        return user;
    }
}