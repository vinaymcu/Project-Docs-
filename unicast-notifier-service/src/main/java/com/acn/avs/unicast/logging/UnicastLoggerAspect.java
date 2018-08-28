package com.acn.avs.unicast.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.acn.avs.unicast.event.json.EventUpdateRequest;
import com.acn.avs.unicast.exception.UnicastRequestValidationException;

/**
 * 
 * @author Anand.Jha
 * 
 * UnicastLoggerAspect is responsible to do all the logging
 *
 */
@Aspect
@Component
public class UnicastLoggerAspect {
	
	/**
	 * SUCCESS_RESULT
	 */
    private static final String SUCCESS_RESULT = "Success";
    
    /**
     * logAdvice
     * @param joinPoint
     * @return Object
     * @throws Throwable
     */
	@Around("execution(* com.acn.avs.unicast.web.rest.controller.UnicastRestController+.*(..))")
	public Object logAdvice(ProceedingJoinPoint  joinPoint) throws Throwable {

		Logger LOGGER = LoggerFactory.getLogger(joinPoint.getTarget().getClass());

		long  startTime = System.currentTimeMillis();

		String method = joinPoint.getSignature().getName();

		StringBuilder requestParams = new StringBuilder();

		final Object[] args = joinPoint.getArgs();
		for (int i = 0; i< args.length; i++){
			EventUpdateRequest request = (EventUpdateRequest) args[i];
			requestParams.append(request.getEventUpdate());
		}
		
		Object obj = null;
		try {
			obj = joinPoint.proceed();
		} catch(UnicastRequestValidationException validationException){
			LOGGER.error(getLoggerValidationErrorMessage(method, requestParams.toString(),
					(UnicastRequestValidationException)validationException), validationException);
			throw validationException;
		}
		catch (Exception exception) {
			LOGGER.error(getLoggerErrorMessage(method, requestParams.toString(), exception),
					exception);
			throw exception;
		}
		LOGGER.info(getLoggerMessage(method, requestParams.toString(), SUCCESS_RESULT, startTime ));
		return obj;
	}
	
	/**
	 * getLoggerMessage
	 * @param method
	 * @param requestParams
	 * @param result
	 * @param time
	 * @return
	 */
	private String getLoggerMessage(String method, String requestParams,
			String result, long time) {
		StringBuilder sb = new StringBuilder();
		sb.append("[").
		append(method).
		append("]-[").
		append(requestParams).
		append("]-[Result = ").append(result).
		append("]-[Execution Time = ").
		append(System.currentTimeMillis() - time).append(" ms]");
		return sb.toString();
	}
  
	/**
	 * getLoggerErrorMessage
	 * @param method
	 * @param requestParams
	 * @param e
	 * @return
	 */
	private String getLoggerErrorMessage(String method, String requestParams , Exception e){
		StringBuilder sb = new StringBuilder();
		sb.append("[").
		append(method).
		append("]-[").
		append(requestParams).
		append("]-[Exception = ").
		append(e.getClass().getName());
		return sb.toString();
	}
	
	/**
	 * getLoggerValidationErrorMessage
	 * @param method
	 * @param requestParams
	 * @param validationException
	 * @return String
	 */
	private String getLoggerValidationErrorMessage(String method, String requestParams , 
			UnicastRequestValidationException validationException) {
		StringBuilder sb = new StringBuilder();
		sb.append("[").
		append(method).
		append("]-[").
		append(requestParams).
		append("]-[UnicastRequestValidationException = ").
		append(validationException.getClass().getName()).
		append(" ;errorCode = ").
		append(validationException.getErrorCode()).
		append(" ;errorMessage = ").
		append(validationException.getMsgParamsArray()[0]).
		append("]");
		return sb.toString();
	}
}
