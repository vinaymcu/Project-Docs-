package com.acn.avs.unicast.event.wrapper;

import org.springframework.stereotype.Component;
import com.acn.avs.unicast.event.json.UnicastRestResponse;

/**
 * 
 * @author Anand.Jha
 *
 */
@Component
public class UnicastRestResponseWrapper {

	private static final String SUCCESS_RESPONSE_CODE = "ACN_200";

	private static final String GENERIC_ERROR_CODE = "ACN_300";

	private static final String GENERIC_ERROR_DESC = "300-GENERIC ERROR";

	/**
	 * prepareSuccessResponse 
	 * 
	 * @return
	 */
	public UnicastRestResponse prepareSuccessResponse(){
		UnicastRestResponse unicastRestResponse = new UnicastRestResponse();
		unicastRestResponse.setResultCode(SUCCESS_RESPONSE_CODE);
		unicastRestResponse.setSystemTime(System.currentTimeMillis());
		return unicastRestResponse;
	}

	/**
	 * prepare Failed Response
	 * 
	 * @return UnicastRestResponse
	 */
	public UnicastRestResponse prepareFailedResponse(){
		UnicastRestResponse unicastRestResponse = new UnicastRestResponse();

		unicastRestResponse.setResultCode(GENERIC_ERROR_CODE);
		unicastRestResponse.setResultDescription(GENERIC_ERROR_DESC);
		unicastRestResponse.setSystemTime(System.currentTimeMillis());
		return unicastRestResponse;
	}

	/**
	 * prepare Validation Failed Response
	 * 
	 * @param errorCode
	 * @param errorMessage
	 * @return UnicastRestResponse
	 */
	public UnicastRestResponse prepareValidationFailedResponse(String errorCode, String errorMessage) {

		UnicastRestResponse unicastRestResponse = new UnicastRestResponse();
		unicastRestResponse.setResultCode(errorCode);
		unicastRestResponse.setResultDescription(errorMessage);
		unicastRestResponse.setSystemTime(System.currentTimeMillis());
		return unicastRestResponse;
	}
}
