package io.linfeng.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.modules.admin.entity.AppUserEntity;
import io.linfeng.modules.app.entity.CommentThumbsEntity;
import io.linfeng.modules.app.form.AddThumbsForm;

import java.util.Map;

/**
 * 
 *
 * @author linfeng
 * @email 3582996245@qq.com
 * @date 2022-01-25 19:00:24
 */
public interface CommentThumbsService extends IService<CommentThumbsEntity> {

    PageUtils queryPage(Map<String, Object> params);

}

