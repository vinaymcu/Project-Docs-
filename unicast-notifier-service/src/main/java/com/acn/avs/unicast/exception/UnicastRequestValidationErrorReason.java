package com.acn.avs.unicast.exception;

/**
 * 
 *This enum class having all the reason for invalid request 
 */
public enum UnicastRequestValidationErrorReason {
	SUBSCRIBERID_NULL_EMPTY("Subscriber Id is Null or Empty"),
	TRIGGERTYPE_NULL_EMPTY("Trigger Type is Null or Empty"),
	TIMESTAMP_NULL_EMPTY("TimeStamp is Null or Empty"),
	SUBSCRIBER_ID_NOT_NUMERIC("Subscriber Id is not Numeric"),
	TIMESTAMP_NOT_NUMERIC("TimeStamp is not Numeric"),
	TRIGGER_INFO_NULL_FOR_MESSAGE_INFO("TriggerInfo is not defined"),
	INVALID_TRIGGER_TYPE("Invalid TriggerType"),
	MAC_NOT_VALIDATED("Invalid MacAddress"),
	NO_STB_FOR_SUBSCRIBER("No STB assigned to this Subscriber"),
	INVALID_SERVICE("Subscriber could not associate with this trigger service"),
	INVALID_TIMESTAMP("Invalid TimeStamp");

	private String reason;

	private UnicastRequestValidationErrorReason(String reason) {
		this.reason =  reason;
	}

	public String getReason(){
		return this.reason;
	}
}

