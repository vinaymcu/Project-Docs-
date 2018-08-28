package com.acn.avs.unicast.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.acn.avs.unicast.entity.TriggerMessage;

/**
 * 
 * @author Anand.Jha
 *
 */
@Repository
@Transactional(readOnly=true)
public interface UnicastTriggerMessageRepository extends JpaRepository<TriggerMessage, Integer> {

	/**
	 * find Trigger Messages by MacAddress
	 * @param macAddress
	 * @return TriggerMessage
	 */
	TriggerMessage findByMacAddress(String macAddress);

	/**
	 * Update retry count by Id
	 * @param retryCount
	 * @param id
	 * 
	 * @return
	 */
	@Modifying(clearAutomatically = true)
	@Transactional(readOnly=false)
	@Query("UPDATE TriggerMessage tm set tm.retryCount =:retryCount where tm.id =:id")
	void setRetryCountById(@Param("retryCount") int retryCount, @Param("id") long id);

	/**
	 * Update Trigger Message by serviceId
	 * @param instanceId
	 * @param instanceIds
	 * 
	 * @return int
	 */
	@Modifying(clearAutomatically = true)
	@Transactional(readOnly=false)
	@Query("UPDATE TriggerMessage tm set tm.instanceId =:instanceId where tm.instanceId not in (:instanceIds)")
	int updateByInstanceId(@Param("instanceId") int instanceId, @Param("instanceIds") List<Integer> instanceIds);

	/**
	 * Delete Trigger Message by id
	 * @param id
	 * 
	 * @return 
	 */
	@Modifying(clearAutomatically = true)
	@Transactional(readOnly=false)
	@Query("DELETE from TriggerMessage WHERE id =:id")
	void deleteById(@Param("id") long id);

	/**
	 * find Trigger Messages by InstanceId
	 * @param instanceId
	 * 
	 * @return List<TriggerMessage>
	 */
	List<TriggerMessage> findByInstanceId(int instanceId);

}
