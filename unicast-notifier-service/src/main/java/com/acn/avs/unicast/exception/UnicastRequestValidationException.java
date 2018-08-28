package com.acn.avs.unicast.exception;

import java.util.Arrays;

/**
 * 
 * @author Anand.Jha
 *
 * This runtime exception may be thrown for unicast invalid request
 */
public class UnicastRequestValidationException extends RuntimeException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7063338577503508527L;

	/**
	 * errorCode
	 */
	private final String errorCode;

	/**
	 * reasonParamsArray
	 */
	private final Object[] reasonParamsArray;

	/**
	 * UnicastRequestValidationException
	 * @param errorCode
	 * @param reasonParams
	 */
	public UnicastRequestValidationException(String errorCode, Object[] reasonParams) {
		super();
		this.errorCode = errorCode;
		this.reasonParamsArray = Arrays.copyOf(reasonParams, reasonParams.length);
	}

	/**
	 * get ErrorCode
	 * @return String
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * getMsgParamsArray
	 * @return Object[]
	 */
	public Object[] getMsgParamsArray(){
		if(reasonParamsArray != null){
			return Arrays.copyOf(reasonParamsArray, reasonParamsArray.length);
		}
		return new Object[0];
	}

}
