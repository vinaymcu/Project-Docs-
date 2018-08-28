package com.acn.avs.unicast.logging;

import org.springframework.stereotype.Component;

/**
 * The Class Holder class for holding request header/ request param.
 *
 * @author Sumit.Sharma
 * @version 1.0
 * @since 1.0
 */

@Component
public class LoggingParameterHolder {

	/** The Constant THREAD_LOCAL_HOLDER. */
	private final ThreadLocal<LoggingParameter> THREAD_LOCAL_HOLDER = new InheritableThreadLocal<>();

	/**
	 * Put Logging Parameter
	 *
	 * @param logingParameter
	 *        the loging parameter
	 */
	public void put(LoggingParameter loggingParameter) {
		THREAD_LOCAL_HOLDER.set(loggingParameter);
	}

	/**
	 * Gets Logging Parameter
	 *
	 * @return the loging parameter
	 */
	public LoggingParameter get() {
		return THREAD_LOCAL_HOLDER.get();
	}

}
