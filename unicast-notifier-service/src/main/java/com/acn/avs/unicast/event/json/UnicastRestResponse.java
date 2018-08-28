package com.acn.avs.unicast.event.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 
 * @author Anand.Jha
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "resultCode",
    "resultDescription",
    "systemTime"
 })
public class UnicastRestResponse {
	
	@JsonProperty("resultCode")
	String resultCode;
	
	@JsonProperty("resultDescription")
	String resultDescription;
	
	@JsonProperty("systemTime")
	long systemTime;
	
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDescription() {
		return resultDescription;
	}

	public void setResultDescription(String resultDescription) {
		this.resultDescription = resultDescription;
	}

	public long getSystemTime() {
		return systemTime;
	}

	public void setSystemTime(long systemTime) {
		this.systemTime = systemTime;
	}

	
}
