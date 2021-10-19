package com.pj;

import cn.dev33.satoken.spring.SaTokenSetup;
import com.pj.current.SpCloudUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启动类
 * @author Runtian
 */
@EnableAsync
@SaTokenSetup	// sa-token权限验证
@EnableCaching // 启用缓存
@EnableScheduling // 启动定时任务
@SpringBootApplication // springboot本尊
@EnableTransactionManagement // 启动注解事务管理
@EnableFeignClients		// 启用Feign实现RPC调用 
public class SpPortalApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SpPortalApplication.class);
		SpCloudUtil.printCurrentServiceInfo();
		// 测试服务调用 
//		System.out.println(FeignFactory.spCfgInterface.getAppCfg("app_name", "默认值"));
	}

}