//package com.pj.more;
//
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.pj.auth.GatewayAuthUtil;
//
///**
// * 以代码方式配置路由转发规则
// * @param builder
// * @return
// */
//@Configuration
//public class RouteLocatorBean {
//	
//	@Bean
//	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
//	    return builder.routes()
//	        .route(p -> p
//	            .path("/sp-admin/**")
//	            .filters(f -> f.stripPrefix(1).addRequestHeader(GatewayAuthUtil.REQUEST_TOKEN_KEY, GatewayAuthUtil.getToken()))
//	            .uri("lb://sp-admin")
//	        ).build();
//	}
//	
//}