package io.linfeng.modules.admin.dao;

import io.linfeng.modules.admin.entity.MessageEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author linfeng
 * @email 3582996245@qq.com
 * @date 2022-01-26 13:15:30
 */
@Mapper
public interface MessageDao extends BaseMapper<MessageEntity> {
	
}
