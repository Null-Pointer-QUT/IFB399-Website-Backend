//package com.pj.current.satoken;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.pj.project4sp.admin.SpAdminMapper;
//import com.pj.project4sp.role4permission.SpRolePermissionService;
//
//import cn.dev33.satoken.stp.StpInterface;
//
///**
// * 自定义权限验证接口扩展 
// * @author Runtian
// *
// */
//@Component	
//public class StpInterfaceImpl implements StpInterface {
//
//	
//	@Autowired
//	SpAdminMapper spAdminMapper;
//	
//	@Autowired
//	SpRolePermissionService spRolePermissionService;
//	
//	
//	/** 返回一个账号所拥有的权限码集合  */
//	@Override
//	public List<Object> getPermissionCodeList(Object loginId, String loginKey) {
//		if(loginKey.equals("login")) {
//			long roleId = spAdminMapper.getById(Long.valueOf(loginId.toString())).getRole_id();	// 获取role_id  
//			return spRolePermissionService.getPcodeByRid2(roleId);								// 所有权限id  
//		}
//		return null;
//	}
//
//}
