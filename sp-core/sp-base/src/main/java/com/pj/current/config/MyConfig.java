package com.pj.current.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 我的自定义配置
 * @author Runtian
 *
 */
@Data
@Component
@ConfigurationProperties(prefix="spring.myconfig")	// yml配置所在位置 
@RefreshScope
public class MyConfig {

	/** md5的盐  */
	private String md5Salt;		
	/** 是否明文存储密码   */
	private Boolean isPw;		
	
	/** 本项目部署到的服务器域名（文件上传、微信支付等等模块  要用到） */
	private String domain;			
	

	private String title;			
	
}
