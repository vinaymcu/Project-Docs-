package com.acn.avs.unicast.event.param;


/**
 * 
 * @author Anand.Jha
 *
 */
public class TqsNotificationParam {
	
	private final String triggerType;
	
	private final String macAddress;
	
	private final long timestamp;
	
	/**
	 * 
	 * @param triggerType
	 * @param macAddress
	 * @param timestamp
	 */
	public TqsNotificationParam(String triggerType, String macAddress, long timestamp){
		this.triggerType = triggerType;
		this.macAddress  = macAddress;
		this.timestamp = timestamp;
	}
	
	/**
	 * @return the triggerType
	 */
	public String getTriggerType() {
		return triggerType;
	}

	/**
	 * @return the macAddress
	 */
	public String getMacAddress() {
		return macAddress;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Overriding toString to get class variable information 
	 * @return String
	 */
	public String toString(){
		return "triggerType = "+ triggerType+ ", macAddress = "+ macAddress+ ", timestamp = "+ timestamp;
	}
	
}
