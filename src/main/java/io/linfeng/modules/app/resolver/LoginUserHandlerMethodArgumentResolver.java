
package io.linfeng.modules.app.resolver;

import io.linfeng.common.utils.RedisUtils;
import io.linfeng.modules.admin.entity.AppUserEntity;
import io.linfeng.modules.admin.service.AppUserService;
import io.linfeng.modules.app.annotation.LoginUser;
import io.linfeng.modules.app.interceptor.AuthorizationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 有@LoginUser注解的方法参数，注入当前登录用户
 */
@Component
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {


    @Autowired
    private AppUserService userService;

    @Autowired
    private RedisUtils redisUtils;


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(AppUserEntity.class) && parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
                                  NativeWebRequest request, WebDataBinderFactory factory) throws Exception {
        //获取用户ID
        Object object = request.getAttribute(AuthorizationInterceptor.USER_KEY, RequestAttributes.SCOPE_REQUEST);
        if (object == null) {
            return null;
        }
        AppUserEntity userInfo = redisUtils.get("userId:" + object, AppUserEntity.class);
        if (userInfo != null) {
            return userInfo;
        }
        //重新获取用户信息
        AppUserEntity user = userService.getById((Long) object);
        redisUtils.set("userId:" + object, user, 7200);
        return user;
    }
}
