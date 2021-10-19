package com.pj.project4sp.spcfg;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务实现者
 * @author Runtian
 *
 */
@RestController
@RequestMapping("/SpCfgApi/")
public class SpCfgInterfaceImpl {

	

	// 获取server端指定配置信息
	@RequestMapping("getServerCfg")
	public String getServerCfg(String key, String defaultValue) {
		return SpCfgUtil.getCfgBy("server_cfg", key, defaultValue);
	}
	
	// 获取App端指定配置信息 
	@RequestMapping("getAppCfg")
	public String getAppCfg(String key, String defaultValue) {
		return SpCfgUtil.getCfgBy("app_cfg", key, defaultValue);
	}
	
	
	
}
