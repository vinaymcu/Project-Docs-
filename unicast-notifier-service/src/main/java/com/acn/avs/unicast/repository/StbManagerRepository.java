package com.acn.avs.unicast.repository;

import java.util.List;

import com.acn.avs.unicast.entity.SetTopBoxes;

/**
 * StbManagerRepository interface is created to get SetTopBox table information
 *
 */
public interface StbManagerRepository {

	/**
	 * getSetTopBoxes
	 * @param subscriberId
	 * @return List<SetTopBoxes>
	 */
	List<SetTopBoxes> getSetTopBoxes(String subscriberId);
	
	/**
	 * getSetTopBoxes
	 * @param subscriberId
	 * @param sourceMacAddress
	 * @return List<SetTopBoxes>
	 */
	List<SetTopBoxes> getSetTopBoxes(String subscriberId, String sourceMacAddress);
	
	/**
	 * getSetTopBoxes
	 * @param subscriberId
	 * @param targetMacAddress
	 * @return List<SetTopBoxes>
	 */
	List<SetTopBoxes> getSetTopBoxes(String subscriberId, List<String> targetMacAddress);
	
	/**
	 * getSetTopBoxPortAndProtocol
	 * @param equipmentId
	 * @param serviceName
	 * @return SetTopBoxes
	 */
	SetTopBoxes getSetTopBoxPortAndProtocol(int equipmentId, String serviceName);

	/**
	 * isHwVerionAssciateWithServiceName
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
