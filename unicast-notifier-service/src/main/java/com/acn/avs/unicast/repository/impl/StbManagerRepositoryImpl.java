package com.acn.avs.unicast.repository.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.acn.avs.unicast.entity.SetTopBoxes;
import com.acn.avs.unicast.repository.StbManagerRepository;

/**
 * StbManagerRepositoryImpl class is created to get SetTopBox table information
 * 
 */
@Repository
public class StbManagerRepositoryImpl implements StbManagerRepository {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StbManagerRepositoryImpl.class);
	
	@Autowired
	@Qualifier("stbmanagerJdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	private static final String IMPLICIT_NAT_QUERY = "select ID from CONNECTION_MODES where CONNECTION_MODE = 'Implicit NAT'";
	
	private static final String HW_SERVICE_QUERY = "SELECT COUNT(*) FROM SERVICE_HARDWARE_MAPPING MAPPING"+
			" WHERE MAPPING.HARDWARE_NAME = ? "+
			" AND MAPPING.SERVICE_ID = "+
			" (SELECT SERVICE_ID FROM DEFAULT_SERVICE_PORT_LINK WHERE"+
			" SERVICE_NAME = ?)";

	
	/**
	 * @param subscriberId
	 * 
	 * @return List<Settopbox>
	 */
	@Override
	public List<SetTopBoxes> getSetTopBoxes(String subscriberId) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("subscriberId: "+subscriberId);
		}
		//Excluding all STB's that does'nt have connection node
		String query = "select s.EQUIPMENT_ID as equipmentId, s.MAC_ADDRESS as "
						+ "macAddress, s.CONNECTION_MODE as connectionMode, s.EXTERNAL_IP_ADDRESS as externalIpAddress, s.HARDWARE_VERSION as hwversion from SET_TOP_BOXES s "
						+ "where s.ASSIGNEDTO_SUBSCRIBER_ID ='"+subscriberId+"' AND s.CONNECTION_MODE is not null " ;
				
		  return jdbcTemplate.query(query, new BeanPropertyRowMapper<SetTopBoxes>(SetTopBoxes.class));
	}
	
	/**
	 * @param subscriberId
	 * @param sourceMacAddress
	 * @return List<Settopbox>
	 */
	@Override
	public List<SetTopBoxes> getSetTopBoxes(String subscriberId, String sourceMacAddress) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("subscriberId: "+subscriberId+" , sourceMacAddress : "+sourceMacAddress );
		}
		//Excluding all STB's that does'nt have connection node 
		String query = "select s.EQUIPMENT_ID as equipmentId, s.MAC_ADDRESS as "
						+ "macAddress, s.CONNECTION_MODE as connectionMode, s.EXTERNAL_IP_ADDRESS as externalIpAddress, s.HARDWARE_VERSION as hwversion from SET_TOP_BOXES s where"
						+ " s.ASSIGNEDTO_SUBSCRIBER_ID ='"+subscriberId+"' and s.MAC_ADDRESS <> '"+sourceMacAddress+"' AND s.CONNECTION_MODE is not null";
				
		  return jdbcTemplate.query(query, new BeanPropertyRowMapper<SetTopBoxes>(SetTopBoxes.class));
	}
	
	/**
	 * @param subscriberId
	 * @param targetMacAddress
	 * @return List<Settopbox>
	 */
	@Override
	public List<SetTopBoxes> getSetTopBoxes(String subscriberId, List<String> targetMacAddress){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("subscriberId: "+subscriberId+" , targetMacAddress : "+targetMacAddress );
		}
		//Excluding all STB's that does'nt have connection node
		String query = "select s.EQUIPMENT_ID as equipmentId, s.MAC_ADDRESS as "
				+ "macAddress, s.CONNECTION_MODE as connectionMode, s.EXTERNAL_IP_ADDRESS as externalIpAddress, s.HARDWARE_VERSION as hwversion from SET_TOP_BOXES s where"
				+ " s.ASSIGNEDTO_SUBSCRIBER_ID ='"+subscriberId+"' and s.MAC_ADDRESS IN ('"
                        + StringUtils.join(targetMacAddress, ',').replaceAll(",", "','") + "')"
                        +" AND s.CONNECTION_MODE is not null";
		
			return jdbcTemplate.query(query, new BeanPropertyRowMapper<SetTopBoxes>(SetTopBoxes.class));
	}
	
	/**
	 * getSetTopBoxPortAndProtocol
	 * 
	 * @param equipmentId
	 * @param serviceName
	 * 
	 * @return SetTopBoxes
	 */
	@Override
	public SetTopBoxes getSetTopBoxPortAndProtocol(int equipmentId, String serviceName){
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("equipmentId: "+equipmentId+" , serviceName : "+serviceName );
		}
		String query  = "select ifnull(sm.EXTERNAL_PORT, fspm.DEFAULT_EXTERNAL_PORT) as externalPort, fspm.PROTOCOL as protocol from"
				+ " DEFAULT_SERVICE_PORT_LINK fspm left outer join"
				+ " STB_SERVICE_PORT_MAPPINGS sm on sm.SERVICE_ID = fspm.SERVICE_ID and sm.EQUIPMENT_ID ="+ equipmentId
				+ " where fspm.SERVICE_NAME='"+serviceName+"'";
		
		return jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<SetTopBoxes>(SetTopBoxes.class));
	}
	
	/**
	 * isHwVerionAssciateWithServiceName
	 * 
	 * @param hwVersion
	 * @param serviceName
	 * 
	 * @return 
	 */
	public boolean isHwVerionAssciateWithServiceName(String hwVersion, String serviceName) {
		if(LOGGER.isDebugEnabled()){
			LOGGER.debug("hwVersion: "+hwVersion+" , serviceName : "+serviceName );
		}

		Integer serviceHwMappingCount = jdbcTemplate.queryForObject(
				HW_SERVICE_QUERY, new Object[] { hwVersion, serviceName }, Integer.class);
		return (serviceHwMappingCount > 0) ? true: false;
	}

	/**
	 * 
	 * @return int
	 */
	public int getConnectionIdForImplicitNAT(){
		return jdbcTemplate.queryForObject(IMPLICIT_NAT_QUERY, Integer.class);
	}
	
}
