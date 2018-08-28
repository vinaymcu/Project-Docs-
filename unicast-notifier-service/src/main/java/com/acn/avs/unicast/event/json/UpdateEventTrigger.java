package com.acn.avs.unicast.event.json;

import com.acn.avs.unicast.event.json.UpdateEvent;
import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author Anand.Jha
 *
 */
public class UpdateEventTrigger {
	
	@SerializedName("UpdateEvent") 
	private final UpdateEvent updateEvent;

	/**
	 * @return the updateEvent
	 */
	public UpdateEvent getUpdateEvent() {
		return updateEvent;
	}

	/**
	 * UpdateEventTrigger class constructor
	 * @param triggerType
	 * @param triggerInfo
	 */
	public UpdateEventTrigger(String triggerType, String triggerInfo){
		this.updateEvent = this.createUpdateEvent(triggerType, triggerInfo);
	}
	
	/**
	 * create UpdateEvent
	 * @param triggerType
	 * @param triggerInfo
	 * @return
	 */
	public UpdateEvent createUpdateEvent(String triggerType, String triggerInfo){
		return new UpdateEvent(triggerType,triggerInfo );
	}
	
}
