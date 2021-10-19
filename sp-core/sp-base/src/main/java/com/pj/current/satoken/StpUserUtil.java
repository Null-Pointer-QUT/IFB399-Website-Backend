package com.pj.current.satoken;

import java.util.Map;

import org.springframework.stereotype.Service;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpLogic;

/**
 * user表鉴权认证实现
 * @author Runtian
 */
@Service
public class StpUserUtil {

	/**
	 * 底层的 StpLogic 对象  
	 */
	public static StpLogic stpLogic = new StpLogic("user"); 
	
	
	// =================== 获取token 相关 ===================


	/**
	 *  获取当前tokenValue
	 * @return 当前tokenValue
	 */
	public static String getTokenValue() {
		return stpLogic.getTokenValue();
	}

	/** 
	 * 获取指定id的tokenValue
	 * @param loginId  .
	 * @return
	 */
	public static String getTokenValueByLoginId(Object loginId) {
		return stpLogic.getTokenValueByLoginId(loginId);
	}

	/**
	 * 获取当前会话的token信息：tokenName与tokenValue
	 * @return 一个Map对象 
	 */
	public static Map<String, String> getTokenInfo() {
		return stpLogic.getTokenInfo();
	}

	// =================== 登录相关操作 ===================

	/**
	 * 在当前会话上登录id 
	 * @param loginId 登录id ，建议的类型：（long | int | String）
	 */
	public static void setLoginId(Object loginId) {
		stpLogic.setLoginId(loginId);
	}

	/** 
	 * 当前会话注销登录
	 */
	public static void logout() {
		stpLogic.logout();
	}

	/**
	 * 指定loginId的会话注销登录（踢人下线）
	 * @param loginId 账号id 
	 */
	public static void logoutByLoginId(Object loginId) {
		stpLogic.logoutByLoginId(loginId);
	}

	// 查询相关

	/** 
 	 * 获取当前会话是否已经登录
 	 * @return 是否已登录 
 	 */
	public static boolean isLogin() {
		return stpLogic.isLogin();
	}

	/**
	 * 检验当前会话是否已经登录，如未登录，则抛出异常
 	 */
 	public static void checkLogin() {
 		getLoginId();
 	}
	
	/** 
 	 * 获取当前会话登录id, 如果未登录，则抛出异常
 	 * @return .
 	 */
	public static Object getLoginId() {
		return stpLogic.getLoginId();
	}

	/** 
	 * 获取当前会话登录id, 如果未登录，则返回默认值
	 * @param defaultValue .
	 * @return .
	 */
	public static <T> T getLoginId(T defaultValue) {
		return stpLogic.getLoginId(defaultValue);
	}
	
	/** 
	 * 获取当前会话登录id, 如果未登录，则返回null
	 * @return
	 */
	public static Object getLoginIdDefaultNull() {
		return stpLogic.getLoginIdDefaultNull();
 	}

	/** 
	 * 获取当前会话登录id, 并转换为String
	 * @return
	 */
	public static String getLoginIdAsString() {
		return stpLogic.getLoginIdAsString();
	}

	/** 
	 * 获取当前会话登录id, 并转换为int
	 * @return
	 */
	public static int getLoginIdAsInt() {
		return stpLogic.getLoginIdAsInt();
	}

	/**
	 * 获取当前会话登录id, 并转换为long
	 * @return
	 */
	public static long getLoginIdAsLong() {
		return stpLogic.getLoginIdAsLong();
	}

	/** 
 	 * 获取指定token对应的登录id，如果未登录，则返回 null 
 	 * @return .
 	 */
 	public static Object getLoginIdByToken(String tokenValue) {
 		return stpLogic.getLoginIdByToken(tokenValue);
 	}
	
	// =================== session相关 ===================

	/** 
	 * 获取指定loginId的session, 如果没有，isCreate=是否新建并返回
	 * @param loginId 登录id
	 * @param isCreate 是否新建
	 * @return SaSession
	 */
	public static SaSession getSessionByLoginId(Object loginId, boolean isCreate) {
		return stpLogic.getSessionByLoginId(loginId, isCreate);
	}

	/** 
	 * 获取指定loginId的session
	 * @param loginId .
	 * @return .
	 */
	public static SaSession getSessionByLoginId(Object loginId) {
		return stpLogic.getSessionByLoginId(loginId);
	}
	
	/** 
	 * 获取当前会话的session
	 * @return
	 */
	public static SaSession getSession() {
		return stpLogic.getSession();
	}

	// =================== 权限验证操作 ===================

	/** 
 	 * 指定loginId是否含有指定权限
 	 * @param loginId .
 	 * @param pcode .
 	 * @return .
 	 */
	public static boolean hasPermission(Object loginId, Object pcode) {
		return stpLogic.hasPermission(loginId, pcode);
	}

	/** 
 	 * 当前会话是否含有指定权限
 	 * @param pcode .
 	 * @return .
 	 */
	public static boolean hasPermission(Object pcode) {
		return stpLogic.hasPermission(pcode);
	}

	/** 
 	 * 当前账号是否含有指定权限 ， 没有就抛出异常
 	 * @param pcode .
 	 */
	public static void checkPermission(Object pcode) {
		stpLogic.checkPermission(pcode);
	}

	/** 
 	 * 当前账号是否含有指定权限 ， 【指定多个，必须全都有】
 	 * @param pcodeArray .
 	 */
	public static void checkPermissionAnd(Object... pcodeArray) {
		stpLogic.checkPermissionAnd(pcodeArray);
	}

	/** 
 	 * 当前账号是否含有指定权限 ， 【指定多个，有一个就可以了】
 	 * @param pcodeArray .
 	 */
	public static void checkPermissionOr(Object... pcodeArray) {
		stpLogic.checkPermissionOr(pcodeArray);
	}


}
