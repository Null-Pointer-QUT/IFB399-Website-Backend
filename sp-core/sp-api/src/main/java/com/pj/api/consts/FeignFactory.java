package com.pj.api.consts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pj.api.client.SpCfgInterface;

/**
 * Feign bean工厂类
 * @author Runtian
 *
 */
@Component
public class FeignFactory {

	/**
	 *  系统配置 通信接口
	 */
	public static SpCfgInterface spCfgInterface;
	@Autowired
	public void setSpCfgInterface(SpCfgInterface spCfgInterface) {
		FeignFactory.spCfgInterface = spCfgInterface;
	}
	
	
	
	
	
}
