package com.pj.current.sentinel;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;

/**
 * Sentinel配置 
 * @author Runtian
 * 
 */
@Component
public class SentinelConfig {

	
	/**
	 * 注册Sentinel切面对象 
	 * @return
	 */
	@Bean
    public SentinelResourceAspect sentinelResourceAspect() {
//		initFlowRules();
        return new SentinelResourceAspect();
    }
	
	
	/**
	 * 初始化降级规则 
	 */
//	private static void initFlowRules() {
//		System.err.println("----------------------- 初始化限流规则 ！！！");
//		
//        List<FlowRule> rules = new ArrayList<>();
//
//        // 规则  qps > 1 时，触发降级 
//        FlowRule rule = new FlowRule();
//        rule.setResource("testQPS");
//        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
//        rule.setCount(1);
//        rules.add(rule);
//
//        // 可创建多个规则
//        // ...
//        
//        FlowRuleManager.loadRules(rules);
//    }
	
	
	
}
