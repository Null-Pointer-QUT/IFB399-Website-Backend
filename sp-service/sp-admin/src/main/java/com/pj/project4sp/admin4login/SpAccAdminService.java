package com.pj.project4sp.admin4login;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pj.current.config.SystemObject;
import com.pj.project4sp.SP;
import com.pj.project4sp.admin.SpAdmin;
import com.pj.project4sp.admin.SpAdminMapper;
import com.pj.project4sp.role4permission.SpRolePermissionService;
import com.pj.utils.sg.AjaxJson;
import com.pj.utils.sg.NbUtil;
import com.pj.utils.sg.WebNbUtil;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SpringMvcUtil;

/**
 * service：admin账号相关
 * @author Runtian
 *
 */
@Service
public class SpAccAdminService {

	

	@Autowired
	SpAccAdminMapper spAccAdminMapper;

	@Autowired
	SpAdminMapper spAdminMapper;
	
	@Autowired
	SpRolePermissionService spRolePermissionService;
	
	
	/**
	  * 登录 
	 * @param key 店铺名称
	 * @param password 店铺密码 
	 * @return
	 */
	AjaxJson doLogin(String key, String password) {
		
		// 0、判断 way (1=ID, 2=昵称，3=手机号  )
    	int way = 2;	
    	if(NbUtil.isNumber(key) == true){
    		way = 1;
    		if(key.length() == 11){
    			way = 3;
    		}
    	}
		
		// 2、获取admin
        SpAdmin admin = null;	
        if(way == 1) {
        	admin = spAdminMapper.getById(Long.parseLong(key)); 
        }
        if(way == 2) {
        	admin = spAdminMapper.getByName(key); 
        }
        if(way == 3) {
        	admin = spAdminMapper.getByPhone(key); 
        }
        

        // 3、开始验证
        if(admin == null){
        	return AjaxJson.getError("无此账号");	
        }
        if(NbUtil.isNull(admin.getPassword2())) {
        	return AjaxJson.getError("此账号尚未设置密码，无法登陆");
        }
        String md5_password = SystemObject.getPasswordMd5(admin.getId(), password);
        if(admin.getPassword2().equals(md5_password) == false){
        	return AjaxJson.getError("密码错误");	
        }
        
        // 4、是否禁用
        if(admin.getStatus() == 2) {
        	return AjaxJson.getError("此账号已被禁用，如有疑问，请联系管理员");	
        }

        // =========== 至此, 已登录成功 ============ 
        successLogin(admin);
        StpUtil.setLoginId(admin.getId()); 			// 写入session 
        
        // 组织返回参数  
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("admin", admin);
		map.put("per_list", spRolePermissionService.getPcodeByRid2(admin.getRoleId()));	// 当前拥有的权限集合
		map.put("tokenInfo", StpUtil.getTokenInfo());
		return AjaxJson.getSuccessData(map);	// 将信息返回到前台    
	}
	
	
	// 指定id的账号成功登录一次 （修改最后登录时间等数据 ）
	public int successLogin(SpAdmin s){
		String login_ip = WebNbUtil.getIP(SpringMvcUtil.getRequest());
		int line = spAccAdminMapper.successLogin(s.getId(), login_ip);
		if(line > 0) {
	        s.setLoginIp(login_ip);
	        s.setLoginTime(new Date());
	        s.setLoginCount(s.getLoginCount() + 1);
		}
        return line;
	}
	
	// 修改手机号  
	@Transactional(rollbackFor = Exception.class, propagation=Propagation.REQUIRED)	// REQUIRED=如果调用方有事务  就继续使用调用方的事务 
	public AjaxJson updatePhone(long admin_id, String new_phone) {
		// 修改admin手机号
		int line = SP.publicMapper.updateColumnById("sys_admin", "phone", new_phone, admin_id);	// 修改
		return AjaxJson.getByLine(line);
	}
	
	
	
	
}
