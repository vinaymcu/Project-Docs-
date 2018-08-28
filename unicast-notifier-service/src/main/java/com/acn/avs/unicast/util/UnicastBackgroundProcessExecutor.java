package com.acn.avs.unicast.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.acn.avs.unicast.entity.TriggerMessage;
import com.acn.avs.unicast.event.param.TqsNotificationParam;
import com.acn.avs.unicast.repository.UnicastTriggerMessageRepository;
import com.acn.avs.unicast.service.TqsNotifierService;

/**
 * @author Anand.Jha
 * 
 * This class is responsible to execute Background Process
 * Background process tasks:
 * a) Insert failed message to Trigger_Message table
 * b) Query the DB table “TRIGGER_MESSAGE” for any failed messages.
 * c) Repeat the cycle of resending message to TQS again for all the requests in DB
 * d) If resending of requests fail again than retry till configurable times
 * e) If resending of requests fail again after configurable tries then the corresponding records removed from DB
 * f) If success than also remove the data from DB
 * 
 */

@Component
public class UnicastBackgroundProcessExecutor {

	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = Logger.getLogger(UnicastBackgroundProcessExecutor.class);

	/**
	 * retry count value from properties file
	 */
	@Value("${tqs.server.maxRetries:0}")
	private int maxRetryAccessCount;

	/**
	 * TqsNotifierService
	 */
	@Autowired
	TqsNotifierService tqsNotifierService;

	/**
	 * fixedDelay to start background process
	 */
	@Value("${background.process.repeatInterval:5}")
	private int repeatInterval;

	/**
	 * serverPort value
	 */
	@Value("${server.port:0}")
	private int serverPort;
	
	/** ssl port */
	@Value("${ssl.port:0}")
	private int sslPort;
	
	/**
	 * serviceAppName
	 */
	@Value("${spring.application.name}")
	private String serviceAppName;
	
	
	/**
	 * ScheduledExecutorService
	 */
	ScheduledExecutorService  scheduledExecutorService;

	/**
	 * UnicastTriggerMessageRepository
	 */
	@Autowired
	UnicastTriggerMessageRepository unicastTriggerMessageRepository;
	
	/**
	 * isSchedulerStarted boolean flag to check is scheduler started or not
	 */
	private boolean isSchedulerStarted;
	
	/**
	 * instanceId of currently running server
	 */
	private int instanceId;
	
	@Autowired
	private DiscoveryClient discoveryClient;
	
	/**
	 * 
	 */
	@PostConstruct
	void assignInstanceId(){
		String host = null;
		try {
			host = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Error occured while getting host : "+ e);
		}
		
		instanceId = this.generateServiceId(host);
		
		if (isAnyTriggerMessagePresisted()){
			initiateScheduler();
		}
	}

	/**
	 * persist Messages and Initiate the Scheduler thread
	 * 
	 * @param tqsNotificationParam
	 * @param errorMessage
	 * 
	 * @return
	 * 
	 */
	public void persistMessagesAndInitiateScheduler(TqsNotificationParam tqsNotificationParam, String errorMessage) {
	
		if(maxRetryAccessCount != 0){
			persistMessages(tqsNotificationParam, errorMessage);
			
			if(!isSchedulerStarted ) {
				initiateScheduler();
			}
		}
	}
	
	
	/**
	 * Initiate Scheduler with fixed delay
	 */
	public void initiateScheduler() {
		LOGGER.info("(+)initiateScheduler with fixedDelay : "+ repeatInterval + " minutes.");
		
		LOGGER.info("maxRetryAccessCount = "+ maxRetryAccessCount);

		if(scheduledExecutorService == null || scheduledExecutorService.isShutdown()){
			scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		}else {
			scheduledExecutorService.shutdownNow();
			scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		}

		scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {

			@Override 
			public void run() {
				try{
					process();
				} catch(Exception e){
					LOGGER.error("error occured while retry process", e);
				}
			}
		}, repeatInterval ,repeatInterval , TimeUnit.MINUTES);

		isSchedulerStarted = true;
		
