package com.pj.project4sp.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pj.current.satoken.AuthConst;
import com.pj.project4sp.SP;
import com.pj.project4sp.admin4password.SpAdminPasswordService;
import com.pj.utils.sg.AjaxJson;
import com.pj.utils.sg.SoMap;

import cn.dev33.satoken.stp.StpUtil;

/**
 * Controller -- 系统管理员表
 * @author Runtian
 */
@RestController
@RequestMapping("/admin/")
public class SpAdminController {

	@Autowired
	SpAdminMapper spAdminMapper;
	
	@Autowired
	SpAdminService spAdminService;
	
	@Autowired
	SpAdminPasswordService spAdminPasswordService;

	// 增  
	@RequestMapping("add")
	AjaxJson add(SpAdmin admin){
		StpUtil.checkPermission(AuthConst.p_admin_list);	// 鉴权
		long id = spAdminService.add(admin);
		return AjaxJson.getSuccessData(id);
	}

	// 删  
	@RequestMapping("delete")
	AjaxJson delete(long id){
		StpUtil.checkPermission(AuthConst.p_admin_list);	// 鉴权
		// 不能自己删除自己
		if(StpUtil.getLoginIdAsLong() == id) {
			return AjaxJson.getError("不能自己删除自己");
		}
		int line = spAdminMapper.delete(id);
		return AjaxJson.getByLine(line);
	}


	// 删 - 根据id列表
	@RequestMapping("deleteByIds")
	AjaxJson deleteByIds(){
		// 鉴权
		StpUtil.checkPermission(AuthConst.p_admin_list);	
		// 不能自己删除自己
		List<Long> ids = SoMap.getRequestSoMap().getListByComma("ids", long.class); 
		if(ids.contains(StpUtil.getLoginIdAsLong())) {
			return AjaxJson.getError("不能自己删除自己");
		}
		// 开始删除 
		int line = SP.publicMapper.deleteByIds("sp_admin", ids);
		return AjaxJson.getByLine(line);
	}
	
	// 改  -  name
	@RequestMapping("update")
	AjaxJson update(SpAdmin obj){
		StpUtil.checkPermission(AuthConst.p_admin_list);	// 鉴权
		SpAdminUtil.checkName(obj.getId(), obj.getName());
		int line = spAdminMapper.update(obj);
		return AjaxJson.getByLine(line);
	}

	// 查  
	@RequestMapping("getById")
	AjaxJson getById(long id){
		StpUtil.checkPermission(AuthConst.p_admin_list);	// 鉴权
		Object data = spAdminMapper.getById(id);
		return AjaxJson.getSuccessData(data);
	}

	// 查 - 集合（参数为null或0时默认忽略此条件）  
	@RequestMapping("getList")
	AjaxJson getList(){
		StpUtil.checkPermission(AuthConst.p_admin_list);	// 鉴权
		SoMap so = SoMap.getRequestSoMap();
		List<SpAdmin> list = spAdminMapper.getList(so.startPage());
		return AjaxJson.getPageData(so.getDataCount(), list);
	}

	// 改密码
	@RequestMapping("updatePassword")
	AjaxJson updatePassword(long id, String password){
		StpUtil.checkPermission(AuthConst.p_admin_list);	// 鉴权
		int line = spAdminPasswordService.updatePassword(id, password);
		return AjaxJson.getByLine(line);
	}
	

	// 改头像 
	@RequestMapping("updateAvatar")
	AjaxJson updateAvatar(long id, String avatar){
		StpUtil.checkPermission(AuthConst.p_admin_list);	// 鉴权
		int line = SP.publicMapper.updateColumnById("sp_admin", "avatar", avatar, id);
		return AjaxJson.getByLine(line);
	}
	

	// 改状态 
	@RequestMapping("updateStatus")
	public AjaxJson updateStatus(long admin_id, int status) {
		StpUtil.checkPermission(AuthConst.r1);

		// 验证对方是否为超管(保护超管不受摧残) 
		if(StpUtil.hasPermission(admin_id, AuthConst.r1)){
			return AjaxJson.getError("抱歉，对方角色为系统超级管理员，您暂无权限操作");
		}
		
		// 修改状态 
		SP.publicMapper.updateColumnById("sp_admin", "status", status, admin_id);
		// 如果是禁用，就停掉其秘钥有效果性，使其账号的登录状态立即无效 
		if(status == 2) {
			StpUtil.logoutByLoginId(admin_id);
		}
		return AjaxJson.getSuccess();
	}
	
	
	
	// 改角色 
	@RequestMapping("updateRole")
	AjaxJson updateRole(long id, String role_id){
//		StpUtil.checkPermission(AuthConst.J_1023);	// 鉴权
		int line = SP.publicMapper.updateColumnById("sp_admin", "role_id", role_id, id);
		return AjaxJson.getByLine(line);
	}
	

	// 返回当前admin信息 
	@RequestMapping("getByCurr")
	AjaxJson getByCurr() {
		SpAdmin admin = SpAdminUtil.getCurrAdmin();
		return AjaxJson.getSuccessData(admin);
	}
	
	// 当前admin修改信息
	@RequestMapping("updateInfo")
	AjaxJson updateInfo(SpAdmin obj){
		obj.setId(StpUtil.getLoginIdAsLong());
		SpAdminUtil.checkName(obj.getId(), obj.getName());
		int line = spAdminMapper.update(obj);
		return AjaxJson.getByLine(line);
	}
	
	
	
	
	
	
	
	


}
