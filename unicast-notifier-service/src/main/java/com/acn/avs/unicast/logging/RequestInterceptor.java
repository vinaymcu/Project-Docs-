package com.acn.avs.unicast.logging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * The Interceptor class for getting the common request header / request param
 * for logging.
 */
@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(RequestInterceptor.class);

	/** The Logging Holder. */
	@Autowired
	private LoggingParameterHolder holder;

	/** The Logging Helper. */
	@Autowired
	private LoggingParameterHelper helper;
	
	/**
	 * Interceptor preHandle 
	 * 
	 * @param request
	 * @param response
	 * @param handler
	 * 
	 * @return boolean
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		LoggingParameter loggingParameter = helper.initilizeLoggingParameters(request);
		holder.put(loggingParameter);

		return super.preHandle(request, response, handler);
	}

	/**
	 * Interceptor postHandle 
	 * 
	 * @param request
	 * @param response
	 * @param handler
	 * @param modelAndView
	 * 
	 * @return 
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		long timestamp = System.currentTimeMillis();
		LoggingParameter loggingParameter = holder.get();
		LOGGER.info("EXECUTION TIME: {} (ms) ", timestamp - loggingParameter.getTimestamp());
		super.postHandle(request, response, handler, modelAndView);
	}

}
