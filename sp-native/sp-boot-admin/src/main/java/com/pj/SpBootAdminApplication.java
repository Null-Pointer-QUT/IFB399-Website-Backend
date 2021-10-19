package com.pj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import de.codecentric.boot.admin.server.config.EnableAdminServer;


/**
 * SpringBoot-Admin 监控台
 * @author Runtian
 */
@EnableDiscoveryClient			// 
@SpringBootApplication		// SpringBoot注解
@EnableAdminServer			// 控制台 
public class SpBootAdminApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpBootAdminApplication.class, args);		
		System.out.println("\n--------------- SpBootAdminApplication 间空台启动成功 ---------------\n");
	}
	
	
}
