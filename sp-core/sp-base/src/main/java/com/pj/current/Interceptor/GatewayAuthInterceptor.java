package com.pj.current.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pj.auth.GatewayAuthUtil;
import com.pj.utils.sg.AjaxJson;

/**
 * 网关验证拦截器 
 * @author Runtian
 *
 */
public class GatewayAuthInterceptor implements HandlerInterceptor {

	
	// 之前
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 验证网关令牌 
		String gatewayAuthToken = request.getHeader(GatewayAuthUtil.REQUEST_TOKEN_KEY);
		if(GatewayAuthUtil.verifyToken(gatewayAuthToken) == false) {
			// 验证失败，请求不通过 
			response.setContentType("application/json; charset=utf-8"); 
			response.getWriter().print(new ObjectMapper().writeValueAsString(AjaxJson.getError("无效请求")));
			return false;
		}
		// 请求通过
		return true;
	}

	// 之后
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
		// System.out.println("========== 之后 ==========");
	}

	// 最终
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// System.out.println("========== 最终 ==========");
		// System.out.println(ex);
	}
	
	
	
	
}
