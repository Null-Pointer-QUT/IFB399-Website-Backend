package com.pj.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pj.api.FeignInterceptor;
import com.pj.api.consts.FeignConsts;

/**
 * 系统配置 服务
 * @author Runtian
 *
 */
@FeignClient(
		name = FeignConsts.SP_HOME, 				// 服务名称 
		configuration = FeignInterceptor.class,		// 请求拦截器 
		fallbackFactory = SpCfgInterfaceFallback.class	// 服务降级 
		)	
public interface SpCfgInterface {

	
	// 获取server端指定配置信息
	@RequestMapping("/SpCfgApi/getServerCfg")
	public String getServerCfg(@RequestParam("key")String key, @RequestParam("defaultValue")String defaultValue);
	
	// 获取App端指定配置信息 
	@RequestMapping("/SpCfgApi/getAppCfg")
	public String getAppCfg(@RequestParam("key")String key, @RequestParam("defaultValue")String defaultValue);
	
	
}
