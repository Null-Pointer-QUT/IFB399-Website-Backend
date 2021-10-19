package com.pj.project.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.pj.utils.sg.AjaxJson;

/**
 * 测试controller 
 * @author Runtian
 */
@RestController
public class TestController {

	
	/**
	 * 测试请求，如果能正常访问此路由，则证明项目已经部署成功 
	 * @return
	 */
	@RequestMapping("/test")
	public AjaxJson test() {
		System.out.println("------------------ 成功进入请求 ------------------");
		return AjaxJson.getSuccess("请求成功");
	}

	/**
	 * 测试sentinel，qps过多时触发服务器繁忙 
	 * @return
	 */
	@RequestMapping("/testQPS")
	@SentinelResource("testQPS")
	public AjaxJson testQPS() {
		System.out.println("------------------ 成功进入请求 ------------------");
		return AjaxJson.getSuccess("请求成功, 正常返回");
	}
	
	
	
}
