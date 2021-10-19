package com.pj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * gateway 服务网关-启动 
 * @author Runtian
 */
@EnableDiscoveryClient			// 注册中心 
@SpringBootApplication		// SpringBoot注解
public class SpGatewayApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SpGatewayApplication.class, args);		
		System.out.println("\n--------------- SpGatewayApplication 服务网关启动成功 ---------------\n");
	}
	
	
}
