package com.pj.current.sentinel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pj.utils.sg.AjaxJson;

/**
 * 从sentinel-dashboard 控制台添加的限流规则，触发流控后，会在这里执行代码
 */
@Component
public class PigxUrlBlockHandler implements BlockExceptionHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
		AjaxJson aj = AjaxJson.get(503, "系统繁忙，请稍后再试");
		response.setContentType("application/json; charset=utf-8"); // http说明，我要返回JSON对象
		response.getWriter().print(new ObjectMapper().writeValueAsString(aj));
	}

}
