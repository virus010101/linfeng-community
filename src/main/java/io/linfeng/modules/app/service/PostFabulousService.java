package io.linfeng.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.modules.app.entity.PostFabulousEntity;

import java.util.Map;

/**
 * 
 *
 * @author linfeng
 * @email 3582996245@qq.com
 * @date 2022-01-24 20:49:32
 */
public interface PostFabulousService extends IService<PostFabulousEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Boolean isThumb(Integer uid, Integer id);
}

