package com.acn.avs.unicast.util;

import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testng.Assert;

import com.acn.avs.unicast.entity.SetTopBoxes;
import com.acn.avs.unicast.entity.TriggerMapping;
import com.acn.avs.unicast.event.json.EventUpdate;
import com.acn.avs.unicast.event.json.EventUpdateRequest;
import com.acn.avs.unicast.exception.UnicastRequestValidationException;
import com.acn.avs.unicast.repository.StbManagerRepository;
import com.acn.avs.unicast.repository.UnicastTriggerMappingRepository;
import com.acn.avs.unicast.service.StbNotifierService;

/**
 * @author happy.dhingra
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class UnicastRequestValidatorTest{

	@InjectMocks
	private UnicastRequestValidator unicastRequestValidator;

	@Mock
	StbManagerRepository stbManagerRepository;

	@Mock
	StbNotifierService stbNotifierService;

	@Mock
	UnicastTriggerMappingRepository unicastTriggerMappingRepository;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

	}

	/**
	 * test validate with empty object
	 */
	@Test(expected = UnicastRequestValidationException.class)
	public void testValidateRequest() {
		EventUpdateRequest eventUpdateRequest = new EventUpdateRequest();
		EventUpdate eventUpdate = new EventUpdate();
		eventUpdateRequest.setEventUpdate(eventUpdate);
		unicastRequestValidator.validate(eventUpdateRequest);
	}

	/**
	 * test validate with empty object
	 */
	@Test(expected = UnicastRequestValidationException.class)
	public void testValidate() {
		EventUpdate eventUpdate = new EventUpdate();
		unicastRequestValidator.validate(eventUpdate);
	}
	
	/**
	 * test validate for empty variables
	 */
	@Test(expected = UnicastRequestValidationException.class)
	public void testValidate2() {
		EventUpdate eventUpdate = new EventUpdate();
		eventUpdate.setSubscriberId("");
		eventUpdate.setTriggerType("");
		eventUpdate.setTimestamp("");
		unicastRequestValidator.validate(eventUpdate);
	}

	
	
	/**
	 * test validate with empty vars
	 */
	@Test(expected = UnicastRequestValidationException.class)
	public void testValidate3() {
		EventUpdate eventUpdate = new EventUpdate();
		eventUpdate.setSubscriberId("1");
		eventUpdate.setTriggerType("");
		eventUpdate.setTimestamp("");
		unicastRequestValidator.validate(eventUpdate);
	}

	/**
	 * test validate with empty vars
	 */
	@Test(expected = UnicastRequestValidationException.class)
	public void testValidate4() {
		EventUpdate eventUpdate = new EventUpdate();
		eventUpdate.setSubscriberId("asdasda");
		eventUpdate.setTriggerType("zasdvh");
		eventUpdate.setTimestamp("");
		unicastRequestValidator.validate(eventUpdate);
	}

	/**
	 * test validate with empty vars
	 */
	@Test(expected = UnicastRequestValidationException.class)
	public void testValidate5() {
		EventUpdate eventUpdate = new EventUpdate();
		eventUpdate.setSubscriberId("1.1");
		eventUpdate.setTriggerType("");
		eventUpdate.setTimestamp("zsdcf");
		unicastRequestValidator.validate(eventUpdate);
	}

	/**
	 * test validate when unicastTriggerMappingRepository.findByTriggerType
	 * returns null
	 */
	public void testValidate6() {

		EventUpdate eventUpdate = new EventUpdate();
		eventUpdate.setSubscriberId("1015");
		eventUpdate.setTriggerType("asdsd");
		eventUpdate.setTimestamp("1412121212");
		unicastRequestValidator.validate(eventUpdate);
	}
	
	

	/**
	 * test validate when stbManagerRepository.getSetTopBoxes returns null
	 */
	@Test(expected = UnicastRequestValidationException.class)
	public void testValidate7() {
		setDefaults();
		EventUpdate eventUpdate = new EventUpdate();
		eventUpdate.setSubscriberId("7");
		eventUpdate.setTriggerType("T_MESS_INFO");
		eventUpdate.setTimestamp("1412121212");
		eventUpdate.setTriggerInfo("T_MESS_INFO");
		TriggerMapping triggerMapping = new TriggerMapping();
		triggerMapping.setTriggerType("T_MESS_INFO");
		when(unicastTriggerMappingRepository.findByTriggerType("T_MESS_INFO")).thenReturn(triggerMapping);
		when(stbNotifierService.getTriggerMapping(eventUpdate.getTriggerType())).thenReturn(triggerMapping);
		when(stbNotifierService.getSetTopBoxes(eventUpdate.getSubscriberId())).thenReturn(null);
		unicastRequestValidator.validate(eventUpdate);
	}

	/**
	 * test validate when stbManagerRepository.getSetTopBoxes returns null
	 */
	@Test(expected = UnicastRequestValidationException.class)
	public void testValidate7False() {
		setDefaults();
		EventUpdate eventUpdate = new EventUpdate();
		eventUpdate.setSubscriberId("7");
		eventUpdate.setTriggerType("T_MESS_INFO");
		eventUpdate.setTimestamp("1412121212");
		eventUpdate.setTriggerInfo("T_MESS_INFO");
		TriggerMapping triggerMapping = new TriggerMapping();
		triggerMapping.setTriggerType("T_MESS_INFO");
		triggerMapping.setServiceName("T_MESS_INFO");
		when(unicastTriggerMappingRepository.findByTriggerType("T_MESS_INFO")).thenReturn(triggerMapping);
		when(stbNotifierService.getTriggerMapping(eventUpdate.getTriggerType())).thenReturn(triggerMapping);
		when(stbNotifierService.getSetTopBoxes(eventUpdate.getSubscriberId())).thenReturn(null);
		unicastRequestValidator.validate(eventUpdate);
	}

	/**
	 * test validate with setTopBoxes.setHwversion(null);
	 */
	@Test(expected = UnicastRequestValidationException.class)
	public void testValidate8() {
		setDefaults();
		EventUpdate eventUpdate = new EventUpdate();
		eventUpdate.setSubscriberId("1015");
		eventUpdate.setTriggerType("T_MESS_INFO");
		eventUpdate.setTimestamp("1412121212");
		eventUpdate.setTriggerInfo("T_MESS_INFO");
		TriggerMapping triggerMapping = new TriggerMapping();
		triggerMapping.setTriggerType("T_MESS_INFO");
		triggerMapping.setServiceName("T_MESS_INFO");
		when(stbNotifierService.getTriggerMapping(eventUpdate.getTriggerType())).thenReturn(triggerMapping);
		when(unicastTriggerMappingRepository.findByTriggerType("T_MESS_INFO")).thenReturn(triggerMapping);
		List<SetTopBoxes> setTopBoxesList = new ArrayList<>();
		SetTopBoxes setTopBoxes = new SetTopBoxes();
		setTopBoxes.setConnectionMode(3);
		setTopBoxesList.add(setTopBoxes);
		when(stbNotifierService.getSetTopBoxes(eventUpdate.getSubscriberId()))
				.thenReturn(setTopBoxesList);
		unicastRequestValidator.validate(eventUpdate);
	}

	/**
	 * test validate with valid data
	 */
	@Test
	public void testValidate8WithFalseTrigger() {
		setDefaults();
		EventUpdate eventUpdate = new EventUpdate();
		eventUpdate.setSubscriberId("1015");
		eventUpdate.setTriggerType("T_MESS_INFO");
		eventUpdate.setTimestamp("1483611866");
		eventUpdate.setTriggerInfo("T_MESS_INFO");
		TriggerMapping triggerMapping = new TriggerMapping();
		triggerMapping.setTriggerType("T_MESS_INFO");
		triggerMapping.setServiceName("T_MESS_INFO");
		when(stbNotifierService.isMessageInfoTrigger(eventUpdate.getTriggerType())).thenReturn(true);
		when(stbNotifierService.getTriggerMapping(eventUpdate.getTriggerType())).thenReturn(triggerMapping);
		when(stbNotifierService.isHwVerionAssciateWithServiceName("DELL", "T_MESS_INFO")).thenReturn(true);
		when(unicastTriggerMappingRepository.findByTriggerType("T_MESS_INFO")).thenReturn(triggerMapping);
		List<SetTopBoxes> setTopBoxesList = new ArrayList<>();
		SetTopBoxes setTopBoxes = new SetTopBoxes();
		setTopBoxes.setHwversion("DELL");
		setTopBoxes.setConnectionMode(3);
		setTopBoxesList.add(setTopBoxes);
		when(stbNotifierService.getSetTopBoxes(eventUpdate.getSubscriberId()))
				.thenReturn(setTopBoxesList);
		unicastRequestValidator.validate(eventUpdate);
	}
	/**
	 * test validate with message_info as S_MESSAGE_INFO
	 */
	@Test(expected = UnicastRequestValidationException.class)
	public void testValidate9() {
		setDefaults();

		EventUpdate eventUpdate = new EventUpdate();
		eventUpdate.setSubscriberId("1015");
		eventUpdate.setTriggerType("S_MESSAGE_INFO");
		eventUpdate.setTimestamp("1483611866");
		eventUpdate.setTriggerInfo("T_MESS_INFO");
		TriggerMapping triggerMapping = new TriggerMapping();
		triggerMapping.setTriggerType("T_MESS_INFO");
		List<SetTopBoxes> setTopBoxesList = new ArrayList<>();
		SetTopBoxes setTopBoxes = new SetTopBoxes();
		setTopBoxes.setHwversion("DELL");
		setTopBoxes.setConnectionMode(3);
		setTopBoxesList.add(setTopBoxes);
		when(stbNotifierService.getSetTopBoxes(eventUpdate.getSubscriberId()))
				.thenReturn(setTopBoxesList);
		when(stbNotifierService.getTriggerMapping(eventUpdate.getTriggerType())).thenReturn(triggerMapping);
		when(unicastTriggerMappingRepository.findByTriggerType("T_MESS_INFO")).thenReturn(triggerMapping);
		unicastRequestValidator.validate(eventUpdate);
	}

	/**
	 * test validate with macList and macaddress is same as stb macaddress
	 */
	@Test(expected = UnicastRequestValidationException.class)
	public void testValidate10() {
		setDefaults();
		EventUpdate eventUpdate = new EventUpdate();
		List<String> macList = new ArrayList<>();
		macList.add("10.10.10.10");
		eventUpdate.setSubscriberId("1015");
		eventUpdate.setTriggerType("T_MESS_INFO");
		eventUpdate.setTimestamp("1483611866");
		eventUpdate.setMACAddress(macList);
		eventUpdate.setTriggerInfo("T_MESS_INFO");
		TriggerMapping triggerMapping = new TriggerMapping();
		triggerMapping.setTriggerType("T_MESS_INFO");
		triggerMapping.setServiceName("T_MESS_INFO");
		List<SetTopBoxes> setTopBoxesList = new ArrayList<>();
		SetTopBoxes setTopBoxes = new SetTopBoxes();
		setTopBoxes.setHwversion("DELL");
		setTopBoxes.setMacAddress("10.10.10.10");
		setTopBoxes.setConnectionMode(3);
		setTopBoxesList.add(setTopBoxes);
		when(stbNotifierService.isMessageInfoTrigger(eventUpdate.getTriggerType())).thenReturn(true);
		when(stbNotifierService.getSetTopBoxes(eventUpdate.getSubscriberId()))
				.thenReturn(setTopBoxesList);
		when(stbNotifierService.getTriggerMapping(eventUpdate.getTriggerType())).thenReturn(triggerMapping);
		when(unicastTriggerMappingRepository.findByTriggerType("T_MESS_INFO")).thenReturn(triggerMapping);
		unicastRequestValidator.validate(eventUpdate);
	}

	/**
	 * test validate with macList and macaddress is not same as stb macaddress
	 */
	@Test(expected = UnicastRequestValidationException.class)
	public void testValidate11() {
		setDefaults();
		EventUpdate eventUpdate = new EventUpdate();
		List<String> macList = new ArrayList<>();
		macList.add("10.10.10.10");
		eventUpdate.setSubscriberId("1015");
		eventUpdate.setTriggerType("T_MESS_INFO");
		eventUpdate.setTimestamp("1483611866");
		eventUpdate.setMACAddress(macList);
		eventUpdate.setTriggerInfo("T_MESS_INFO");
		TriggerMapping triggerMapping = new TriggerMapping();
		triggerMapping.setTriggerType("T_MESS_INFO");
		triggerMapping.setServiceName("T_MESS_INFO");
		when(stbNotifierService.getTriggerMapping(eventUpdate.getTriggerType())).thenReturn(triggerMapping);
		when(unicastTriggerMappingRepository.findByTriggerType("T_MESS_INFO")).thenReturn(triggerMapping);
		List<SetTopBoxes> setTopBoxesList = new ArrayList<>();
		SetTopBoxes setTopBoxes = new SetTopBoxes();
		setTopBoxes.setHwversion("DELL");
		setTopBoxes.setConnectionMode(3);
		setTopBoxes.setMacAddress("10.10.10.12");
		setTopBoxesList.add(setTopBoxes);
		when(stbNotifierService.getSetTopBoxes(eventUpdate.getSubscriberId()))
				.thenReturn(setTopBoxesList);
		unicastRequestValidator.validate(eventUpdate);
	}

	/**
	 * test validate with macList and macaddress is same as stb macaddress and
	 * MESSAGE_INFO and eventUpdate.setTriggerType are same
	 */
	@Test(expected = UnicastRequestValidationException.class)
	public void testValidate12() {
		setDefaults();
		EventUpdate eventUpdate = new EventUpdate();
		List<String> macList = new ArrayList<>();
		macList.add("10.10.10.10");
		eventUpdate.setSubscriberId("1015");
		eventUpdate.setTriggerType("S_MESSAGE_INFO");
		eventUpdate.setTimestamp("1483611866");
		eventUpdate.setMACAddress(macList);
		eventUpdate.setTriggerInfo("T_MESS_INFO");
		TriggerMapping triggerMapping = new TriggerMapping();
		triggerMapping.setTriggerType("T_MESS_INFO");
		triggerMapping.setServiceName("T_MESS_INFO");
		when(stbNotifierService.getTriggerMapping(eventUpdate.getTriggerType())).thenReturn(triggerMapping);
		when(unicastTriggerMappingRepository.findByTriggerType("T_MESS_INFO")).thenReturn(triggerMapping);
		List<SetTopBoxes> setTopBoxesList = new ArrayList<>();
		SetTopBoxes setTopBoxes = new SetTopBoxes();
		setTopBoxes.setHwversion("DELL");
		setTopBoxes.setConnectionMode(3);
		setTopBoxes.setMacAddress("10.10.10.10");
		setTopBoxesList.add(setTopBoxes);
		when(stbNotifierService.getSetTopBoxes(eventUpdate.getSubscriberId()))
				.thenReturn(setTopBoxesList);
		unicastRequestValidator.validate(eventUpdate);
	}

	/**
	 * test validate with macList and macaddress is not same as stb macaddress
	 * and MESSAGE_INFO and eventUpdate.setTriggerType are same
	 */
	@Test(expected = UnicastRequestValidationException.class)
	public void testValidate13() {
		setDefaults();
		EventUpdate eventUpdate = new EventUpdate();
		List<String> macList = new ArrayList<>();
		macList.add("10.10.10.10");
		eventUpdate.setSubscriberId("1015");
		eventUpdate.setTriggerType("S_MESSAGE_INFO");
		eventUpdate.setTimestamp("1483611866");
		eventUpdate.setMACAddress(macList);
		eventUpdate.setTriggerInfo("T_MESS_INFO");
		TriggerMapping triggerMapping = new TriggerMapping();
		triggerMapping.setTriggerType("T_MESS_INFO");
		triggerMapping.setServiceName("T_MESS_INFO");
		when(unicastTriggerMappingRepository.findByTriggerType("T_MESS_INFO")).thenReturn(triggerMapping);
		List<SetTopBoxes> setTopBoxesList = new ArrayList<>();
		SetTopBoxes setTopBoxes = new SetTopBoxes();
		setTopBoxes.setHwversion("DELL");
		setTopBoxes.setConnectionMode(3);
		setTopBoxes.setMacAddress("10.10.10.12");
		setTopBoxesList.add(setTopBoxes);
		when(stbNotifierService.getSetTopBoxes(eventUpdate.getSubscriberId()))
				.thenReturn(setTopBoxesList);
		when(stbNotifierService.getTriggerMapping(eventUpdate.getTriggerType())).thenReturn(triggerMapping);
		unicastRequestValidator.validate(eventUpdate);
	}

	@Test(expected = UnicastRequestValidationException.class)
	public void testValidate14() {
		setDefaults();
		EventUpdate eventUpdate = new EventUpdate();
		eventUpdate.setSubscriberId("1015");
		eventUpdate.setTriggerType("T_MESS_INFO");
		eventUpdate.setTimestamp("1483611866");
		eventUpdate.setTriggerInfo("T_MESS_INFO");
		TriggerMapping triggerMapping = new TriggerMapping();
		triggerMapping.setTriggerType("T_MESS_INFO");
		triggerMapping.setServiceName("T_MESS_INFO");
		List<SetTopBoxes> setTopBoxesList = new ArrayList<>();
		SetTopBoxes setTopBoxes = new SetTopBoxes();
		setTopBoxes.setHwversion("DELL");
		setTopBoxes.setConnectionMode(3);
		setTopBoxes.setMacAddress("10.10.10.10");
		setTopBoxesList.add(setTopBoxes);
		when(stbNotifierService.getSetTopBoxes(eventUpdate.getSubscriberId()))
				.thenReturn(setTopBoxesList);
		when(stbNotifierService.getTriggerMapping(eventUpdate.getTriggerType())).thenReturn(triggerMapping);
		unicastRequestValidator.validate(eventUpdate);
	}

	/**
	 * test validate with connection mode !=3
	 */
	@Test(expected = UnicastRequestValidationException.class)
	public void testValidate15() {
		setDefaults();
		EventUpdate eventUpdate = new EventUpdate();
		eventUpdate.setSubscriberId("1015");
		eventUpdate.setTriggerType("T_MESS_INFO");
		eventUpdate.setTimestamp("1483611866");
		eventUpdate.setTriggerInfo("T_MESS_INFO");
		TriggerMapping triggerMapping = new TriggerMapping();
		triggerMapping.setTriggerType("T_MESS_INFO");
		triggerMapping.setServiceName("T_MESS_INFO");
		when(unicastTriggerMappingRepository.findByTriggerType("T_MESS_INFO")).thenReturn(triggerMapping);
		List<SetTopBoxes> setTopBoxesList = new ArrayList<>();
		SetTopBoxes setTopBoxes = new SetTopBoxes();
		setTopBoxes.setHwversion("DELL");
		setTopBoxes.setConnectionMode(3);
		setTopBoxes.setMacAddress("10.10.10.10");
		setTopBoxesList.add(setTopBoxes);
		when(stbNotifierService.getSetTopBoxes(eventUpdate.getSubscriberId()))
				.thenReturn(setTopBoxesList);
		when(stbNotifierService.getTriggerMapping(eventUpdate.getTriggerType())).thenReturn(triggerMapping);
		unicastRequestValidator.validate(eventUpdate);
	}

	private void setDefaults() {
		try {
			setFinalStatic(UnicastRequestValidator.class,"ERRORCODE","ACN_3020");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static void setFinalStatic(Class clazz, String fieldName, Object newValue)
			throws NoSuchFieldException, IllegalAccessException {
		Field field = clazz.getDeclaredField(fieldName);
		field.setAccessible(true);
		Field modifiers = field.getClass().getDeclaredField("modifiers");
		modifiers.setAccessible(true);
		modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		field.set(null, newValue);
	}
	
	/**
	 * test length of TriggerType
	 */
	@Test(expected = UnicastRequestValidationException.class)
	public void testValidateLength() {
		EventUpdate eventUpdate = new EventUpdate();
		eventUpdate.setSubscriberId("12121");
		eventUpdate.setTriggerType("thequickbrownfoxjumpsrightoverthelittlelazydogthequickbrownfoxjumpsrightoverthelittlelazydog");
		eventUpdate.setTimestamp("1483611866");
		unicastRequestValidator.validate(eventUpdate);
	}

	/**
	 * test length of timestamp
	 */
	@Test(expected = UnicastRequestValidationException.class)
	public void testValidateLength2() {
		EventUpdate eventUpdate = new EventUpdate();
		eventUpdate.setSubscriberId("1212");
		eventUpdate.setTriggerType("T_Subscriber_Info");
		eventUpdate.setTimestamp("1483611866234234");
		unicastRequestValidator.validate(eventUpdate);
	}
	
	/**
	 * test length of SubscriberId
	 */
	@Test(expected = UnicastRequestValidationException.class)
	public void testValidateLength3() {
		EventUpdate eventUpdate = new EventUpdate();
		eventUpdate.setSubscriberId("1212121212121212121212");
		eventUpdate.setTriggerType("T_Subscriber_Info");
		eventUpdate.setTimestamp("1483611866123");
		unicastRequestValidator.validate(eventUpdate);
	}
	
	/**
	 * test subscriber alphanumeric
	 */
	@Test(expected = UnicastRequestValidationException.class)
	public void testSubscriberIdNumericValidation() {
		EventUpdate eventUpdate = new EventUpdate();
		eventUpdate.setSubscriberId("ab123");
		eventUpdate.setTriggerType("T_Subscriber_Info");
		eventUpdate.setTimestamp("1483611866");
		unicastRequestValidator.validate(eventUpdate);
	}
	
	/**
	 * test timeStampNumericValidation alphanumeric
	 */
	@Test(expected = UnicastRequestValidationException.class)
	public void testTimeStampNumericValidation() {
		EventUpdate eventUpdate = new EventUpdate();
		eventUpdate.setSubscriberId("12121");
		eventUpdate.setTriggerType("T_Subscriber_Info");
		eventUpdate.setTimestamp("ab1483611866");
		unicastRequestValidator.validate(eventUpdate);
	}
	
	/**
	 * test triggerInfoValidation
	 */
	@Test(expected = UnicastRequestValidationException.class)
	public void testTriggerInfoValidation() {

		EventUpdate eventUpdate = new EventUpdate();
		eventUpdate.setSubscriberId("1015");
		eventUpdate.setTriggerType("asdsd");
		eventUpdate.setTimestamp("1483611866");
		when(stbNotifierService.isMessageInfoTrigger(eventUpdate.getTriggerType())).thenReturn(true);
		unicastRequestValidator.validate(eventUpdate);
	}
	
	/**
	 * test triggerInfoValidation
	 */
	@Test(expected = UnicastRequestValidationException.class)
	public void testTriggerTypeValidation() {

		EventUpdate eventUpdate = new EventUpdate();
		eventUpdate.setSubscriberId("1015");
		eventUpdate.setTriggerType("asdsd");
		eventUpdate.setTimestamp("1483611866");
		eventUpdate.setTriggerInfo("testing");
		when(stbNotifierService.getTriggerMapping(eventUpdate.getTriggerType())).thenReturn(null);
		when(stbNotifierService.isMessageInfoTrigger(eventUpdate.getTriggerType())).thenReturn(true);
		unicastRequestValidator.validate(eventUpdate);
	}
	
	/**
	 * testing validateMacAddress for macLenght>20
	 */
	@Test(expected = UnicastRequestValidationException.class)
	public void testValidateMacAddress() {
		setDefaults();
		EventUpdate eventUpdate = new EventUpdate();
		List<String> macList = new ArrayList<>();
		macList.add("10.10.10.10");
		eventUpdate.setSubscriberId("1015");
		eventUpdate.setTriggerType("S_MESSAGE_INFO");
		eventUpdate.setTimestamp("1483611866");
		eventUpdate.setMACAddress(macList);
		eventUpdate.setTriggerInfo("T_MESS_INFO");
		TriggerMapping triggerMapping = new TriggerMapping();
		triggerMapping.setTriggerType("T_MESS_INFO");
		triggerMapping.setServiceName("T_MESS_INFO");
		when(unicastTriggerMappingRepository.findByTriggerType("T_MESS_INFO")).thenReturn(triggerMapping);
		List<SetTopBoxes> setTopBoxesList = new ArrayList<>();
		SetTopBoxes setTopBoxes = new SetTopBoxes();
		setTopBoxes.setHwversion("DELL");
		setTopBoxes.setConnectionMode(3);
		setTopBoxes.setMacAddress("101222.101212.101212.112122");
		setTopBoxesList.add(setTopBoxes);
		when(stbNotifierService.getSetTopBoxes(eventUpdate.getSubscriberId()))
				.thenReturn(setTopBoxesList);
		when(stbNotifierService.getTriggerMapping(eventUpdate.getTriggerType())).thenReturn(triggerMapping);
		unicastRequestValidator.validate(eventUpdate);
	}
	
	/**
	 * testing validateMacAddress for macLenght>20
	 */
	@Test(expected = UnicastRequestValidationException.class)
	public void testValidateStampAfterDate() {
		setDefaults();
		EventUpdate eventUpdate = new EventUpdate();
		List<String> macList = new ArrayList<>();
		macList.add("10.10.10.10");
		eventUpdate.setSubscriberId("1015");
		eventUpdate.setTriggerType("S_MESSAGE_INFO");
		eventUpdate.setTimestamp("2209440807");
		eventUpdate.setMACAddress(macList);
		eventUpdate.setTriggerInfo("T_MESS_INFO");
		TriggerMapping triggerMapping = new TriggerMapping();
		triggerMapping.setTriggerType("T_MESS_INFO");
		triggerMapping.setServiceName("T_MESS_INFO");
		when(unicastTriggerMappingRepository.findByTriggerType("T_MESS_INFO")).thenReturn(triggerMapping);
		List<SetTopBoxes> setTopBoxesList = new ArrayList<>();
		SetTopBoxes setTopBoxes = new SetTopBoxes();
		setTopBoxes.setHwversion("DELL");
		setTopBoxes.setConnectionMode(3);
		setTopBoxes.setMacAddress("101222.101212.101212.112122");
		setTopBoxesList.add(setTopBoxes);
		when(stbNotifierService.getSetTopBoxes(eventUpdate.getSubscriberId()))
				.thenReturn(setTopBoxesList);
		when(stbNotifierService.getTriggerMapping(eventUpdate.getTriggerType())).thenReturn(triggerMapping);
		unicastRequestValidator.validate(eventUpdate);
	}
	
	@Test
	public void testValidatorNullValue()
	{
		Assert.assertEquals(unicastRequestValidator.isNumeric(null),false);
	}
	@Test
	public void testUnicastRequestValidationException() throws NoSuchFieldException, IllegalAccessException
	{
		Object[] obj=new Object[1];
		UnicastRequestValidationException unicastRequestValidationException=new UnicastRequestValidationException("ACN_3020", obj);
		unicastRequestValidationException.getMsgParamsArray();
		Assert.assertEquals(unicastRequestValidationException.getErrorCode(),"ACN_3020");
	}
	
	

}
