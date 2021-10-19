package com.pj.current;

import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.pj.utils.sg.NbUtil;

/**
 * springboot启动之后 
 */
@Order(Integer.MAX_VALUE)
@Component
public class SpCloudUtil {
    
	@Value("${server.port:8080}")
    private String port;

    @Value("${server.servlet.context-path:}")
    private String path;

    @Value("${spring.application.name:}")
    private String applicationName;

    @Value("${spring.profiles.active:}")
    private String active;
   
    
    public void run() throws Exception {
        String ip = InetAddress.getLocalHost().getHostAddress();
        String str = "\n------------- " + applicationName + " (" + active + ") 启动成功 --by " + NbUtil.getNow() + " -------------\n" + 
                "\t- Local:   http://localhost:" + port + path + "\n" +
                "\t- Local2:  http://127.0.0.1:" + port + path + "\n" +
                "\t- Network: http://" + ip + ":" + port + path + "\n";
        System.out.println(str);
    }



    
    static SpCloudUtil saPlusStartup;
    @Autowired
	public void setSaPlusStartup(SpCloudUtil saPlusStartup) {
    	SpCloudUtil.saPlusStartup = saPlusStartup;
	}
    
    
    /**
     * 打印当前服务信息 
     */
    public static void printCurrentServiceInfo() {
    	try {
    		saPlusStartup.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    

}

