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
package com.acn.avs.unicast.entity;

import java.io.Serializable;

/**
 * The Class Settopbox.
 *
 * @author happy.dhingra
 */
public class SetTopBoxes implements Serializable {
  
  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 123658495L;
  
  /** The equipment id. */
  private int equipmentId;
  
 /** The connection mode. */
  private int connectionMode;
  
  /** The stb ip address. */
  private String externalIpAddress;
  
  /** The stb ip address. */
  private String macAddress;
  
  /** The assigned subs id. */
  private int assignedSubsId;
  
  /** The port opa. */
  private int externalPort;
  
  private String protocol;
  
  private String hwversion;
    
/**
 * @return the protocol
 */
public String getProtocol() {
	return protocol;
}

/**
 * @param protocol the protocol to set
 */
public void setProtocol(String protocol) {
	this.protocol = protocol;
}

/**
 * @return the externalPort
 */
public int getExternalPort() {
	return externalPort;
}

/**
 * @param externalPort the externalPort to set
 */
public void setExternalPort(int externalPort) {
	this.externalPort = externalPort;
}

/**
   * Instantiates a new settopbox.
   */
  public SetTopBoxes() {
	  //for junit test cases
  }
  
  /**
   * Instantiates a new settopbox.
   *
   * @param equipmentId the equipment id
   */
  public SetTopBoxes(int equipmentId) {
    this.equipmentId = equipmentId;
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are
    // not set
    if (!(object instanceof SetTopBoxes)) {
      return false;
    }
    SetTopBoxes other = (SetTopBoxes) object;
    if (this.equipmentId == other.equipmentId) {
      return true;
    }
    return false;
  }
  
  @Override
  public int hashCode() {
  	final int prime = 31;
  	int result = 1;
  	result = prime * result + assignedSubsId;
  	result = prime * result + equipmentId;
  	result = prime * result + ((macAddress == null) ? 0 : macAddress.hashCode());
  	return result;
  }
  
  
  /**
   * @return the equipmentId
   */
  public int getEquipmentId() {
  	return equipmentId;
  }

  /**
   * @param equipmentId the equipmentId to set
   */
  public void setEquipmentId(int equipmentId) {
  	this.equipmentId = equipmentId;
  }

  /**
   * @return the connectionMode
   */
  public int getConnectionMode() {
  	return connectionMode;
  }

  /**
   * @param connectionMode the connectionMode to set
   */
  public void setConnectionMode(int connectionMode) {
  	this.connectionMode = connectionMode;
  }

  /**
   * @return the externalIpAddress
   */
  public String getExternalIpAddress() {
  	return externalIpAddress;
  }

  /**
   * @param externalIpAddress the externalIpAddress to set
   */
  public void setExternalIpAddress(String externalIpAddress) {
  	this.externalIpAddress = externalIpAddress;
  }

  /**
   * @return the macAddress
   */
  public String getMacAddress() {
  	return macAddress;
  }

  /**
   * @param macAddress the macAddress to set
   */
  public void setMacAddress(String macAddress) {
  	this.macAddress = macAddress;
  }

  /**
   * @return the assignedSubsId
   */
  public int getAssignedSubsId() {
  	return assignedSubsId;
  }

  /**
   * @param assignedSubsId the assignedSubsId to set
   */
  public void setAssignedSubsId(int assignedSubsId) {
  	this.assignedSubsId = assignedSubsId;
  }

  
  public String getHwversion() {
	  return hwversion;
  }
  
  /**
   * @param hwversion to set
   */
  public void setHwversion(String hwversion) {
	  this.hwversion = hwversion;
  }

	@Override
	public String toString() {
		return "SetTopBoxes [equipmentId=" + equipmentId + ", connectionMode=" + connectionMode + ", externalIpAddress="
				+ externalIpAddress + ", macAddress=" + macAddress + ", assignedSubsId=" + assignedSubsId
				+ ", externalPort=" + externalPort + ", protocol=" + protocol + ", hwversion=" + hwversion + "]";
	}
}
