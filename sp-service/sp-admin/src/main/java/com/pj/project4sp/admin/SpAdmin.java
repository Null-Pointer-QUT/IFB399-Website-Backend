package com.pj.project4sp.admin;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

/**
 * Model: 系统管理员表
 * @author Runtian
 */
@Data
public class SpAdmin implements Serializable  {

	private static final long serialVersionUID = 1L;


	private long id;		// id，--主键、自增 
	private String name;		// admin名称 
	private String avatar;		// 头像  
	private String password;		// 密码 
	private String pw;		// 明文密码 
	private String phone;		// 手机号 
	private int roleId;		// 角色id
	private int status;		// 状态 （1=是，2=否） 
	private long createByAid;		// 创建自哪个管理员
	private Date createTime;		// 创建时间
	private Date loginTime;		// 上次登陆时间
	private String loginIp;		// 上次登陆IP
	private int loginCount;		// 登陆次数
	
	// 额外字段 
	private String roleName;		// 所属角色名称


	// 防止密码被传递到前台 
    public String getPassword(){
    	return "********";
    }
    // 获取真实密码 
    @JsonIgnore()
    public String getPassword2(){
    	return this.password;
    }
	
}
