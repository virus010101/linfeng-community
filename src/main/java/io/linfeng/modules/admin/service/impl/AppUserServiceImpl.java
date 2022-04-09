package io.linfeng.modules.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.linfeng.common.exception.LinfengException;
import io.linfeng.common.utils.*;
import io.linfeng.modules.admin.service.*;
import org.springframework.stereotype.Service;
import java.util.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.modules.admin.dao.AppUserDao;
import io.linfeng.modules.admin.entity.AppUserEntity;


@Service
public class AppUserServiceImpl extends ServiceImpl<AppUserDao, AppUserEntity> implements AppUserService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AppUserEntity> page = this.page(
                new Query<AppUserEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }


    @Override
    public void ban(Integer id) {
        Integer status = this.lambdaQuery().eq(AppUserEntity::getUid, id).one().getStatus();
        if(status==1){
            throw new LinfengException("该用户已被禁用");
        }
        UpdateWrapper<AppUserEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status",1);
        updateWrapper.eq("uid",id);
        update(updateWrapper);
    }

    @Override
    public void openBan(Integer id) {
        Integer status = this.lambdaQuery().eq(AppUserEntity::getUid, id).one().getStatus();
        if(status==0){
            throw new LinfengException("该用户已解除禁用");
        }
        UpdateWrapper<AppUserEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status",0);
        updateWrapper.eq("uid",id);
        update(updateWrapper);
    }


}