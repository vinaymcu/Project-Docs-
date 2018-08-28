package com.acn.avs.unicast.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Anand.Jha
 *
 */
@Entity
@Table(name = "TRIGGER_MESSAGE")
public class TriggerMessage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name="MACADDRESS")
	private String macAddress;
	
	@Column(name="TRIGGER_TYPE")
	private String triggerType;
	
	@Column(name="TIMESTAMP")
	private long timestamp;
	
	@Column(name="RETRY_COUNT")
	private int retryCount;
	
	@Column(name="ERROR_MESSAGE")
	private String errorMessage;
	
	@Column(name="INSTANCE_ID")
	private int instanceId;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public int getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}

	@Override
	public String toString() {
		return "TriggerMessage [id=" + id + ", macAddress=" + macAddress + ", triggerType=" + triggerType
				+ ", timestamp=" + timestamp + ", retryCount=" + retryCount + ", errorMessage=" + errorMessage
				+ ", instanceId=" + instanceId + "]";
	}
}
