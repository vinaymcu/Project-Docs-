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

import com.acn.avs.unicast.entity.TriggerMessage;
import com.acn.avs.unicast.event.param.TqsNotificationParam;

/**
 * @author Anand.Jha
 * 
 * The Class TqsNotifierService.
 */

public interface TqsNotifierService  {
	
	/**
	 * notify tqs server
	 * 
	 * @param tqsNotificationParam
	 */
	public void notify(TqsNotificationParam tqsNotificationParam);
	
	/**
	 * notify trigger message to tqs server
	 * 
	 * @param triggerMessage
	 * @return
	 */
	public int notify(TriggerMessage triggerMessage);
	
}
