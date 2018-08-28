package com.acn.avs.unicast.event.param;

/**
 * 
 * @author Anand.Jha
 *
 */
public class StbNotificationParam {
	
	private final String macAddress;
	
	private final String updateEvent;
	
	private final String stbHost;
	
	private final int stbPort;
	
	private final String stbProtocol;
	
	/**
	 * StbNotificationParam class constructor
	 * @param macAddress
	 * @param updateEvent
	 * @param stbHost
	 * @param stbPort
	 * @param stbProtocol
	 */
	public StbNotificationParam(String macAddress, String updateEvent, String stbHost, int stbPort, String stbProtocol){
		this.macAddress = macAddress;
		this.updateEvent = updateEvent;
		this.stbHost = stbHost;
		this.stbPort = stbPort;
		this.stbProtocol = stbProtocol;
	}
	
	/**
	 * @return the macAddress
	 */
	public String getMacAddress() {
		return macAddress;
	}
	
	/**
	 * @return the updateEvent
	 */
	public String getUpdateEvent() {
		return updateEvent;
	}

	/**
	 * @return the stbHost
	 */
	public String getStbHost() {
		return stbHost;
	}

	/**
	 * @return the stbPort
	 */
	public int getStbPort() {
		return stbPort;
	}
	
	/**
	 * @return the stbProtocol
	 */
	public String getStbProtocol() {
		return stbProtocol;
	}

}
