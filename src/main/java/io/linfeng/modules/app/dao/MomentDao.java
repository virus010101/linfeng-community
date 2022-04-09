package io.linfeng.modules.app.dao;

import io.linfeng.modules.app.entity.MomentEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 动态表
 * 
 * @author linfeng
 * @email 3582996245@qq.com
 * @date 2022-01-05 14:23:11
 */
@Mapper
public interface MomentDao extends BaseMapper<MomentEntity> {
	
}
