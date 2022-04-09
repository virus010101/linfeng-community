package io.linfeng.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.modules.app.entity.MomentEntity;

import java.util.Map;

/**
 * 动态表
 *
 * @author linfeng
 * @email 3582996245@qq.com
 * @date 2022-01-05 14:23:11
 */
public interface MomentService extends IService<MomentEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

