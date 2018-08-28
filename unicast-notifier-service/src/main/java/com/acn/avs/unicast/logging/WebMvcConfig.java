package com.acn.avs.unicast.logging;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * The Configuration class for auto configure logging interceptor to Spring's
 * ApplicationContext.
 *
 * @version 1.0
 * @since 1.0
 *
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(WebMvcConfig.class);

	/** The request interceptor. */
	@Autowired
	private RequestInterceptor requestInterceptor;

	/**
	 * Add Interceptors 
	 * 
	 * @param registry
	 * @return 
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LOGGER.info("Adding  RequestParameterHolderInterceptor ");
		registry.addInterceptor(requestInterceptor);
	}

	/**
	 * Inits the.
	 */
//	@PostConstruct
//	public void init() {
//		LOGGER.info("Initializing WebMvcConfig .............. ");
//	}

}