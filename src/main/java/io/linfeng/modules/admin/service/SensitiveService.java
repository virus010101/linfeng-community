package io.linfeng.modules.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.modules.admin.entity.SensitiveEntity;

import java.util.Map;

/**
 * 敏感词库信息表
 *
 * @author linfeng
 * @email 3582996245@qq.com
 * @date 2022-01-28 13:40:57
 */
public interface SensitiveService extends IService<SensitiveEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Boolean checkContent(String content);

    void checkPostContent(String content);
}

