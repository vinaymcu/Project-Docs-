package com.acn.avs.unicast.util;

import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.acn.avs.unicast.entity.TriggerMessage;
import com.acn.avs.unicast.event.param.TqsNotificationParam;
import com.acn.avs.unicast.repository.UnicastTriggerMessageRepository;
import com.acn.avs.unicast.service.TqsNotifierService;

/**
 * @author happy.dhingra
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class UnicastBackGroundExecuterTest {

	@InjectMocks
	private UnicastBackgroundProcessExecutor unicastBackgroundProcessExecutor;

	@Mock
	UnicastTriggerMessageRepository unicastTriggerMessageRepository;

	@Mock
	TqsNotifierService tqsNotifierService;
	@Mock
	ScheduledExecutorService scheduledExecutorService;

	 /**
     * serviceAppName
     */
    @Value("${spring.application.name}")
    private String serviceAppName;
	@Mock
	private DiscoveryClient discoveryClient;

	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * testing process method with empty List<TriggerMessage>
	 */
	@Test
	public void testProcess() {
		try {
			List<TriggerMessage> messageList = new ArrayList<>();
			when(unicastTriggerMessageRepository.findByInstanceId(0)).thenReturn(messageList);
			Method method = UnicastBackgroundProcessExecutor.class.getDeclaredMethod("process");
			method.setAccessible(true);
			method.invoke(unicastBackgroundProcessExecutor);

		} catch (Exception e) {
			Assert.fail();
		}
	}

	/**
	 * testing process method with empty List<TriggerMessage>
	 */
	@Test
	public void testTriggerMessagePresisted() {
		try {
			List<TriggerMessage> messageList = new ArrayList<>();
			when(unicastTriggerMessageRepository.findAll()).thenReturn(messageList);
			Method method = UnicastBackgroundProcessExecutor.class.getDeclaredMethod("isAnyTriggerMessagePresisted");
			method.setAccessible(true);
			method.invoke(unicastBackgroundProcessExecutor);
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	/**
	 * testing process method with empty List<TriggerMessage>
	 */
	@Test
	public void testServiceId() {
		
		String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
        }
        
		try {
			List<TriggerMessage> messageList = new ArrayList<>();
			when(unicastTriggerMessageRepository.findAll()).thenReturn(messageList);
			Method method = UnicastBackgroundProcessExecutor.class.getDeclaredMethod("generateServiceId",String.class);
			method.setAccessible(true);
			method.invoke(unicastBackgroundProcessExecutor,host);
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	/** private boolean isAnyTriggerMessagePresisted(){
        return CollectionUtils.isNotEmpty(unicastTriggerMessageRepository.findAll());
    }
	 * testing process method with empty List<TriggerMessage>
	 * with updateCount
	 */
	@Test
	public void testProcess2() {
		try {
			String host = null;
	        try {
	            host = InetAddress.getLocalHost().getHostAddress();
	        } catch (UnknownHostException e) {
	            // TODO Auto-generated catch block
	        }
			List<TriggerMessage> messageList = new ArrayList<>();
			when(unicastTriggerMessageRepository.findByInstanceId(0)).thenReturn(messageList);
			when(unicastTriggerMessageRepository.updateByInstanceId(getServiceId(host,"-1"), getInstanceIdsByDiscoveryClient())).thenReturn(2);
			Method method = UnicastBackgroundProcessExecutor.class.getDeclaredMethod("process");
			method.setAccessible(true);
			method.invoke(unicastBackgroundProcessExecutor);

		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	/**
	 * testing process method with trigger list
	 */
	@Test
	public void testProcessWithTrigger() {
		try {
			String host = null;
	        try {
	            host = InetAddress.getLocalHost().getHostAddress();
	        } catch (UnknownHostException e) {
	            // TODO Auto-generated catch block
	        }
			List<TriggerMessage> messageList = new ArrayList<>();
			TriggerMessage triggerMessage = new TriggerMessage();
			messageList.add(triggerMessage);
			when(unicastTriggerMessageRepository.findByInstanceId(0)).thenReturn(messageList);
			when(tqsNotifierService.notify(triggerMessage)).thenReturn(200);
			Method method = UnicastBackgroundProcessExecutor.class.getDeclaredMethod("process");
			method.setAccessible(true);
			method.invoke(unicastBackgroundProcessExecutor);
		} catch (Exception e) {
			Assert.fail();
		}

	}

	/**
	 * testing process method when
	 * tqsNotifierService.notify(triggerMessage) returns 400
	 */
	@Test
	public void testProcessWithTrigger2() {
		try {
			String host = null;
	        try {
	            host = InetAddress.getLocalHost().getHostAddress();
	        } catch (UnknownHostException e) {
	            // TODO Auto-generated catch block
	        }
			List<TriggerMessage> messageList = new ArrayList<>();
			TriggerMessage triggerMessage = new TriggerMessage();
			messageList.add(triggerMessage);
			when(unicastTriggerMessageRepository.findByInstanceId(0)).thenReturn(messageList);
			when(tqsNotifierService.notify(triggerMessage)).thenReturn(400);
			Method method = UnicastBackgroundProcessExecutor.class.getDeclaredMethod("process");
			method.setAccessible(true);
			method.invoke(unicastBackgroundProcessExecutor);
		} catch (Exception e) {
			Assert.fail();
		}

	}

	/**
	 * testing process method when
	 * tqsNotifierService.notify(triggerMessage) returns 400
	 * and maxRetryAccessCount is 4
	 */
	@Test
	public void testProcessWithTrigger3() {
		try {
			String host = null;
	        try {
	            host = InetAddress.getLocalHost().getHostAddress();
	        } catch (UnknownHostException e) {
	            // TODO Auto-generated catch block
	        }
			Field field = UnicastBackgroundProcessExecutor.class.getDeclaredField("maxRetryAccessCount");
			field.setAccessible(true);
			field.setInt(unicastBackgroundProcessExecutor, 4);
			List<TriggerMessage> messageList = new ArrayList<>();
			TriggerMessage triggerMessage = new TriggerMessage();
			messageList.add(triggerMessage);
			when(unicastTriggerMessageRepository.findByInstanceId(0)).thenReturn(messageList);
			when(tqsNotifierService.notify(triggerMessage)).thenReturn(400);
			Method method = UnicastBackgroundProcessExecutor.class.getDeclaredMethod("process");
			method.setAccessible(true);
			method.invoke(unicastBackgroundProcessExecutor);
		} catch (Exception e) {
			Assert.fail();
		}

	}

	/**
	 * testing persistMessagesAndInitiateScheduler method with maxRetryAccessCount is 0
	 */
	@Test
	public void testPersistMessagesAndInitiateScheduler1() {
		try {
			Field field = UnicastBackgroundProcessExecutor.class.getDeclaredField("maxRetryAccessCount");
			field.setAccessible(true);
			field.setInt(unicastBackgroundProcessExecutor, 0);
		} catch (Exception e) {
			Assert.fail();
		}
		when(scheduledExecutorService.isShutdown()).thenReturn(true);
		TqsNotificationParam tqsNotificationParam = new TqsNotificationParam("T_MESSAGE_INFO", "10.125.133.85", 1000l);
		unicastBackgroundProcessExecutor.persistMessagesAndInitiateScheduler(tqsNotificationParam, "Unable to send");
	}

	
	/**
	 * testing persistMessagesAndInitiateScheduler method with maxRetryAccessCount is 4
	 */
	@Test
	public void testPersistMessagesAndInitiateScheduler() {
		try {
			Field field = UnicastBackgroundProcessExecutor.class.getDeclaredField("maxRetryAccessCount");
			field.setAccessible(true);
			field.setInt(unicastBackgroundProcessExecutor, 4);
			
			Field field2 = UnicastBackgroundProcessExecutor.class.getDeclaredField("repeatInterval");
			field2.setAccessible(true);
			field2.setInt(unicastBackgroundProcessExecutor, 1);
		} catch (Exception e) {
			Assert.fail();
		}
		when(scheduledExecutorService.isShutdown()).thenReturn(true);
		TqsNotificationParam tqsNotificationParam = new TqsNotificationParam("T_MESSAGE_INFO", "10.125.133.85", 1000l);
		unicastBackgroundProcessExecutor.persistMessagesAndInitiateScheduler(tqsNotificationParam, "Unable to send");
	}
	
	/**
	 * testing persistMessagesAndInitiateScheduler method with maxRetryAccessCount is 4
	 * scheduledExecutorService.isShutdown() false
	 */
	@Test
	public void testPersistMessagesAndInitiateScheduler2() {
		try {
			Field field = UnicastBackgroundProcessExecutor.class.getDeclaredField("maxRetryAccessCount");
			field.setAccessible(true);
			field.setInt(unicastBackgroundProcessExecutor, 4);
			
			Field field2 = UnicastBackgroundProcessExecutor.class.getDeclaredField("repeatInterval");
			field2.setAccessible(true);
			field2.setInt(unicastBackgroundProcessExecutor, 1);
		} catch (Exception e) {
			Assert.fail();
		}
		when(scheduledExecutorService.isShutdown()).thenReturn(false);
		TqsNotificationParam tqsNotificationParam = new TqsNotificationParam("T_MESSAGE_INFO", "10.125.133.85", 1000l);
		unicastBackgroundProcessExecutor.persistMessagesAndInitiateScheduler(tqsNotificationParam, "Unable to send");
	}

	/**
	 * testing updateRetryCount
	 */
	@Test
	public void testRetryCount() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		TriggerMessage message = new TriggerMessage();
		message.setMacAddress("10.10.10.10");
		int retryCount = 3;
		try
		{
		unicastBackgroundProcessExecutor.updateRetryCount(message, retryCount);
		}
		catch(Exception e)
		{
			Assert.fail();
		}
	}
	
	private int getServiceId(String host,String servicePort) {
		int prime = 31;
		int result = 1;
		result = result*prime + host.hashCode();
		result = result*prime + servicePort.hashCode();
		return result;
	}
	 /**
     * getServiceIdsByDiscoveryClient
     * 
     * @return String
     */
    private List<Integer> getInstanceIdsByDiscoveryClient() {
    	String host = null;
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
        }
        List<Integer> serviceIds = new ArrayList<Integer>();
        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances(serviceAppName);
        
        for (ServiceInstance serviceInstance : serviceInstanceList) {
            serviceIds.add(getServiceId(host,String.valueOf(serviceInstance.getPort())));
        }
        return serviceIds;
    }
    
    
  
}
