package com.acn.avs.unicast.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Anand.Jha
 *
 */
@Component
@RefreshScope
public class ConfigurationProperties {
	
	/** Trigger server accessUrl */
	@Value("${tqs.server.accessUrl}")
	private String accessUrl;
	
	@Value("${stb.notification.timeout:2000}")
	private int stbNotificationTimeout;
	
	/**
	 * @return the accessUrl
	 */
	public String getAccessUrl() {
		return accessUrl;
	}
		
	/**
	 * @return the stbNotificationTimeout
	 */
	public int getStbNotificationTimeout() {
		return stbNotificationTimeout;
	}
}
