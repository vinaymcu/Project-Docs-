package com.acn.avs.unicast.logging;

/**
 * The model class for logging parameter holding.
 *
 * @version 1.0
 * @since 1.0
 *
 *
 */
public class LoggingParameter {

	/** The api. */
	private String api;

	/** The device type. */
	private String deviceType;

	/** The channel. */
	private String channel;

	/** The sid. */
	private String sid;

	/** The tenant id. */
	private String tenantId;

	/** The provider name. */
	private String providerName;

	/** The service name. */
	private String serviceName;

	/** The ms id. */
	private String msId;

	/** The tn. */
	private String tn;

	/** The timestamp. */
	private final long timestamp;

	/**
	 * Instantiates a new logging parameter.
	 */
	public LoggingParameter() {
		super();
		timestamp = System.currentTimeMillis();
	}

	/**
	 * Gets the api.
	 *
	 * @return the api
	 */
	public String getApi() {
		return api;
	}

	/**
	 * Sets the api.
	 *
	 * @param api
	 *            the new api
	 */
	public void setApi(String api) {
		this.api = api;
	}

	/**
	 * Gets the device type.
	 *
	 * @return the device type
	 */
	public String getDeviceType() {
		return deviceType;
	}

	/**
	 * Sets the device type.
	 *
	 * @param deviceType
	 *            the new device type
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	/**
	 * Gets the channel.
	 *
	 * @return the channel
	 */
	public String getChannel() {
		return channel;
	}

	/**
	 * Sets the channel.
	 *
	 * @param channel
	 *            the new channel
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}

	/**
	 * Gets the sid.
	 *
	 * @return the sid
	 */
	public String getSid() {
		return sid;
	}

	/**
	 * Sets the sid.
	 *
	 * @param sid
	 *            the new sid
	 */
	public void setSid(String sid) {
		this.sid = sid;
	}

	/**
	 * Gets the tenant id.
	 *
	 * @return the tenant id
	 */
	public String getTenantId() {
		return tenantId;
	}

	/**
	 * Sets the tenant id.
	 *
	 * @param tenantId
	 *            the new tenant id
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * Gets the provider name.
	 *
	 * @return the provider name
	 */
	public String getProviderName() {
		return providerName;
	}

	/**
	 * Sets the provider name.
	 *
	 * @param providerName
	 *            the new provider name
	 */
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	/**
	 * Gets the service name.
	 *
	 * @return the service name
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * Sets the service name.
	 *
	 * @param serviceName
	 *            the new service name
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * Gets the tn.
	 *
	 * @return the tn
	 */
	public String getTn() {
		return tn;
	}

	/**
	 * Sets the tn.
	 *
	 * @param tn
	 *            the new tn
	 */
	public void setTn(String tn) {
		this.tn = tn;
	}

	/**
	 * Gets the ms id.
	 *
	 * @return the ms id
	 */
	public String getMsId() {
		return msId;
	}

	/**
	 * Sets the ms id.
	 *
	 * @param msId
	 *            the new ms id
	 */
	public void setMsId(String msId) {
		this.msId = msId;
	}

	/**
	 * Gets the timestamp.
	 *
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LoggingParameter [api=" + api + ", deviceType=" + deviceType + ", channel=" + channel + ", sid=" + sid
				+ ", tenantId=" + tenantId + ", providerName=" + providerName + ", serviceName=" + serviceName
				+ ", msId=" + msId + ", tn=" + tn + ", timestamp=" + timestamp + "]";
	}

}
