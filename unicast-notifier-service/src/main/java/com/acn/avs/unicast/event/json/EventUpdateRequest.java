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

import com.acn.avs.unicast.event.json.EventUpdate;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Anand.Jha
 * 
 *         The Class EventUpdateWrapper.
 */
public class EventUpdateRequest implements Serializable {


	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5703470264120157285L;
	
	/** The event update. */
	@JsonProperty("EventUpdate")
	private EventUpdate eventUpdate;
	
	private boolean validated;

	/**
	 * @return the validated
	 */
	public boolean isValidated() {
		return validated;
	}

	/**
	 * @param validated the validated to set
	 */
	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	public EventUpdate getEventUpdate() {
		return eventUpdate;
	}

	public void setEventUpdate(EventUpdate eventUpdate) {
		this.eventUpdate = eventUpdate;
	}
	
	

}
