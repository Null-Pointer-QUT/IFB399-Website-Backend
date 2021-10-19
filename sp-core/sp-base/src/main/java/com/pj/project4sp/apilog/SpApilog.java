package com.pj.project4sp.apilog;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * Model: api请求记录表
 * @author Runtian
 */
@Data
@TableName("sp_apilog")
public class SpApilog implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;		// 记录id 
	private String reqId;		// 请求id
	private String reqIp;		// 客户端ip
	private String reqApi;		// 请求api
	private String reqParame;		// 请求参数
	private String reqType;		// 请求方式
	private String reqToken;		// 请求token
	private String reqHeader;		// 请求header
	
	private int resCode;		// 返回-状态码
	private String resMsg;		// 返回-信息描述
	private String resString;		// 返回-整个信息字符串形式
	
	private long userId;		// user_id
	private long adminId;		// admin_id

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone="GMT+8")
	private Date startTime;		// 请求开始时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone="GMT+8")
	private Date endTime;		// 请求结束时间
	private int costTime;		// 花费时间，单位ms
	
	
	// 构造一个普通 实体类
	public SpApilog() {}
	
	// 构造一个 save 实体类 
	public SpApilog(String reqId, String reqIp, String reqApi, String reqParame, String reqToken, long userId,
					long adminId) {
		super();
		this.reqId = reqId;
		this.reqIp = reqIp;
		this.reqApi = reqApi;
		this.reqParame = reqParame;
		this.reqToken = reqToken;
		this.userId = userId;
		this.adminId = adminId;
	}

	// 构造一个 update 实体类 
	public SpApilog(String reqId, int resCode, String resMsg, String resString, int costTime) {
		super();
		this.reqId = reqId;
		this.resCode = resCode;
		this.resMsg = resMsg;
		this.resString = resString;
		this.costTime = costTime;
	}

	
	
	
	

}
