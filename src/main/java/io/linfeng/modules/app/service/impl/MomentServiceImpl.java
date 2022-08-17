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
package io.linfeng.modules.app.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.Query;

import io.linfeng.modules.app.dao.MomentDao;
import io.linfeng.modules.app.entity.MomentEntity;
import io.linfeng.modules.app.service.MomentService;


@Service("momentService")
public class MomentServiceImpl extends ServiceImpl<MomentDao, MomentEntity> implements MomentService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MomentEntity> page = this.page(
                new Query<MomentEntity>().getPage(params),
                new QueryWrapper<MomentEntity>()
        );

        return new PageUtils(page);
    }

}