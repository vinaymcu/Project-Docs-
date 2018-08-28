/***************************************************************************
 * Copyright (C) Accenture
 *
 * The reproduction, transmission or use of this document or its contents is not permitted without
 * prior express written consent of Accenture. Offenders will be liable for damages. All rights,
 * including but not limited to rights created by patent grant or registration of a utility model or
 * design, are reserved.
 *
 * Accenture reserves the right to modify technical specifications and features.
 *
 * Technical specifications and features are binding only insofar as they are specifically and
 * expressly agreed upon in a written contract.
 *
 **************************************************************************/
package com.acn.avs.unicast.event.json;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class EventUpdate.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "TriggerType", "SubscriberId", "MACAddress", "TriggerInfo", "Timestamp" })
public class EventUpdate implements Serializable {

  private static final long serialVersionUID = 6570208075940284928L;
  
  /** The trigger type. */
  @JsonProperty(value = "TriggerType", required = true)
  private String triggerType;
  
  /** The subscriber id. */
  @JsonProperty(value = "SubscriberId", required = true)
  private String subscriberId;
  
  /** The m AC address. */
  @JsonProperty("MACAddress")
  private List<String> macAddress;
  
  /** The trigger info. */
  @JsonProperty("TriggerInfo")
  private String triggerInfo;
  
  /** The timestamp. */
  @JsonProperty(value = "Timestamp", required = true)
  private String timestamp;
  
  /**
   * Gets the MAC address.
   *
   * @return The mACAddress
   */
  public List<String> getMACAddress() {
    return macAddress;
  }
  
  /**
   * Gets the subscriber id.
   *
   * @return The subscriberId
   */
  public String getSubscriberId() {
    return subscriberId;
  }
  
  /**
   * Gets the timestamp.
   *
   * @return The timestamp
   */
  public String getTimestamp() {
    return timestamp;
  }
  
  /**
   * Gets the trigger info.
   *
   * @return The triggerInfo
   */
  public String getTriggerInfo() {
    return triggerInfo;
  }
  
  /**
   * Gets the trigger type.
   *
   * @return The triggerType
   */
  public String getTriggerType() {
    return triggerType;
  }
  
  
  /**
   * Sets the MAC address.
   *
   * @param mACAddress The MACAddress
   */
  public void setMACAddress(final List mACAddress) {
    this.macAddress = mACAddress;
  }
  
  /**
   * Sets the subscriber id.
   *
   * @param subscriberId The SubscriberId
   */
  public void setSubscriberId(final String subscriberId) {
    this.subscriberId = subscriberId;
  }
  
  /**
   * Sets the timestamp.
   *
   * @param timestamp The Timestamp
   */
  public void setTimestamp(final String timestamp) {
    this.timestamp = timestamp;
  }
  
  /**
   * Sets the trigger info.
   *
   * @param triggerInfo The TriggerInfo
   */
  public void setTriggerInfo(final String triggerInfo) {
    this.triggerInfo = triggerInfo;
  }
  
  /**
   * Sets the trigger name.
   *
   * @param triggerType
   */
  public void setTriggerType(final String triggerType) { 
    this.triggerType = triggerType;
  }
  
  @Override
  public String toString(){
	  return "triggerType  = "+ triggerType+ ", macAddress = "+ macAddress + ", subscriberId = "+ subscriberId+ ", timestamp = "+ timestamp+", triggerInfo = "+ triggerInfo;
  }
  
}
