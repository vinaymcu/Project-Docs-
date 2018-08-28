package com.acn.avs.unicast.service.impl;

import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;
import com.acn.avs.unicast.config.TestConfig;
import com.acn.avs.unicast.config.ConfigurationProperties;
import com.acn.avs.unicast.entity.TriggerMessage;
import com.acn.avs.unicast.event.param.TqsNotificationParam;
import com.acn.avs.unicast.util.UnicastBackgroundProcessExecutor;

/**
 * @author happy.dhingra
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class TqsNotifierServiceImplTest {

	/** The Tqs Notifier Service Impl. */
	@InjectMocks
	private TqsNotifierServiceImpl tqsNotifierServiceImpl;

	@Mock
	RestTemplate restTemplate=new RestTemplate();
	
	@Mock
	UnicastBackgroundProcessExecutor unicastBackgroundProcessExecutor;
	
	@Mock
	ConfigurationProperties configurationProperties;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * test notify method with valid TqsNotificationParam.
	 * @throws URISyntaxException 
	 */
	@Test
	public void testNotifyObj() throws URISyntaxException {
		settingDefaultValue();
		String accessUrl="http://10.125.133.32:8082/triggerservice/writeTrigger";
		String triggerType="T_SUBSCRIBER_INFO";
		String macAddress="10.10.10.10";
		long time=1000l;
		TqsNotificationParam tqsNotificationParam = new TqsNotificationParam(triggerType, macAddress, time);
		URI uri = new URI(accessUrl + "?MACAddress=" + macAddress
				+ "&TriggerType=" + triggerType + "&TimeStamp=" + time);
		when(restTemplate.getForEntity(uri, String.class)).thenReturn(ResponseEntity.ok("OK"));
		try {
			tqsNotifierServiceImpl.notify(tqsNotificationParam);
		} catch (Exception e) {
			Assert.fail();
		}

	}

	/**
	 * test notify method with valid TqsNotificationParam.
	 * when resttemplate throws exception
	 * @throws URISyntaxException 
	 */
	@Test
	public void testNotifyObjWithException() throws URISyntaxException {
		settingDefaultValue();
		String accessUrl="http://10.125.133.32:8082/triggerservice/writeTrigger";
		String triggerType="T_SUBSCRIBER_INFO";
		String macAddress="10.10.10.10";
		long time=1000l;
		TqsNotificationParam tqsNotificationParam = new TqsNotificationParam(triggerType, macAddress, time);
		URI uri = new URI(accessUrl + "?MACAddress=" + macAddress
				+ "&TriggerType=" + triggerType + "&TimeStamp=" + time);
		when(restTemplate.getForEntity(uri, String.class)).thenThrow(new RestClientException("TESTING"));
		try {
			tqsNotifierServiceImpl.notify(tqsNotificationParam);
		} catch (Exception e) {
			Assert.fail();
		}

	}
	
	
	/**
	 * test notify method with valid TriggerMessage.
	 */
	@Test
	public void testNotifyWithTriggerMessage() {
		settingDefaultValue();
		String accessUrl="http://10.125.133.32:8082/triggerservice/writeTrigger";
		String triggerType="T_SUBSCRIBER_INFO";
		String macAddress="10.10.10.10";
		long time=1000l;
		TriggerMessage triggerMessage = new TriggerMessage();
		triggerMessage.setTriggerType(triggerType);
		triggerMessage.setMacAddress(macAddress);
		triggerMessage.setTimestamp(time);
		
		try {
			URI uri = new URI(accessUrl + "?MACAddress=" + macAddress
					+ "&TriggerType=" + triggerType + "&TimeStamp=" + time);
			when(restTemplate.getForEntity(uri, String.class)).thenReturn(ResponseEntity.ok("OK"));
			tqsNotifierServiceImpl.notify(triggerMessage);
			
		} catch (Exception e) {
			Assert.fail();
		}
	}


	/**
	 * test notify method with valid TriggerMessage.
	 * when resttemplate throws exception
	 */
	@Test
	public void testNotifyWithTriggerMessageWithException() {
		settingDefaultValue();
		String accessUrl="http://10.125.133.32:8082/triggerservice/writeTrigger";
		String triggerType="T_SUBSCRIBER_INFO";
		String macAddress="10.10.10.10";
		long time=1000l;
		TriggerMessage triggerMessage = new TriggerMessage();
		triggerMessage.setTriggerType(triggerType);
		triggerMessage.setMacAddress(macAddress);
		triggerMessage.setTimestamp(time);
		
		try {
			URI uri = new URI(accessUrl + "?MACAddress=" + macAddress
					+ "&TriggerType=" + triggerType + "&TimeStamp=" + time);
			when(restTemplate.getForEntity(uri, String.class)).thenThrow(new RestClientException("TESTING"));
			tqsNotifierServiceImpl.notify(triggerMessage);
			
		} catch (Exception e) {
			Assert.fail();
		}
	}
	

	/**
	 * test notify method with null as access url.
	 */
	@Test
	public void testNotifyObjWithNullUrl() {
		TqsNotificationParam tqsNotificationParam = new TqsNotificationParam("T_SUBSCRIBER_INFO", "10.10.10.10", 1000l);
		try {
			tqsNotifierServiceImpl.notify(tqsNotificationParam);
		} catch (Exception e) {
			Assert.fail();
		}
	}



	/**
	 * setting valid access url
	 */
	private void settingDefaultValue() {
		try {
			when(configurationProperties.getAccessUrl()).thenReturn("http://10.125.133.32:8082/triggerservice/writeTrigger");
			Field field1 = TqsNotifierServiceImpl.class.getDeclaredField("restTemplate");
			field1.setAccessible(true);
			field1.set(tqsNotifierServiceImpl, restTemplate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
