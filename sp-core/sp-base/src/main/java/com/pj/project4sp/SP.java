package com.pj.project4sp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.pj.project4sp.public4mapper.PublicMapper;
import com.pj.project4sp.public4mapper.PublicService;

/**
 * 公共Mapper 与 公共Service
 * @author Runtian
 *
 */
@Component
public class SP {
	

	/**
	 * 公共Mapper
	 */
	public static PublicMapper publicMapper;	
	/**
	 * 公共Service
	 */
	public static PublicService publicService;				
	
	
	
	
	// 注入 
	@Autowired
	public void setBean(
			PublicMapper publicMapper,
			PublicService publicService
			) {
		SP.publicMapper = publicMapper;
		SP.publicService = publicService;
	}

	
	// 返回RestTemplate实例 
	@Bean
	@LoadBalanced	// 使其可以解析自定义host并具有负载均衡的功能   (却使它丧失了解析正常 host 的能力 )
	public RestTemplate getTestTemplate(){
		return new RestTemplate();
	}
	// http操作类
	public static RestTemplate restTemplate;
	@Autowired
	private void setRestTemplage(RestTemplate restTemplate) {
		SP.restTemplate = restTemplate;
	}
	
	
	
}
