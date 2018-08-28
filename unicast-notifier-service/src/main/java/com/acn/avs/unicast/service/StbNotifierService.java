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

package com.acn.avs.unicast.service;


import java.util.List;

import com.acn.avs.unicast.entity.Notifications;
import com.acn.avs.unicast.entity.SetTopBoxes;
import com.acn.avs.unicast.entity.TriggerMapping;
import com.acn.avs.unicast.event.param.StbNotificationParam;


/**
 * @author Anand.Jha
 * 
 * The Class StbNotifierService.
 */

public interface StbNotifierService  {
	
	/**
	 * notify
	 * 
	 * @param stbNotificationParam
	 */
	public void notify(StbNotificationParam stbNotificationParam);
	
	/**
	 * getSetTopBoxes
	 * 
	 * @param subscriberId
	 * @return List<SetTopBoxes>
	 */
	List<SetTopBoxes> getSetTopBoxes(String subscriberId);
	
	/**
	 * getSetTopBoxPortAndProtocol
	 * 
	 * @param equipmentId
	 * @param serviceName
	 * @return SetTopBoxes
	 */
	SetTopBoxes getSetTopBoxPortAndProtocol(int equipmentId, String serviceName);
	
	/**
	 * getNotifications
	 * 
	 * @param subscriberId
	 * @return Notifications
	 */
	Notifications getNotifications(String subscriberId);
	
	/**
	 * getNotifications
	 * 
	 * @param subscriberId
	 * @param sourceMacAddress
	 * @return Notifications
	 */
	Notifications getNotifications(String subscriberId, String sourceMacAddress);
	
	/**
	 * getNotifications
	 * 
	 * @param subscriberId
	 * @param targetMacAddress
	 * @return Notifications
	 */
	Notifications getNotifications(String subscriberId, List<String> targetMacAddress);
	
	/**
	 * getUpdateEventAsJson
	 * 
	 * @param triggerType
	 * @param triggerInfo
	 * @return String
	 */
	String getUpdateEventAsJson(String triggerType, String triggerInfo);
	
	/**
	 * getServiceNameByTriggerType
	 * 
	 * @param triggerType
	 * @return String
	 */
	String getServiceNameByTriggerType(String triggerType);
	
	/**
	 * getTriggerMapping
	 * 
	 * @param triggerType
	 * @return TriggerMapping
	 */
	TriggerMapping getTriggerMapping(String triggerType);
	
	/**
	 * isMessageInfoTrigger
	 * 
	 * @param triggerType
	 * @return boolean
	 */
	boolean isMessageInfoTrigger(String triggerType);
	
	/**
	 * isImplicitNATMode
	 * 
	 * @param connectionMode
	 * @return boolean
	 */
	boolean isImplicitNATMode(int connectionMode);
	
	/**
	 * isHwVerionAssciateWithServiceName
	 * 
	 * @param hwVersion
	 * @param serviceName
	 * @return boolean
	 */
	boolean isHwVerionAssciateWithServiceName(String hwVersion, String serviceName);
	
	/**
	 * 
	 * @return
	 */
	int getConnectionIdForImplicitNAT();
}
