package com.acn.avs.unicast.event.json;

import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author Anand.Jha
 *
 */
public class UpdateEvent {
	 
	@SerializedName("TriggerType") 
	private final String triggerType;
	 
	@SerializedName("TriggerInfo") 
	private final String triggerInfo;
	 
	/**
	 * UpdateEvent class constructor
	 * @param triggerType
	 * @param triggerInfo
	 */
	public UpdateEvent(String triggerType, String triggerInfo){
		this.triggerType = triggerType;
		this.triggerInfo = triggerInfo;
	}
}
