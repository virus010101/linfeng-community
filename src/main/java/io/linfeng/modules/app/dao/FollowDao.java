package io.linfeng.modules.app.dao;

import io.linfeng.modules.app.entity.FollowEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * 
 * @author linfeng
 * @email 3582996245@qq.com
 * @date 2022-01-24 14:38:31
 */
@Mapper
public interface FollowDao extends BaseMapper<FollowEntity> {

	void cancelFollow(@Param("uid")Integer uid, @Param("followId")Integer followId);
}
