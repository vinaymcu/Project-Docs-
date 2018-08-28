package com.acn.avs.unicast.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acn.avs.unicast.entity.SetTopBoxes;
import com.acn.avs.unicast.entity.TriggerMapping;
import com.acn.avs.unicast.event.json.EventUpdate;
import com.acn.avs.unicast.event.json.EventUpdateRequest;
import com.acn.avs.unicast.exception.UnicastRequestValidationErrorReason;
import com.acn.avs.unicast.exception.UnicastRequestValidationException;
import com.acn.avs.unicast.service.StbNotifierService;

/**
 * 
 * @author Anand.Jha
 *
 *         This class will validate the input request
 */
@Component
public class UnicastRequestValidator {
	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UnicastRequestValidator.class);

	@Autowired
	StbNotifierService stbNotifierService;

	private static final String ERRORCODE = "ACN_3020";

	private Date epochStartDate;
	private Date epochEndDate;

	UnicastRequestValidator() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		try {
			epochStartDate = sdf.parse("1970-01-01");
			epochEndDate = sdf.parse("2038-01-19");
		} catch (ParseException e) {
			LOGGER.error(" ParseException : " + e);
		}
	}

	/**
	 * validate json request
	 * 
	 * @param eventUpdate
	 * 
	 * @return boolean
	 * 
	 * @throws UnicastRequestValidationException
	 */
	public boolean validate(EventUpdate eventUpdate) throws UnicastRequestValidationException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("(+)validate");
		}
		String subscriberId = eventUpdate.getSubscriberId();
		String triggerType = eventUpdate.getTriggerType();
		String timeStamp = eventUpdate.getTimestamp();
		List<String> macAddresses = eventUpdate.getMACAddress();

		validateMandatoryParameter(subscriberId, triggerType, timeStamp);

		timeStampNumericValidation(timeStamp);
		timeStampValidation(timeStamp);

		boolean isTriggerTypeMessageInfo = stbNotifierService.isMessageInfoTrigger(triggerType);

		String triggerInfo = eventUpdate.getTriggerInfo();

		triggerInfoValidation(isTriggerTypeMessageInfo, triggerInfo);

		TriggerMapping triggerMapping = stbNotifierService.getTriggerMapping(triggerType);

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("TriggerMapping : " + triggerMapping);
		}

		triggerTypeValidation(triggerMapping);

		List<SetTopBoxes> setTopBoxes = stbNotifierService.getSetTopBoxes(subscriberId);

		setTopBoxesValidation(setTopBoxes);

		extractMacAddressAndValidate(macAddresses, isTriggerTypeMessageInfo, setTopBoxes);

		validateService(setTopBoxes, triggerMapping.getServiceName());

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("(-)validate");
		}
		return true;
	}

	/**
	 * 
	 * @param eventUpdateRequest
	 * @return
	 * @throws UnicastRequestValidationException
	 */
	public boolean validate(EventUpdateRequest eventUpdateRequest) throws UnicastRequestValidationException {
		eventUpdateRequest.setValidated(true);
		return this.validate(eventUpdateRequest.getEventUpdate());
	}

	/**
	 * timeStampNumericValidation
	 * 
	 * @param timeStamp
	 * 
	 * @return
	 */
	private void timeStampNumericValidation(String timeStamp) {

		if (!isNumeric(timeStamp)) {
			throw new UnicastRequestValidationException(ERRORCODE,
					new Object[] { UnicastRequestValidationErrorReason.TIMESTAMP_NOT_NUMERIC.getReason() });
		}
	}

	/**
	 * triggerInfoValidation
	 * 
	 * @param isTriggerTypeMessageInfo
	 * @param triggerInfo
	 */
	private void triggerInfoValidation(boolean isTriggerTypeMessageInfo, String triggerInfo) {

		if (isTriggerTypeMessageInfo && StringUtils.isEmpty(triggerInfo)) {
			throw new UnicastRequestValidationException(ERRORCODE, new Object[] {
					UnicastRequestValidationErrorReason.TRIGGER_INFO_NULL_FOR_MESSAGE_INFO.getReason() });
		}
	}

	/**
	 * triggerTypeValidation
	 * 
	 * @param triggerMapping
	 * 
	 * @return
	 */
	private void triggerTypeValidation(TriggerMapping triggerMapping) {

		if (triggerMapping == null) {
			throw new UnicastRequestValidationException(ERRORCODE,
					new Object[] { UnicastRequestValidationErrorReason.INVALID_TRIGGER_TYPE.getReason() });
		}
	}

	/**
	 * validateMandatoryParameter
	 * 
	 * @param subscriberId
	 * @param triggerType
	 * @param timeStamp
	 * 
	 *            return boolean
	 */
	private boolean validateMandatoryParameter(String subscriberId, String triggerType, String timeStamp) {

		if (StringUtils.isEmpty(subscriberId)) {
			throw new UnicastRequestValidationException(ERRORCODE,
					new Object[] { UnicastRequestValidationErrorReason.SUBSCRIBERID_NULL_EMPTY.getReason() });
		}

		if (StringUtils.isEmpty(triggerType)) {
			throw new UnicastRequestValidationException(ERRORCODE,
					new Object[] { UnicastRequestValidationErrorReason.TRIGGERTYPE_NULL_EMPTY.getReason() });
		}

		if (StringUtils.isEmpty(timeStamp)) {
			throw new UnicastRequestValidationException(ERRORCODE,
					new Object[] { UnicastRequestValidationErrorReason.TIMESTAMP_NULL_EMPTY.getReason() });
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Mandatory parameter validataion is true!!");
		}
		return true;
	}

	/**
	 * extractMacAddressAndValidate
	 * 
	 * Validate all macAddress in case of message info trigger and validate only
	 * 1st macAdress for other trigger
	 * 
	 * @param macAddresses
	 * @param isTriggerTypeMessageInfo
	 * @param setTopBoxes
	 */
	private void extractMacAddressAndValidate(List<String> macAddresses, boolean isTriggerTypeMessageInfo,
			List<SetTopBoxes> setTopBoxes) {

		boolean result = false;

		if (CollectionUtils.isNotEmpty(macAddresses)) {
			if (isTriggerTypeMessageInfo) {
				result = validateMacAddress(setTopBoxes, macAddresses);
			} else {
				result = validateMacAddress(setTopBoxes, macAddresses.get(0));
			}

			if (!result) {
				throw new UnicastRequestValidationException(ERRORCODE,
						new Object[] { UnicastRequestValidationErrorReason.MAC_NOT_VALIDATED.getReason() });
			}
		}
	}

	/**
	 * validate requested macAddresses with associated setTopBoxes
	 * 
	 * @param setTopBoxes
	 * @param reqMacAddresses
	 * 
	 * @return boolean
	 */
	private boolean validateMacAddress(List<SetTopBoxes> setTopBoxes, List<String> reqMacAddresses) {
		boolean validateMac = true;
		for (String reqMacAdress : reqMacAddresses) {
			for (SetTopBoxes stb : setTopBoxes) {

				if (reqMacAdress.equalsIgnoreCase(stb.getMacAddress())) {
					validateMac = true;
					break;
				} else {
					validateMac = false;
				}
			}
			if (!validateMac) {
				return validateMac;
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("validateMacAddress  : " + validateMac);
		}
		return validateMac;
	}

	/**
	 * validate requested macAddress with associated setTopBoxes
	 * 
	 * @param setTopBoxes
	 * @param reqMacAddressList
	 * 
	 * @return boolean
	 */
	private boolean validateMacAddress(List<SetTopBoxes> setTopBoxes, String reqMacAddress) {
		boolean validateMac = false;

		for (SetTopBoxes stb : setTopBoxes) {

			if (reqMacAddress.equalsIgnoreCase(stb.getMacAddress())) {
				validateMac = true;
				break;
			}
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("validateMacAddress  : " + validateMac);
		}
		return validateMac;
	}

	/**
	 * validateService
	 * 
	 * @param serviceName
	 * @param setTopBoxes
	 */
	private void validateService(List<SetTopBoxes> setTopBoxes, String serviceName) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("(+)validateService, setTopBoxes : " + setTopBoxes + ", serviceName= " + serviceName);
		}
		boolean result = true;
		for (SetTopBoxes stb : setTopBoxes) {
			String hwVersion = stb.getHwversion();

			if (!stbNotifierService.isImplicitNATMode(stb.getConnectionMode())) {
				result = stbNotifierService.isHwVerionAssciateWithServiceName(hwVersion, serviceName);
			}
		}

		if (!result) {
			throw new UnicastRequestValidationException(ERRORCODE,
					new Object[] { UnicastRequestValidationErrorReason.INVALID_SERVICE.getReason() });
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("(-)validateService");
		}
	}

	/**
	 * @param setTopBoxes
	 */
	private void setTopBoxesValidation(List<SetTopBoxes> setTopBoxes) {
		if (CollectionUtils.isEmpty(setTopBoxes)) {
			throw new UnicastRequestValidationException(ERRORCODE,
					new Object[] { UnicastRequestValidationErrorReason.NO_STB_FOR_SUBSCRIBER.getReason() });
		}
	}

	/**
	 * check if the string contains valid numeric value This method is 20 times
	 * faster than using Integer.parseInt this method is 10 times faster than
	 * using regex str.matches("^-?\\d+$")
	 * 
	 * @param value
	 * @param isCommaSeparated
	 * @return boolean
	 */
	public static boolean isNumeric(String value) {
		boolean status = true;
		if (value == null) {
			return false;
		}

		int length = value.length();
		int count = 0;
		for (int i = 0; i < length; i++) {
			char c = value.charAt(i);
			if (c < '0' || c > '9') {
				status = false;
				break;
			} else {
				count++;
			}
		}
		if (count == 0) {
			status = false;
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("value : " + value + ", isNumeric = " + status);
		}
		return status;
	}

	/**
	 * timeStampValidation
	 * 
	 * @param timeStamp
	 * 
	 * @return
	 */
	private void timeStampValidation(String timeStamp) {
		long timeToCompare = 0l;
		if (timeStamp.length() == 10) {
			timeToCompare = Long.valueOf(timeStamp) * 1000;
		} else if (timeStamp.length() == 13) {
			timeToCompare = Long.valueOf(timeStamp);
		} else {
			throw new UnicastRequestValidationException(ERRORCODE,
					new Object[] { UnicastRequestValidationErrorReason.INVALID_TIMESTAMP.getReason() });
		}
		Date dateToComare = new Date(timeToCompare);
		if (dateToComare.before(epochStartDate) || dateToComare.after(epochEndDate)) {
			throw new UnicastRequestValidationException(ERRORCODE,
					new Object[] { UnicastRequestValidationErrorReason.INVALID_TIMESTAMP.getReason() });

		}
	}

}
