package com.pj.project4sp.user4login;

import cn.dev33.satoken.stp.StpUtil;
import com.pj.project4sp.role4permission.SpRolePermissionService;
import com.pj.project4sp.user.SpUser;
import com.pj.project4sp.user.SpUserUtil;
import com.pj.utils.sg.AjaxJson;
import com.pj.utils.sg.NbUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * user账号相关的接口
 * @author Runtian
 *
 */
@RestController
@RequestMapping("/AccUser/")
public class SpAccUserController {


	@Resource
	SpAccUserService spAccUserService;

	@Resource
	SpRolePermissionService spRolePermissionService;


	// 账号、密码登录
	@RequestMapping("doLogin")
	AjaxJson doLogin(@RequestBody UserLoginParam loginParam) {
		// 1、验证参数
		if(NbUtil.isOneNull(loginParam.getKey(), loginParam.getPassword())) {
			return AjaxJson.getError("The account and password cannot be empty");
		}
		return spAccUserService.doLogin(loginParam.getKey(), loginParam.getPassword());
	}


	// 退出登录
	@PostMapping("doExit")
	AjaxJson doExit() {
		StpUtil.logout();
		return AjaxJson.getSuccess();
	}


	// 管理员登录后台时需要返回的信息
	// user=当前登录user信息，menu_list=当前user权限集合
	@RequestMapping("firstOpenUser")
	AjaxJson firstOpenUser(HttpServletRequest request) {
		// 当前user
		SpUser user = SpUserUtil.getCurrUser();

		// 组织参数
		Map<String, Object> map = new HashMap<>();
		map.put("user", SpUserUtil.getCurrUser());	// 当前登录user
		map.put("per_list", spRolePermissionService.getPcodeByRid2(user.getRoleId()));								// 当前拥有的权限集合
//		map.put("app_cfg", SysCfgUtil.getAppCfg());								// 当前系统的配置
		return AjaxJson.getSuccessData(map);
	}


	// 测试
	@RequestMapping("test")
	AjaxJson test() {
		System.out.println("接口测试");
		return AjaxJson.getSuccess();
	}


}
