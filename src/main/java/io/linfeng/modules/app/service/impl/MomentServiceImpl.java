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