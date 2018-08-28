package com.acn.avs.unicast.logging;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The Helper claas for initializing Logging Parameters.
 *
 * @author Sumit.Sharma
 * @since 1.0
 * @version 1.0
 *
 *
 *
 */
@Component
public class LoggingParameterHelper {

	/** The device type. */
	@Value("${info.device-Type:#{null}}")
	private String deviceType;

	/** The channel. */
	@Value("${info.channel:#{null}}")
	private String channel;

	/** The tenant id. */
	@Value("${info.tenant-id:#{null}}")
	private String tenantId;

	/** The provider name. */
	@Value("${info.provider-name:#{null}}")
	private String providerName;

	/** The service name. */
	@Value("${info.service-name:#{null} }")
	private String serviceName;

	/** The ms id. */
	@Value("${info.service-id:#{null}}")
	private String msId;

	/**
	 * Initilize logging parameters.
	 *
	 * @return the logging parameter
	 */
	public LoggingParameter initilizeLoggingParameters() {
		LoggingParameter loggingParameter = new LoggingParameter();
		loggingParameter.setChannel(channel);
		loggingParameter.setDeviceType(deviceType);
		loggingParameter.setMsId(msId);
		loggingParameter.setProviderName(providerName);
		loggingParameter.setServiceName(serviceName);
		loggingParameter.setTenantId(tenantId);
		loggingParameter.setTn(UUID.randomUUID().toString());
		loggingParameter.setSid(UUID.randomUUID().toString());

		return loggingParameter;
	}

	/**
	 * Initilize logging parameters.
	 *
	 * @param request
	 *            the request
	 * @return the logging parameter
	 */
	public LoggingParameter initilizeLoggingParameters(HttpServletRequest request) {
		LoggingParameter loggingParameter = new LoggingParameter();
		loggingParameter.setChannel(request.getHeader("channel"));
		loggingParameter.setDeviceType(request.getHeader("deviceType"));
		loggingParameter.setMsId(request.getHeader("msId"));
		loggingParameter.setProviderName(request.getHeader("providerName"));
		loggingParameter.setServiceName(request.getHeader("serviceName"));
		loggingParameter.setTenantId(request.getHeader("tenantId"));
		loggingParameter.setTn(request.getHeader("tn"));
		loggingParameter.setSid(request.getHeader("sid"));
		loggingParameter.setApi(request.getRequestURI());
		addLoggingParameterstoMDC(loggingParameter);
		return loggingParameter;
	}

	/**
	 * Adds the logging parameters to MDC.
	 *
	 * @param loggingParameter
	 *            the logging parameter
	 */
	private void addLoggingParameterstoMDC(LoggingParameter loggingParameter) {
		if (StringUtils.isNotEmpty(loggingParameter.getChannel())) {
			MDC.put("channel", loggingParameter.getChannel());
		}
		if (StringUtils.isNotEmpty(loggingParameter.getDeviceType())) {
			MDC.put("deviceType", loggingParameter.getDeviceType());
		}
		if (StringUtils.isNotEmpty(loggingParameter.getMsId())) {
			MDC.put("msId", loggingParameter.getMsId());
		}
		if (StringUtils.isNotEmpty(loggingParameter.getProviderName())) {
			MDC.put("providerName", loggingParameter.getProviderName());
		}
		if (StringUtils.isNotEmpty(loggingParameter.getServiceName())) {
			MDC.put("serviceName", loggingParameter.getServiceName());
		}
		if (StringUtils.isNotEmpty(loggingParameter.getTenantId())) {
			MDC.put("tenantId", loggingParameter.getTenantId());
		}
		if (StringUtils.isNotEmpty(loggingParameter.getTn())) {
			MDC.put("tn", loggingParameter.getTn());
		}
		if (StringUtils.isNotEmpty(loggingParameter.getApi())) {
			MDC.put("api", loggingParameter.getApi());
		}
		if (StringUtils.isNotEmpty(loggingParameter.getSid())) {
			MDC.put("sid", loggingParameter.getSid());
		}
	}
}
