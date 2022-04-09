package io.linfeng.modules.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.linfeng.common.utils.AppPageUtils;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.modules.admin.entity.LinkEntity;
import io.linfeng.modules.app.form.LinkListForm;

import java.util.Map;

/**
 * 
 *
 * @author linfeng
 * @email 3582996245@qq.com
 * @date 2022-01-26 14:05:38
 */
public interface LinkService extends IService<LinkEntity> {

    PageUtils queryPage(Map<String, Object> params);

}

