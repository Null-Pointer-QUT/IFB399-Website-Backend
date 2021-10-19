package com.pj.task;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import com.pj.auth.GatewayAuthUtil;
import com.pj.utils.LogUtil;
import com.pj.utils.sg.NbUtil;

/**
 * 网关鉴权token，定时刷新
 * @author Runtian
 *
 */
@Configuration
public class GatewayTokenRefreshTask {

	/**
	 * 刷新网关鉴权token  
	 */
	// 从 0 分钟开始 每隔 5 分钟执行一次 
	@Scheduled(cron = "0 0/5 * * * ? ")
	public void systemAuto_doCancel(){
		GatewayAuthUtil.refreshToken();
		LogUtil.info("============= 执行完毕：刷新网关鉴权token：" + NbUtil.getNow());
	}
	
	
	
}
