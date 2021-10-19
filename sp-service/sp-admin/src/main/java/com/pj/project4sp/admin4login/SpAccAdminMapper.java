package com.pj.project4sp.admin4login;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 账号相关 
 * @author Runtian
 *
 */
@Mapper
public interface SpAccAdminMapper {

	// 指定id的账号成功登录一次 
	public int successLogin(@Param("id")long id, @Param("login_ip")String login_ip);
	
}
