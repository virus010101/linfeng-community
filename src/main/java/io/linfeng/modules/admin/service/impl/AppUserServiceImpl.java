package io.linfeng.modules.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.linfeng.common.exception.LinfengException;
import io.linfeng.common.response.HomeRateResponse;
import io.linfeng.common.utils.*;
import io.linfeng.modules.admin.entity.PostEntity;
import io.linfeng.modules.admin.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.modules.admin.dao.AppUserDao;
import io.linfeng.modules.admin.entity.AppUserEntity;


@Service
public class AppUserServiceImpl extends ServiceImpl<AppUserDao, AppUserEntity> implements AppUserService {


    @Autowired
    private PostService postService;

    @Autowired
    private AppUserDao userDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<AppUserEntity> queryWrapper=new QueryWrapper<>();
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
        if(status==1){
            throw new LinfengException("该用户已被禁用");
        }
        this.lambdaUpdate()
                .set(AppUserEntity::getStatus,1)
                .eq(AppUserEntity::getUid,id)
                .update();
    }

    @Override
    public void openBan(Integer id) {
        Integer status = this.lambdaQuery().eq(AppUserEntity::getUid, id).one().getStatus();
        if(status==0){
            throw new LinfengException("该用户已解除禁用");
        }
        this.lambdaUpdate()
                .set(AppUserEntity::getStatus,0)
                .eq(AppUserEntity::getUid,id)
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