package com.acn.avs.unicast.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TriggerMapping Entity
 *
 */
@Entity
@Table(name = "TRIGGER_MAPPING")
public class TriggerMapping {

	@Id
	@Column(name="TRIGGER_TYPE")
	private String triggerType;
	
	@Column(name="SERVICE_NAME")
	private String serviceName;

	
	public String getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	@Override
	public String toString() {
		return "[triggerType=" + triggerType + ", serviceName=" + serviceName + "]";
	}
}
