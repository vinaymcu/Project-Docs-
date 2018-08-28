package com.acn.avs.unicast.web.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.acn.avs.unicast.event.json.UnicastRestResponse;
import com.acn.avs.unicast.event.wrapper.UnicastRestResponseWrapper;
import com.acn.avs.unicast.exception.UnicastRequestValidationException;


/**
 * 
 * @author Anand.Jha
 * 
 * This class is responsible to catch the exception 
 *
 */
@ControllerAdvice
public class UnicastExceptionCotroller {
	private static final Logger LOGGER = LoggerFactory.getLogger(UnicastExceptionCotroller.class);
	
	 @Autowired
	 private UnicastRestResponseWrapper unicastRestResponseWrapper;
	
	 @Autowired
	 MessageSourceAccessor messageSourceAccessor;
	 
	/**
     * handle HttpRequestMethodNotSupportedException
     * 
     * @param hrme
     */
    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Invalid request method found.")
    public void handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException hrme) {
        LOGGER.error("HttpRequestMethodNotSupportedException found. : ", hrme);
    }
    
    /**
     * handle HttpMessageNotReadableException
     * 
     * @param hmne
     */
    @ExceptionHandler({ HttpMessageNotReadableException.class })
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The request entity has a invalid value.")
    public void handleHttpMessageNotReadableException(HttpMessageNotReadableException hmne) {
        LOGGER.error("HttpMessageNotReadableException found. : ", hmne);
    }
    
   
    /**
     * Handle Exception
     * 
     * @param ex
     * @return UnicastRestResponse
     */
    @ExceptionHandler({ Exception.class })
    @ResponseBody
    public UnicastRestResponse handleException(Exception ex) {
        return unicastRestResponseWrapper.prepareFailedResponse();
    }
    
    /**
     * Handle UnicastRequestValidationException
     * 
     * @param requestValidationException
     * @return UnicastRestResponse
     */
    @ExceptionHandler({ UnicastRequestValidationException.class })
    @ResponseBody
    public UnicastRestResponse handleUnicastRequestValidationException(UnicastRequestValidationException requestValidationException) {
    	String errorCode = requestValidationException.getErrorCode();
    	String errorMsg = messageSourceAccessor.getMessage(errorCode,
    			requestValidationException.getMsgParamsArray());

    	return unicastRestResponseWrapper.prepareValidationFailedResponse(errorCode, errorMsg);
    }
}
