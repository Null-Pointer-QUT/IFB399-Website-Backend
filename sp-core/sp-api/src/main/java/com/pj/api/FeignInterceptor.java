package com.pj.api;

import org.springframework.stereotype.Component;

import com.pj.auth.GatewayAuthUtil;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * feign拦截器, 在feign请求发出之前，加入一些操作
 * 
 * @author Runtian
 *
 */
@Component
public class FeignInterceptor implements RequestInterceptor {
	
	/**
	 * 执行feign调用前，会进入此方法添加上网关鉴权token，否则无法通过请求验证 
	 */
	@Override
	public void apply(RequestTemplate requestTemplate) {
//		System.out.println("------------------------");
		requestTemplate.header(GatewayAuthUtil.REQUEST_TOKEN_KEY, GatewayAuthUtil.getToken());
	}
	
	
	
}
