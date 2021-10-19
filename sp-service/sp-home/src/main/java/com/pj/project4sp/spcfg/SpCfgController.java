package com.pj.project4sp.spcfg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pj.utils.sg.AjaxJson;

/**
 * 系统配置相关 
 */
@RestController
@RequestMapping("/SpCfg/")
public class SpCfgController {

	
	// 配置信息
	@Autowired
	SpCfgService sysCfgService;
		
	// 返回应用配置信息 （对公开放的）
	@RequestMapping("app_cfg")
	public AjaxJson app_cfg(){
		return AjaxJson.getSuccessData(sysCfgService.getCfgValue("app_cfg"));
	}
	
	
	
	
	
	
}
