/***************************************************************************
 * Copyright (C) Accenture
 *
 * The reproduction, transmission or use of this document or its contents is not permitted without
 * prior express written consent of Accenture. Offenders will be liable for damages. All rights,
 * including but not limited to rights created by patent grant or registration of a utility model or
 * design, are reserved.
 *
 * Accenture reserves the right to modify technical specifications and features.
 *
 * Technical specifications and features are binding only insofar as they are specifically and
 * expressly agreed upon in a written contract.
 *
 **************************************************************************/
package com.acn.avs.unicast.message;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import com.acn.avs.unicast.entity.Notifications;
import com.acn.avs.unicast.entity.SetTopBoxes;
import com.acn.avs.unicast.event.json.EventUpdate;
import com.acn.avs.unicast.event.json.EventUpdateRequest;
import com.acn.avs.unicast.event.param.StbNotificationParam;
import com.acn.avs.unicast.event.param.TqsNotificationParam;
import com.acn.avs.unicast.exception.UnicastRequestValidationException;
import com.acn.avs.unicast.service.StbNotifierService;
import com.acn.avs.unicast.service.TqsNotifierService;
import com.acn.avs.unicast.util.UnicastRequestValidator;

/**
 * @author Anand.Jha
 * 
 * This Class is rabbitmq messege listener
 */
@Component
public class UnicastJmsMessageListener {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(UnicastJmsMessageListener.class);

	@Autowired
	TqsNotifierService tqsNotifierService;

	@Autowired
	StbNotifierService stbNotifierService;
	
	@Autowired
	UnicastRequestValidator validator;
	
	 @Autowired
	 MessageSourceAccessor messageSourceAccessor;
	/**
	 * 
	 * Receive the JMS message
	 * 
	 * @param eventUpdate
	 * 
	 */
	public void onMessage(EventUpdateRequest eventUpdate) {
		LOGGER.info("onMessage (+)");
		
		notify(eventUpdate);
		
		LOGGER.info("onMessage (-)");
	}

	/**
	 * 
	 * @param eventUpdate
	 */
	private void notify (EventUpdateRequest eventUpdateRequest){
		if (!eventUpdateRequest.isValidated()){
			LOGGER.info("EventUpdateRequest through JMS: "+ eventUpdateRequest.getEventUpdate());
			try{
				validator.validate(eventUpdateRequest);
			}catch(UnicastRequestValidationException urve){
				String errorMsg = messageSourceAccessor.getMessage(urve.getErrorCode(),
						urve.getMsgParamsArray());
				LOGGER.error("Error occured while notify "+ errorMsg);
				return;
			}
		}
		EventUpdate eventUpdate = eventUpdateRequest.getEventUpdate();
		String subscriberId = eventUpdate.getSubscriberId();
		
		String triggerType = eventUpdate.getTriggerType();
		
		Notifications notifications = null;
		
		boolean isMacNotEmpty = CollectionUtils.isNotEmpty(eventUpdate.getMACAddress());
		boolean isMessageInfoTrigger = stbNotifierService.isMessageInfoTrigger(triggerType);
		try {

			if (isMacNotEmpty && !isMessageInfoTrigger) {
				String sourceMacAddress = eventUpdate.getMACAddress().get(0);
				notifications = stbNotifierService.getNotifications(
						subscriberId, sourceMacAddress);
			} else if (isMacNotEmpty && isMessageInfoTrigger) {
				notifications = stbNotifierService.getNotifications(
						subscriberId, eventUpdate.getMACAddress());
			} else {
				notifications = stbNotifierService
						.getNotifications(subscriberId);
			}

			notifyToTqs(notifications.getTqsNotifications(), triggerType,
					eventUpdate.getTimestamp());

			notifyToStb(notifications.getStbNotifications(), triggerType,
					eventUpdate.getTriggerInfo());

		}catch (Exception ex){
			LOGGER.error("Error occured while notify ", ex);
		}
	}
		

	/**
	 * Notify to Trigger server, initiate retry process if not
	 * 
	 * @param tqsNotificationBoxes
	 * @param triggerType
	 * @param timestamp
	 * 
	 * @return
	 */
	private void notifyToTqs(List<SetTopBoxes> tqsNotificationBoxes, String triggerType, String timestamp) {
		
		if (tqsNotificationBoxes.isEmpty()){
			return;
		}
		
		long  time = Long.parseLong(timestamp);
		
		for (SetTopBoxes setTopBox: tqsNotificationBoxes){
			tqsNotifierService.notify(new TqsNotificationParam(triggerType, setTopBox.getMacAddress(), time));
		}
	}

	/**
	 * Notify to STB
	 * 
	 * @param stbNotificationBoxes
	 * @param triggerType
	 * @param triggerInfo
	 * 
	 * @return
	 */
	private void notifyToStb(List<SetTopBoxes> stbNotificationBoxes,  String triggerType, String triggerInfo) {
		
		if (stbNotificationBoxes.isEmpty()){
			return;
		}
		
		String serviceName = stbNotifierService.getServiceNameByTriggerType(triggerType);
		
		String updateEvent = stbNotifierService.getUpdateEventAsJson(triggerType, triggerInfo);
		
		for (SetTopBoxes setTopBox: stbNotificationBoxes){
			SetTopBoxes setTopBoxWithPortAndProtocol  = stbNotifierService.getSetTopBoxPortAndProtocol(setTopBox.getEquipmentId(), serviceName);
			
			StbNotificationParam stbNotificationParam = new StbNotificationParam(
					setTopBox.getMacAddress(),
					updateEvent,
					setTopBox.getExternalIpAddress(),
					setTopBoxWithPortAndProtocol.getExternalPort(),
					setTopBoxWithPortAndProtocol.getProtocol());
			
			stbNotifierService.notify(stbNotificationParam);
		}
	}
	
	

}
