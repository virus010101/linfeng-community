/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 商业版详情查看: https://www.linfengtech.cn
 * 商业版购买联系技术客服QQ:  3582996245
 * 可正常分享和学习源码，不得转卖或非法牟利！
 * Copyright (c) 2021-2026 linfeng all rights reserved.
 * 版权所有 ，侵权必究！
 * -----------------------------------
 */
package io.linfeng.common.aspect;

import io.linfeng.common.annotation.NoRepeatSubmit;
import io.linfeng.common.exception.LinfengException;
import io.linfeng.modules.app.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 接口防重复提交处理
 *
 */
@Aspect
@Component
@Slf4j
@SuppressWarnings("all")
public class RepeatSubmitAspect {

    public static final String KEYPREX = "noRepeat:user:";

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 进行接口防重复提交处理
     *
     * @param pjp            切点
     * @param noRepeatSubmit  防重复注解
     * @return
     */
    @Around("(execution(* io.linfeng.modules.app.controller.*.*(..)) || execution(* io.linfeng.modules.admin.controller.*.*(..))) && @annotation(noRepeatSubmit)")
    public Object around(ProceedingJoinPoint pjp, NoRepeatSubmit noRepeatSubmit) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        StringBuilder sb = new StringBuilder();
        String token = request.getHeader(jwtUtils.getHeader());
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(jwtUtils.getHeader());
        }
        sb.append(KEYPREX).append(token).append(request.getRequestURI().toString());

        long now = System.currentTimeMillis();
        if (redisTemplate.hasKey(sb.toString())) {
            long lastTime = Long.parseLong(redisTemplate.opsForValue().get(sb.toString()).toString());
            if ((now - lastTime) > noRepeatSubmit.lockTime()) {
                redisTemplate.opsForValue().set(sb.toString(), String.valueOf(now), 10, TimeUnit.MINUTES);
                Object result = pjp.proceed();
                return result;
            } else {
                throw new LinfengException("点太快啦，稍后再试");
            }
        } else {
            redisTemplate.opsForValue().set(sb.toString(), String.valueOf(now), 10, TimeUnit.MINUTES);
            Object result = pjp.proceed();
            return result;
        }
    }
}
