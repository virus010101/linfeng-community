package io.linfeng.modules.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.linfeng.common.response.AppUserInfoResponse;
import io.linfeng.common.response.AppUserResponse;
import io.linfeng.common.response.HomeRateResponse;
import io.linfeng.common.utils.AppPageUtils;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.modules.admin.entity.AppUserEntity;
import io.linfeng.modules.admin.entity.MessageEntity;
import io.linfeng.modules.app.form.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author linfeng
 * @email 3582996245@qq.com
 * @date 2022-01-20 12:10:43
 */
public interface AppUserService extends IService<AppUserEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void ban(Integer id);

    void openBan(Integer id);

    /**
     * 首页数据
     * @return HomeRateResponse
     */
    HomeRateResponse indexDate();
}

