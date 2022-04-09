package io.linfeng.modules.admin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.linfeng.common.utils.AppPageUtils;
import io.linfeng.modules.app.form.LinkListForm;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.Query;

import io.linfeng.modules.admin.dao.LinkDao;
import io.linfeng.modules.admin.entity.LinkEntity;
import io.linfeng.modules.admin.service.LinkService;


@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkDao, LinkEntity> implements LinkService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LinkEntity> page = this.page(
                new Query<LinkEntity>().getPage(params),
                new QueryWrapper<LinkEntity>()
        );

        return new PageUtils(page);
    }

}