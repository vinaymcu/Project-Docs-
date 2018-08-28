package com.acn.avs.unicast.service.impl;

import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

import com.acn.avs.unicast.config.ConfigurationProperties;
import com.acn.avs.unicast.entity.Notifications;
import com.acn.avs.unicast.entity.SetTopBoxes;
import com.acn.avs.unicast.entity.TriggerMapping;
import com.acn.avs.unicast.event.param.StbNotificationParam;
import com.acn.avs.unicast.repository.StbManagerRepository;
import com.acn.avs.unicast.repository.UnicastTriggerMappingRepository;
import com.google.gson.Gson;

/**
 * @author happy.dhingra
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class StbNotifierServiceImplTest {

	/** The Stb Notifier Service Impl. */
	@InjectMocks
	private StbNotifierServiceImpl stbNotifierService;

	Gson gson=new Gson();
	@Mock
	StbManagerRepository stbManagerRepository;
	@Mock
	UnicastTriggerMappingRepository unicastTriggerMappingRepository;
	
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
	 * test notifyUdp method
	 */
	@Test
	public void testNotifyUdp() {
		settingPrivateValues();
		StbNotificationParam stbNotificationParam = new StbNotificationParam("AS101",
				stbNotifierService.getUpdateEventAsJson("T_SUBSCRIBER_INFO", "Info about trigger"), "10.125.133.32", 8080,
				"UDP");
		try {
			stbNotifierService.notify(stbNotificationParam);
		} catch (Exception e) {
			Assert.fail();
		}

	}

	/**
	 * test notify method with invalid protoc0l
	 */
	@Test
	public void testNotifyHttp() {
		settingPrivateValues();
		StbNotificationParam stbNotificationParam = new StbNotificationParam("AS101",
				stbNotifierService.getUpdateEventAsJson("T_SUBSCRIBER_INFO", "Info about trigger"), "10.125.133.32", 8080,
				"Http");
		try {
			stbNotifierService.notify(stbNotificationParam);
		} catch (Exception e) {
			Assert.fail();
		}

	}

	/**
	 * test notifyTcp method
	 */
	@Test
	public void testNotifyTcp() {
		settingPrivateValues();
		StbNotificationParam stbNotificationParam = new StbNotificationParam("AS101",
				stbNotifierService.getUpdateEventAsJson("T_SUBSCRIBER_INFO", "Info about trigger"), "10.125.133.32", 9093,
				"TCP");
		try {
			stbNotifierService.notify(stbNotificationParam);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	/**
	 * test notifyTcp method exception by changing port
	 */
	@Test
	public void testNotifyTcpException() {
		settingPrivateValues();
		StbNotificationParam stbNotificationParam = new StbNotificationParam("AS101",
				stbNotifierService.getUpdateEventAsJson("T_SUBSCRIBER_INFO", "Info about trigger"), "10.125.133.32", 8444,
				"TCP");
		try {
			stbNotifierService.notify(stbNotificationParam);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	/**
	 * test fetchMacList method
	 */
	@Test
	public void testGetSetTopBoxes() {
		Assert.assertEquals(stbNotifierService.getSetTopBoxes("1").size(), 0);
	}

	/**
	 * Test isHwVerionAssciateWithServiceName Test getServiceNameByTriggerType
	 * 
	 */
	@Test
	public void testGetAssignedServiceName() {
		TriggerMapping triggerMapping = new TriggerMapping();
		triggerMapping.setServiceName("S_MESSAGE_INFO");
		when(unicastTriggerMappingRepository.findByTriggerType("S_MESSAGE_INFO")).thenReturn(triggerMapping);
		when(stbManagerRepository.isHwVerionAssciateWithServiceName("S_MESSAGE_INFO", "S_MESSAGE_INFO"))
				.thenReturn(false);
		Assert.assertEquals(stbNotifierService.isHwVerionAssciateWithServiceName("S_MESSAGE_INFO", "S_MESSAGE_INFO"),
				false);
		Assert.assertEquals(stbNotifierService.getServiceNameByTriggerType("S_MESSAGE_INFO"), "S_MESSAGE_INFO");
	}

	/**
	 * Test Get SetTopBox with protocol and port detail Test
	 * isMessageInfoTrigger Test isHwVerionAssciateWithServiceName
	 * 
	 */
	@Test
	public void testGetAssignedServiceName2() {
		SetTopBoxes setTopBoxes = new SetTopBoxes();
		when(stbManagerRepository.isHwVerionAssciateWithServiceName("T_SUBSCRIBER_INFO", "S_MESSAGE_INFO"))
				.thenReturn(true);
		Assert.assertEquals(stbNotifierService.isHwVerionAssciateWithServiceName("T_SUBSCRIBER_INFO", "S_MESSAGE_INFO"),
				true);
		stbNotifierService.isHwVerionAssciateWithServiceName("S_MESSAGE_INFO", "S_MESSAGE_INFO");
		Assert.assertEquals(stbNotifierService.isMessageInfoTrigger("S_MESSAGE_INFO"), true);
		Assert.assertEquals(stbNotifierService.isMessageInfoTrigger("T_SUBSCRIBER_INFO"), false);
		when(stbManagerRepository.getSetTopBoxPortAndProtocol(1, "STB_TRIGGER_UPDATE")).thenReturn(setTopBoxes);
		Assert.assertEquals(stbNotifierService.getSetTopBoxPortAndProtocol(1, "STB_TRIGGER_UPDATE"), setTopBoxes);
	}

	@Test
	public void testGetNotifications() {
		List<SetTopBoxes> tqsNotifications = new ArrayList<>();
		List<SetTopBoxes> stbNotifications = new ArrayList<>();
		Notifications notifications = new Notifications(tqsNotifications, stbNotifications);
		Assert.assertEquals(stbNotifierService.getNotifications("1", "ADGST").getStbNotifications().size(),
				notifications.getStbNotifications().size());
		Assert.assertEquals(stbNotifierService.getNotifications("1", "ADGST").getTqsNotifications().size(),
				notifications.getTqsNotifications().size());

	}

	@Test
	public void testGetNotifications2() {
		List<SetTopBoxes> tqsNotifications = new ArrayList<>();
		List<SetTopBoxes> stbNotifications = new ArrayList<>();

		settingPrivateValues();
		List<SetTopBoxes> setTopBoxesList = new ArrayList<>();
		SetTopBoxes setTopBoxes1 = new SetTopBoxes();
		SetTopBoxes setTopBoxes2 = new SetTopBoxes();
		setTopBoxes1.setConnectionMode(3);
		setTopBoxes2.setConnectionMode(2);
		setTopBoxesList.add(setTopBoxes1);
		setTopBoxesList.add(setTopBoxes2);
		tqsNotifications.add(setTopBoxes1);
		stbNotifications.add(setTopBoxes2);
		Notifications notifications = new Notifications(tqsNotifications, stbNotifications);
		//when(configurationProperties.getInetConnectionMode()).thenReturn(3);
		when(stbManagerRepository.getSetTopBoxes("1", "ADGST")).thenReturn(setTopBoxesList);
		Assert.assertEquals(stbNotifierService.getNotifications("1", "ADGST").getStbNotifications().size(),
				notifications.getStbNotifications().size());
		Assert.assertEquals(stbNotifierService.getNotifications("1", "ADGST").getTqsNotifications().size(),
				notifications.getTqsNotifications().size());
	}
	
	@Test
	public void testGetNotifications3() {
		List<SetTopBoxes> tqsNotifications = new ArrayList<>();
		List<SetTopBoxes> stbNotifications = new ArrayList<>();

		settingPrivateValues();
		List<SetTopBoxes> setTopBoxesList = new ArrayList<>();
		SetTopBoxes setTopBoxes1 = new SetTopBoxes();
		SetTopBoxes setTopBoxes2 = new SetTopBoxes();
		setTopBoxes1.setConnectionMode(3);
		setTopBoxes2.setConnectionMode(2);
		setTopBoxesList.add(setTopBoxes1);
		setTopBoxesList.add(setTopBoxes2);
		tqsNotifications.add(setTopBoxes1);
		stbNotifications.add(setTopBoxes2);
		Notifications notifications = new Notifications(tqsNotifications, stbNotifications);
		//when(configurationProperties.getInetConnectionMode()).thenReturn(3);
		when(stbManagerRepository.getSetTopBoxes("1")).thenReturn(setTopBoxesList);
		Assert.assertEquals(stbNotifierService.getNotifications("1").getStbNotifications().size(),
				notifications.getStbNotifications().size());
		Assert.assertEquals(stbNotifierService.getNotifications("1").getTqsNotifications().size(),
				notifications.getTqsNotifications().size());
	}

	@Test
	public void testGetNotifications4() {
		List<SetTopBoxes> tqsNotifications = new ArrayList<>();
		List<SetTopBoxes> stbNotifications = new ArrayList<>();
		List<String> macAddressList=new ArrayList<>();
		macAddressList.add("ADGST");
		settingPrivateValues();
		List<SetTopBoxes> setTopBoxesList = new ArrayList<>();
		SetTopBoxes setTopBoxes1 = new SetTopBoxes();
		SetTopBoxes setTopBoxes2 = new SetTopBoxes();
		setTopBoxes1.setConnectionMode(3);
		setTopBoxes2.setConnectionMode(2);
		setTopBoxesList.add(setTopBoxes1);
		setTopBoxesList.add(setTopBoxes2);
		tqsNotifications.add(setTopBoxes1);
		stbNotifications.add(setTopBoxes2);
		Notifications notifications = new Notifications(tqsNotifications, stbNotifications);
		//when(configurationProperties.getInetConnectionMode()).thenReturn(3);
		when(stbManagerRepository.getSetTopBoxes("1",macAddressList)).thenReturn(setTopBoxesList);
		Assert.assertEquals(stbNotifierService.getNotifications("1",macAddressList).getStbNotifications().size(),
				notifications.getStbNotifications().size());
		Assert.assertEquals(stbNotifierService.getNotifications("1",macAddressList).getTqsNotifications().size(),
				notifications.getTqsNotifications().size());
	}
	

	@Test
	public void testInitializeInetConnection()
	{
		when(stbManagerRepository.getConnectionIdForImplicitNAT()).thenReturn(2);
		try
		{
		Method method=StbNotifierServiceImpl.class.getDeclaredMethod("initializeInetConnection");
		method.setAccessible(true);
		method.invoke(stbNotifierService);
		}
		catch(Exception e)
		{
			Assert.fail();
		}
		
	}
	/**
	 * setting default values
	 */
	private void settingPrivateValues() {
		try {
			when(configurationProperties.getStbNotificationTimeout()).thenReturn(2000);
			when(stbManagerRepository.getConnectionIdForImplicitNAT()).thenReturn(2);
			setFinalStatic(StbNotifierServiceImpl.class,"TCPPROTOCOL","TCP");
			setFinalStatic(StbNotifierServiceImpl.class,"UDPPROTOCOL","UDP");

			Field field3 = StbNotifierServiceImpl.class.getDeclaredField("gson");
			field3.setAccessible(true);
			field3.set(stbNotifierService, gson);

			Field field1 = StbNotifierServiceImpl.class.getDeclaredField("inetConnectionMode");
			field1.setAccessible(true);
			field1.set(stbNotifierService, 3);

			
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

}
