package io.linfeng.modules.admin.dao;

import io.linfeng.modules.admin.entity.SensitiveEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 敏感词库信息表
 * 
 * @author linfeng
 * @email 3582996245@qq.com
 * @date 2022-01-28 13:40:57
 */
@Mapper
public interface SensitiveDao extends BaseMapper<SensitiveEntity> {
	
}
