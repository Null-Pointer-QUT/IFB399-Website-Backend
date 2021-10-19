package com.pj.satoken;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.pj.project4sp.role4permission.SpRolePermissionService;
import com.pj.project4sp.user.SpUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义权限验证接口扩展 
 * @author Runtian
 *
 */
@Component	
public class StpInterfaceImpl implements StpInterface {

	
	@Autowired
	SpUserMapper spUserMapper;
	
	@Autowired
	SpRolePermissionService spRolePermissionService;
	
	
	/** 返回一个账号所拥有的权限码集合  */
	@Override
	public List<Object> getPermissionCodeList(Object loginId, String loginKey) {
		if(loginKey.equals("login")) {
			long roleId = spUserMapper.selectById(Long.valueOf(loginId.toString())).getRoleId();	// 获取role_id
			return spRolePermissionService.getPcodeByRid2(roleId);								// 所有权限id  
		}
		return null;
	}

}
