/*package com.acn.avs.unicast.util;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.acn.avs.unicast.config.UnicastNotiflerApplication;
import com.acn.avs.unicast.repository.UnicastTriggerMessageRepository;
import com.acn.avs.unicast.service.TqsNotifierService;


public class UnicastBackGroundMockTest extends UnicastConfigTest{

	*//** The Stb Notifier Service Impl. *//*
	@InjectMocks
	private UnicastBackgroundProcessExecutor unicastBackgroundProcessExecutor;
	
	*//** The mock mvc. *//*
	private MockMvc mockMvc;
	
	@Mock
	UnicastTriggerMessageRepository unicastTriggerMessageRepository;
	
	@Mock
	DiscoveryClient discoveryClient;
	@Mock
	TqsNotifierService tqsNotifierService;
	@Mock
	ScheduledExecutorService  scheduledExecutorService;
	*//**
	 * Setup.
	 *//*
	@Before
	public void setup() {
		initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(unicastBackgroundProcessExecutor).build();
	}
	
	@Test
	public void testStopProcess() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException
	{
		List<Integer> emptyList=new ArrayList<>();
		Field field=UnicastBackgroundProcessExecutor.class.getDeclaredField("serviceId");
		field.setAccessible(true);
		field.set(unicastBackgroundProcessExecutor, 1);
		when(unicastTriggerMessageRepository.updateByServiceId(1, emptyList)).thenReturn(1);
		Method method2=UnicastBackgroundProcessExecutor.class.getDeclaredMethod("stopProcess");
		method2.setAccessible(true);
		method2.invoke(unicastBackgroundProcessExecutor);
	}
	
	@Test
	public void testGetServiceIdsByDiscoveryClient() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException
	{
		List<ServiceInstance> emptyList=new ArrayList<>();
		Field field=UnicastBackgroundProcessExecutor.class.getDeclaredField("serviceAppName");
		field.setAccessible(true);
		field.set(unicastBackgroundProcessExecutor, "Trigger");
		when(discoveryClient.getInstances("Trigger")).thenReturn(emptyList);
		Method method2=UnicastBackgroundProcessExecutor.class.getDeclaredMethod("getServiceIdsByDiscoveryClient");
		method2.setAccessible(true);
		method2.invoke(unicastBackgroundProcessExecutor);
	}
	
	
}
*/