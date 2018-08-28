package com.acn.avs.unicast.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.acn.avs.unicast.entity.TriggerMapping;

/**
 * 
 * UnicastTriggerMappingRepository
 *
 */
@Repository
@Transactional(readOnly=true)
public interface UnicastTriggerMappingRepository extends JpaRepository<TriggerMapping, String> {
	
	/**
	 * findByTriggerType
	 * 
	 * @param triggerType
	 * @return TriggerMapping
	 */
	TriggerMapping findByTriggerType(String triggerType);

}
