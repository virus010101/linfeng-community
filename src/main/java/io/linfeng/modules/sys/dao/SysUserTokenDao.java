
package io.linfeng.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.linfeng.modules.sys.entity.SysUserTokenEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户Token
 *
 */
@Mapper
public interface SysUserTokenDao extends BaseMapper<SysUserTokenEntity> {

    SysUserTokenEntity queryByToken(String token);
	
}
