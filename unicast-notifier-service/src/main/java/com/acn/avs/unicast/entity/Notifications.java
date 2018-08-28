package com.acn.avs.unicast.entity;

import java.util.List;

/**
 * 
 * @author Anand.Jha
 *
 */
public class Notifications {
	
	private final List<SetTopBoxes> tqsNotifications;
	
	private final List<SetTopBoxes> stbNotifications;
	
	/**
	 * getTqsNotifications
	 * @return the tqsNotificationBoxes
	 */
	public List<SetTopBoxes> getTqsNotifications() {
		return tqsNotifications;
	}

	/**
	 * getStbNotifications
	 * @return the stbNotificationBoxes
	 */
	public List<SetTopBoxes> getStbNotifications() {
		return stbNotifications;
	}
	
	/**
	 * Notifications class constructor
	 * @param tqsNotifications 
	 * @param stbNotifications 
	 */
	public  Notifications(List<SetTopBoxes> tqsNotifications, List<SetTopBoxes> stbNotifications) {
		this.tqsNotifications = tqsNotifications;
		this.stbNotifications = stbNotifications;
	}
	
}
