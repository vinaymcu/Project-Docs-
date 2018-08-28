package com.acn.avs.unicast.message;

import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testng.Assert;

import com.acn.avs.unicast.entity.Notifications;
import com.acn.avs.unicast.entity.SetTopBoxes;
import com.acn.avs.unicast.event.json.EventUpdate;
import com.acn.avs.unicast.event.json.EventUpdateRequest;
import com.acn.avs.unicast.exception.UnicastRequestValidationErrorReason;
import com.acn.avs.unicast.exception.UnicastRequestValidationException;
import com.acn.avs.unicast.service.StbNotifierService;
import com.acn.avs.unicast.service.TqsNotifierService;
import com.acn.avs.unicast.util.UnicastRequestValidator;

/**
 * @author happy.dhingra
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class UnicastJmsMessageListenerTest {

	@InjectMocks
	private UnicastJmsMessageListener unicastJmsMessageListener;

	@Mock
	StbNotifierService stbNotifierService;

	@Mock
	TqsNotifierService tqsNotifierService;

	@Mock
	UnicastRequestValidator unicastRequestValidator;
	
	@Mock
	MessageSourceAccessor messageSourceAccessor;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * test onMessage method for tqs server without mac address
	 */
	@Test
	public void testOnMessage() {
		EventUpdateRequest eventUpdateRequest=new EventUpdateRequest();
		Notifications notifications = getEmptyNotifications();
		EventUpdate eventUpdate = getEventUpdate();
		when(stbNotifierService.getNotifications(eventUpdate.getSubscriberId(), ""))
				.thenReturn(notifications);
		when(unicastRequestValidator.validate(eventUpdateRequest)).thenReturn(true);
		eventUpdateRequest.setEventUpdate(eventUpdate);
		eventUpdateRequest.setValidated(false);
		try {
			unicastJmsMessageListener.onMessage(eventUpdateRequest);
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	/**
	 * test onMessage method for tqs server without mac address
	 */
	@Test//(expected=UnicastRequestValidationException.class)
	public void testOnMessageTestException() {
		EventUpdateRequest eventUpdateRequest=new EventUpdateRequest();
		Notifications notifications = getEmptyNotifications();
		EventUpdate eventUpdate = getEventUpdate();
		when(stbNotifierService.getNotifications(eventUpdate.getSubscriberId(), ""))
				.thenReturn(notifications);
		when(unicastRequestValidator.validate(eventUpdateRequest)).thenThrow(new UnicastRequestValidationException("ACN_3020",new Object[]{ UnicastRequestValidationErrorReason.SUBSCRIBER_ID_NOT_NUMERIC.getReason()}));
		eventUpdateRequest.setEventUpdate(eventUpdate);
		eventUpdateRequest.setValidated(false);
			unicastJmsMessageListener.onMessage(eventUpdateRequest);
		
	}

	/**
	 * test onMessage method for tqs server without mac address handling
	 * exception
	 */
	@Test
	public void testOnMessage1() {
		EventUpdateRequest eventUpdateRequest=new EventUpdateRequest();
		Notifications notifications = getEmptyNotifications();
		EventUpdate eventUpdate = getEventUpdate();
		when(stbNotifierService.getNotifications(eventUpdate.getSubscriberId(), ""))
				.thenThrow(new RuntimeException());
		when(unicastRequestValidator.validate(eventUpdateRequest)).thenReturn(true);
		eventUpdateRequest.setEventUpdate(eventUpdate);
		eventUpdateRequest.setValidated(false);
		try {
			unicastJmsMessageListener.onMessage(eventUpdateRequest);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	/**
	 * test onMessage method for tqs server with mac address but empty
	 * notifications
	 */
	@Test
	public void testOnMessage2() {
		EventUpdateRequest eventUpdateRequest=new EventUpdateRequest();
		Notifications notifications = getEmptyNotifications();
		EventUpdate eventUpdate = getEventUpdate();
		List<String> macList = new ArrayList<>();
		macList.add("AMQTP");
		eventUpdate.setMACAddress(macList);
		when(stbNotifierService.getNotifications(eventUpdate.getSubscriberId(),
			eventUpdate.getMACAddress().get(0))).thenReturn(notifications);
		when(unicastRequestValidator.validate(eventUpdateRequest)).thenReturn(true);
		eventUpdateRequest.setEventUpdate(eventUpdate);
		eventUpdateRequest.setValidated(false);
		try {
			unicastJmsMessageListener.onMessage(eventUpdateRequest);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	/**
	 * test onMessage method for tqs server with mac address and
	 * TriggerType("T_SUBSCRIBER_INFO")
	 */
	@Test
	public void testOnMessage4() {
		EventUpdateRequest eventUpdateRequest=new EventUpdateRequest();
		Notifications notifications = getNotifications();
		EventUpdate eventUpdate = getEventUpdate();
		List<String> macList = new ArrayList<>();
		macList.add("AMQTP");
		eventUpdate.setMACAddress(macList);
		eventUpdate.setTriggerType("T_SUBSCRIBER_INFO");
		when(stbNotifierService.getNotifications(eventUpdate.getSubscriberId(),
				eventUpdate.getMACAddress().get(0))).thenReturn(notifications);
		when(stbNotifierService.getSetTopBoxPortAndProtocol(1, null))
				.thenReturn(notifications.getStbNotifications().get(0));
		when(unicastRequestValidator.validate(eventUpdateRequest)).thenReturn(true);
		eventUpdateRequest.setEventUpdate(eventUpdate);
		eventUpdateRequest.setValidated(false);
		try {
			unicastJmsMessageListener.onMessage(eventUpdateRequest);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	/**
	 * test onMessage method for tqs server with mac address and
	 * TriggerType("T_SUBSCRIBER_INFO") and isMessageInfoTrigger
	 * returns true
	 */
	@Test
	public void testOnMessage5() {
		EventUpdateRequest eventUpdateRequest=new EventUpdateRequest();
		Notifications notifications = getNotifications();
		EventUpdate eventUpdate = getEventUpdate();
		List<String> macList = new ArrayList<>();
		macList.add("AMQTP");
		eventUpdate.setMACAddress(macList);
		eventUpdate.setTriggerType("T_SUBSCRIBER_INFO");
		when(stbNotifierService.getNotifications(eventUpdate.getSubscriberId(),
				eventUpdate.getMACAddress().get(0))).thenReturn(notifications);
		when(stbNotifierService.isMessageInfoTrigger(eventUpdate.getTriggerType())).thenReturn(true);
		when(stbNotifierService.getSetTopBoxPortAndProtocol(1, null))
				.thenReturn(notifications.getStbNotifications().get(0));
		when(unicastRequestValidator.validate(eventUpdateRequest)).thenReturn(true);
		eventUpdateRequest.setEventUpdate(eventUpdate);
		eventUpdateRequest.setValidated(false);
		try {
			unicastJmsMessageListener.onMessage(eventUpdateRequest);
		} catch (Exception e) {
			Assert.fail();
		}
	}
	/**
	 * returns valid dummy object for EventUpdate
	 */
	private EventUpdate getEventUpdate() {
		EventUpdate eventUpdate = new EventUpdate();
		eventUpdate.setSubscriberId("102365");
		eventUpdate.setTriggerType("T_SUBSCRIBER_INFO");
		eventUpdate.setTimestamp("1000");
		return eventUpdate;

	}

	/**
	 * returns empty dummy object for List<SetTopBoxes>
	 */
	private Notifications getEmptyNotifications() {
		List<SetTopBoxes> tqsNotifications = new ArrayList<>();
		List<SetTopBoxes> stbNotifications = new ArrayList<>();
		Notifications notifications = new Notifications(tqsNotifications, stbNotifications);
		return notifications;
	}

	/**
	 * returns dummy object for List<SetTopBoxes>
	 */
	private Notifications getNotifications() {
		List<SetTopBoxes> tqsNotifications = new ArrayList<>();
		SetTopBoxes setTopBoxes = new SetTopBoxes();
		setTopBoxes.setMacAddress("AMQP");
		tqsNotifications.add(setTopBoxes);
		List<SetTopBoxes> stbNotifications = new ArrayList<>();
		SetTopBoxes setTopBoxes2 = new SetTopBoxes();
		setTopBoxes2.setExternalIpAddress("10.10.10.10");
		setTopBoxes2.setProtocol("8080");
		setTopBoxes2.setExternalPort(8080);
		setTopBoxes2.setEquipmentId(1);
		stbNotifications.add(setTopBoxes2);
		Notifications notifications = new Notifications(tqsNotifications, stbNotifications);
		return notifications;
	}
	

}
