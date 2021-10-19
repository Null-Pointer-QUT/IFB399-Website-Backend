package com.pj.project4sp.user4login;


import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SpringMvcUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pj.current.config.SystemObject;
import com.pj.project4sp.SP;
import com.pj.project4sp.role4permission.SpRolePermissionService;
import com.pj.project4sp.user.SpUser;
import com.pj.project4sp.user.SpUserMapper;
import com.pj.utils.sg.AjaxJson;
import com.pj.utils.sg.NbUtil;
import com.pj.utils.sg.WebNbUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * service：user账号相关
 * @author Runtian
 *
 */
@Service
public class SpAccUserService {

	

	@Resource
	private SpUserMapper spUserMapper;

	@Resource
	SpRolePermissionService spRolePermissionService;
	
	
	/**
	  * 登录 
	 * @param key 店铺名称
	 * @param password 店铺密码 
	 * @return
	 */
	AjaxJson doLogin(String key, String password) {
		
		// 0、判断 way (1=ID, 2=昵称，3=邮箱  )
    	int way = 2;	
    	if(NbUtil.isNumber(key) == true){
    		way = 1;
    	}
		
		// 2、获取user
        SpUser user = null;
        if(way == 1) {
        	user = spUserMapper.selectById(Long.parseLong(key));
        }
        if(way == 2) {
        	user = spUserMapper.selectOne(Wrappers.<SpUser>query().lambda()
					.eq(SpUser::getName, key));
        	if (BeanUtil.isEmpty(user)) {
        		user = spUserMapper.selectOne(Wrappers.<SpUser>query().lambda()
						.eq(SpUser::getEmail, key));
			}
        }


        // 3、开始验证
        if(user == null){
        	return AjaxJson.getError("Account does not exist");
        }
        if(NbUtil.isNull(user.getPassword2())) {
        	return AjaxJson.getError("This account has no password and cannot be logged in");
        }
        String md5_password = SystemObject.getPasswordMd5(user.getId(), password);
        if(user.getPassword2().equals(md5_password) == false){
        	return AjaxJson.getError("The account or password is incorrect");
        }
        
        // 4、是否禁用
        if(user.getStatus() == 2) {
        	return AjaxJson.getError("This account has been disabled. If you have any questions, contact your administrator");
        }

        // =========== 至此, 已登录成功 ============ 
        return afterValidate(user);
	}

	public AjaxJson afterValidate(SpUser user) {
		successLogin(user);
		StpUtil.setLoginId(user.getId()); 			// 写入session

		// 组织返回参数
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", user);
		map.put("per_list", spRolePermissionService.getPcodeByRid2(user.getRoleId()));	// 当前拥有的权限集合
		map.put("tokenInfo", StpUtil.getTokenInfo());
		return AjaxJson.getSuccessData(map);	// 将信息返回到前台
	}
	
	
	// 指定id的账号成功登录一次 （修改最后登录时间等数据 ）
	public int successLogin(SpUser u){
		String login_ip = WebNbUtil.getIP(SpringMvcUtil.getRequest());

		int line = spUserMapper.update(null, Wrappers.<SpUser>update().lambda()
				.eq(SpUser::getId, u.getId())
				.set(SpUser::getLoginIp, login_ip)
				.set(SpUser::getLoginTime, new Date())
				.setSql("login_count = login_count + 1"));
		if(line > 0) {
	        u.setLoginIp(login_ip);
	        u.setLoginTime(new Date());
	        u.setLoginCount(u.getLoginCount()==null?0:u.getLoginCount() + 1);
		}
        return line;
	}
	
	// 修改手机号  
	@Transactional(rollbackFor = Exception.class, propagation=Propagation.REQUIRED)	// REQUIRED=如果调用方有事务  就继续使用调用方的事务 
	public AjaxJson updatePhone(long userId, String new_phone) {
		// 修改user手机号
		int line = SP.publicMapper.updateColumnById("sp_user", "phone", new_phone, userId);	// 修改
		return AjaxJson.getByLine(line);
	}
	
	
	
	
}
