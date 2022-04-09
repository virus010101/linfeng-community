package io.linfeng.modules.admin.service.impl;

import io.linfeng.modules.admin.dao.AppUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.Query;

import io.linfeng.modules.admin.dao.SystemDao;
import io.linfeng.modules.admin.entity.SystemEntity;
import io.linfeng.modules.admin.service.SystemService;

@Service("systemService")
public class SystemServiceImpl extends ServiceImpl<SystemDao, SystemEntity> implements SystemService {

    @Autowired
    private AppUserDao appUserDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SystemEntity> page = this.page(
                new Query<SystemEntity>().getPage(params),
                new QueryWrapper<SystemEntity>()
        );

        return new PageUtils(page);
    }


}