package com.pj.project4sp.user;

import cn.dev33.satoken.SaTokenManager;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.cglib.CglibUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pj.utils.sg.AjaxError;
import com.pj.utils.sg.NbUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * user工具类
 * @author Runtian
 *
 */
@Component
public class SpUserUtil {

	
	static SpUserMapper spUserMapper;
	@Autowired
	public void setSpUserMapper(SpUserMapper spUserMapper) {
		SpUserUtil.spUserMapper = spUserMapper;
	}

	public static SpUserVo getUserById(long userId) {
		return CglibUtil.copy(spUserMapper.selectById(userId), SpUserVo.class);
	}
	
	// 当前user
	public static SpUser getCurrUser() {
		long userId = StpUtil.getLoginIdAsLong();
		return spUserMapper.selectById(userId);
	}
	
	
	// 检查指定姓名是否合法 ,如果不合法，则抛出异常 
	public static boolean checkName(long userId, String name) {
		if(NbUtil.isNull(name)) {
			throw AjaxError.get("The user name cannot be empty");
		}
		if(NbUtil.isNumber(name)) {
			throw AjaxError.get("The user name cannot be a pure number");
		}
//		if(name.startsWith("a")) {
//			throw AjaxException.get("账号名称不能以字母a开头");
//		}
		SpUser u2 = spUserMapper.selectOne(Wrappers.<SpUser>query().lambda().eq(SpUser::getName, name));
		if(u2 != null && u2.getId() != userId) {	// 能查出来数据，而且不是本人，则代表与已有数据重复
			throw AjaxError.get("The account name has been used by an account, please change it");
		} 
		return true;
	}

	public static boolean checkEmail(long userId, String email) {
		if(NbUtil.isNull(email)) {
			throw AjaxError.get("The email address cannot be empty");
		}
		if (!email.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
			throw AjaxError.get("The email address is invalid");
		}
		SpUser u2 = spUserMapper.selectOne(Wrappers.<SpUser>query().lambda().eq(SpUser::getEmail, email));
		if(u2 != null && u2.getId() != userId) {	// 能查出来数据，而且不是本人，则代表与已有数据重复
			throw AjaxError.get("The email address has been registered");
		}
		return true;
	}
	
	// 检查整个user是否合格
	public static boolean checkUser(SpUser u) {
		// 检查姓名 
		checkName(u.getId(), u.getName());
		// 检查密码
		if (BeanUtil.isEmpty(u.getUuid())) {
			if (u.getPassword2().length() < 4) {
				throw new AjaxError("The password must be at least four characters long");
			}
			checkEmail(u.getId(), u.getEmail());
		}
		return true;
	}
	
	
	
	// 指定的name是否可用 
	public static boolean nameIsOk(String name) {
		SpUser u2 = spUserMapper.selectOne(Wrappers.<SpUser>query().lambda().eq(SpUser::getName, name));
		if(u2 == null) {
			return true;
		}
		return false;
	}
	
	
	// 获取指定token对应的user_id
	public static long getUserIdByToken(String token) {
		Object login_id = SaTokenManager.getDao().getValue(StpUtil.stpLogic.getKeyTokenValue(token));
		if(login_id == null) {
			throw new NotLoginException();
		}
		return Long.parseLong(login_id.toString());
	}






	
	
}
