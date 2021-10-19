package com.pj.api.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import feign.hystrix.FallbackFactory;

/**
 * 系统配置 服务降级处理
 * @author Runtian
 *
 */
@Component
public class SpCfgInterfaceFallback implements FallbackFactory<SpCfgInterface>
{

	private static final Logger log = LoggerFactory.getLogger(SpCfgInterfaceFallback.class);
	
	/**
	 * 服务降级时触发的方法 
	 */
	@Override
	public SpCfgInterface create(Throwable cause) {
		
		
		log.error("--------------------系统配置服务异常，触发降级: {}", cause.getMessage());
		
		// 返回熔断后的对象 
		return new SpCfgInterface() {
			
			@Override
			public String getServerCfg(String key, String defaultValue) {
				return defaultValue;
			}
			
			@Override
			public String getAppCfg(String key, String defaultValue) {
				return defaultValue;
			}
			
		};
	}

}