		LOGGER.info("(-)initiateScheduler");
	}

	/**
	 * Process all the notification from Database and retry till configurable times and update the retry value into Database.
	 * Finally if all the data process complete stop the scheduler
	 *  
	 */
	private void process() {
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("(+) process");
		}

		List<TriggerMessage> triggerMessages = this.getTriggerMessages();
		
		if (this.getTriggerMessagesSize(triggerMessages) == 0){
			stopProcess();
		}

		for(TriggerMessage triggerMessage : triggerMessages) {

			if (tqsNotifierService.notify(triggerMessage) == HttpStatus.OK.value()) {
				deleteMessageById(triggerMessage.getId());
			} else {
				int retryCount = triggerMessage.getRetryCount() + 1;

				if (retryCount >= maxRetryAccessCount ){
					deleteMessageById(triggerMessage.getId());
					LOGGER.error("TQS server is not reachable even after max retries ("+maxRetryAccessCount+"), so deleteing Trigger[ MacAddress:"+ triggerMessage.getMacAddress() +", TriggerType: "+ triggerMessage.getTriggerType()+ "].");
				}else {
					updateRetryCount(triggerMessage, retryCount);
				}
			}
		}
		
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("(-) process");
		}
	}

	/**
	 * persist Trigger Messages
	 * 
	 * @param tqsNotificationParam
	 * @param errorMessage
	 * 
	 * @return 
	 */
	private void persistMessages(TqsNotificationParam tqsNotificationParam,
			String errorMessage) {
		
		LOGGER.info("persistMessages for the macAddress : "
				+tqsNotificationParam.getMacAddress());
		
			TriggerMessage triggerMessage = new TriggerMessage();
			triggerMessage.setMacAddress(tqsNotificationParam.getMacAddress());
			triggerMessage.setTriggerType(tqsNotificationParam.getTriggerType());
			triggerMessage.setTimestamp(tqsNotificationParam.getTimestamp());
			triggerMessage.setErrorMessage(errorMessage);
			triggerMessage.setRetryCount(0);
			triggerMessage.setInstanceId(instanceId);
			unicastTriggerMessageRepository.save(triggerMessage);
		
	}

	/**
	 * Update RetryCount in database
	 *  
	 * @param triggerMessage
	 * @param retryCount
	 * 
	 * @return
	 */
	public void updateRetryCount(TriggerMessage triggerMessage,  int retryCount) {
		
			LOGGER.info("updateRetryCount for the macAddress : "
					+triggerMessage.getMacAddress() + " with retryCount: "+ retryCount);
			unicastTriggerMessageRepository.setRetryCountById(retryCount, triggerMessage.getId());
	}
	
	/**
	 * deleteMessageById
	 * 
	 * @param id
	 * @return
	 */
	private void deleteMessageById(long id){
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("(+)deleting trigger messege for  : "+id);
		}
		unicastTriggerMessageRepository.deleteById(id);
		
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("(-)trigger messege deleted for  : "+id);
		}
	}
	
	/**
	 * stopProcess
	 * @return
	 */
	private void stopProcess() {
		LOGGER.info("(+)stopProcess");
		int updateCount = unicastTriggerMessageRepository.updateByInstanceId(instanceId, getInstanceIdsByDiscoveryClient());
		if(updateCount == 0) {
			scheduledExecutorService.shutdown();
			isSchedulerStarted = false;
			LOGGER.info("Scheduler has been stopped!!!");
		}else {
			LOGGER.info("Scheduler would not stopped because some trigger still to be process.");
		}

		LOGGER.info("(-)stopProcess");
	}

	/**
	 * getServiceIdsByDiscoveryClient
	 * 
	 * @return List
	 */
	private List<Integer> getInstanceIdsByDiscoveryClient() {
		
		List<Integer> serviceIds = new ArrayList<Integer>();
		List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances(serviceAppName);
		
		for (ServiceInstance serviceInstance : serviceInstanceList)	{
			serviceIds.add(generateServiceId(serviceInstance.getHost(),serviceInstance.getPort()));
		}
		LOGGER.info("serviceIds = "+ serviceIds);
		return serviceIds;
	}
	
	/**
	 * generateServiceId
	 * 
	 * @param port
	 * @param host
	 * 
	 * @return int
	 */
	private int generateServiceId(String host, Integer port) {
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("host : "+ host+ ", port : "+ port);
		}
		int prime = 31;
		int result = 1;
		result = result*prime + host.hashCode();
		result = result*prime + port.hashCode();
		
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("generateServiceId "+ result);
		}
		return result;
	}
	
	/**
	 * generateServiceId
	 * 
	 * @param host
	 * @return
	 */
	private int generateServiceId(String host){
		if (serverPort>0){
			return 	this.generateServiceId(host, serverPort);
		}else {
			if (LOGGER.isInfoEnabled()){
				LOGGER.info("sslport is being used to generate the service instance id.");
			}
			return 	this.generateServiceId(host, sslPort);
		}
	}
	/**
	 * getTriggerMessages
	 * 
	 * @return List
	 */
	private List<TriggerMessage> getTriggerMessages(){
		
		return unicastTriggerMessageRepository.findByInstanceId(instanceId);
	}
	
	/**
	 * 
	 * @param triggerMessages
	 * @return
	 */
	private int getTriggerMessagesSize(List<TriggerMessage> triggerMessages){
		
		int size = triggerMessages.size();
		
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("Failed TriggerMessages list size : "+ size);
		}
		return size;
	}
	
	/**
	 * isAnyTriggerMessagePresisted
	 * 
	 * @return
	 */
	private boolean isAnyTriggerMessagePresisted(){
		return CollectionUtils.isNotEmpty(unicastTriggerMessageRepository.findAll());
	}
}
