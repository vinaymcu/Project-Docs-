package com.acn.avs.unicast.service.impl;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.acn.avs.unicast.config.ConfigurationProperties;
import com.acn.avs.unicast.entity.TriggerMessage;
import com.acn.avs.unicast.event.param.TqsNotificationParam;
import com.acn.avs.unicast.service.TqsNotifierService;
import com.acn.avs.unicast.util.UnicastBackgroundProcessExecutor;

/**
 * 
 * @author Anand.Jha
 *
 *This class is responsible to notify the trigger to trigger server through http
 */
@Service
public class TqsNotifierServiceImpl implements TqsNotifierService {

	/** DOCUMENT ME! */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TqsNotifierService.class);

	@Autowired
	UnicastBackgroundProcessExecutor unicastBackgroundProcessExecutor;

	@Autowired
	private RestTemplate restTemplate;  
	
	@Autowired
	ConfigurationProperties configurationProperties;
	
	/**
	 * Send the write trigger notification request to TQS in a synchronizes
	 * manner, as there could me multiple concurrent notifications can trigger
	 * from multiple threads.
	 * 
	 * @param macAddress
	 * @param triggerType
	 * @param time
	 * 
	 * @throws RestClientException,  URISyntaxException
	 * 
	 * @return int
	 */
	private int notify(String macAddress, String triggerType, long time) throws 
							RestClientException, URISyntaxException {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("TqsNotifier: notify(+)");
		}
	

			String accessUrl = configurationProperties.getAccessUrl();
			
			if (StringUtils.isEmpty(accessUrl)) {
				LOGGER.error("Trigger Server's accessUrl is empty or null.");
				return HttpStatus.BAD_GATEWAY.value();
			}

			URI uri = new URI(accessUrl + "?MACAddress=" + macAddress
					+ "&TriggerType=" + triggerType + "&TimeStamp=" + time);

			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("----------- Sending write trigger request to TQS -------------");
				LOGGER.info("TriggerServer URL: " + uri.toString());
			}
			int statusCode = -1;
			
			synchronized(this) {
				ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
				statusCode = response.getStatusCode().value();
			}
		return statusCode;


	} // end method notify


	/**
	 * Send the write trigger notification request to TQS and
	 * start background retry process if reponse code is not 200.
	 * 
	 * @param tqsNotificationParam
	 * return 
	 */
	@Override
	public void notify(TqsNotificationParam tqsNotificationParam) {
		
		int responseCode = 0;
		String errorMessage = null;
		try{
			responseCode  = notify(tqsNotificationParam.getMacAddress(), tqsNotificationParam.getTriggerType(),
					tqsNotificationParam.getTimestamp());
		}
		catch (RestClientException | URISyntaxException ex) {
			LOGGER.error("Error occured while notify the Trigger Server", ex);
			errorMessage = ex.getMessage();
			
		} 
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("responseCode : "+ responseCode);
		}

		if(responseCode != HttpStatus.OK.value()){
			unicastBackgroundProcessExecutor.persistMessagesAndInitiateScheduler(tqsNotificationParam, 
					errorMessage);
		}
		
	}
	
	/**
	 * 
	 * notify TriggerMessage from database
	 * 
	 * @param triggerMessage
	 * 
	 * @return 
	 */
	@Override
	public int notify(TriggerMessage triggerMessage) {
		int responseCode = 0;
		try{
			responseCode  = notify(triggerMessage.getMacAddress(), triggerMessage.getTriggerType(),
					triggerMessage.getTimestamp());
		}
		catch (RestClientException | URISyntaxException ex) {
			LOGGER.error("Error occured while notify the Trigger Server", ex);
			triggerMessage.setErrorMessage(ex.getMessage());
			
		} 
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("responseCode : "+ responseCode);
		}
		return responseCode;
	}
}
