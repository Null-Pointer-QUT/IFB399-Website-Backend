package com.pj.project4sp.role;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pj.project4sp.user.SpUserUtil;
import com.pj.utils.sg.AjaxError;
import com.pj.utils.sg.AjaxJson;
import com.pj.utils.sg.NbUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 工具类：SysRole
 * @author Runtian
 *
 */
@Component
public class SpRoleUtil {
	

	/** 底层Mapper依赖 */
	static SpRoleMapper spRoleMapper;
	@Autowired
	public void setSpRoleMapper(SpRoleMapper spRoleMapper) {
		SpRoleUtil.spRoleMapper = spRoleMapper;
	}
	
	// 获取当前会话的role_id
	public static long getCurrRoleId() {
		return SpUserUtil.getCurrUser().getRoleId();
	}
	
	

	// 验证一个SysRole 是否符合标准 
	static AjaxJson checkRole(SpRole s) {
		
		// 1、名称相关 
		if(NbUtil.isNull(s.getRoleName())) {
			return AjaxJson.getError("昵称不能为空");
		}
		SpRole s2 = spRoleMapper.selectOne(Wrappers.<SpRole>query().lambda()
				.eq(SpRole::getRoleName, s.getRoleName()));
		if(s2 != null && s2.getId() != s.getId()) {	// 如果该名称已存在，并且不是当前角色 
			return AjaxJson.getError("昵称与已有角色重复，请更换");
		}
		
		// 重重检验，最终合格
		return AjaxJson.getSuccess();
	}
	// 验证一个Role是否符合标准, 不符合则抛出异常
	static void checkRoleThrow(SpRole s) {
		AjaxJson aj = checkRole(s);
		if(aj.getCode() != AjaxJson.CODE_SUCCESS){
			throw AjaxError.get(aj.getMsg());
		}
	}





	
	
}
