package com.acn.avs.unicast.web.rest.controller.unit;

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testng.Assert;

import com.acn.avs.unicast.event.json.EventUpdate;
import com.acn.avs.unicast.event.json.EventUpdateRequest;
import com.acn.avs.unicast.event.json.UnicastRestResponse;
import com.acn.avs.unicast.event.wrapper.UnicastRestResponseWrapper;
import com.acn.avs.unicast.util.UnicastRequestValidator;
import com.acn.avs.unicast.web.rest.controller.UnicastRestController;

/**
 * @author happy.dhingra
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class UnicastRestControllerUTest{

	private static final String SUCCESS_RESPONSE_CODE = "ACN_200";
	
	@InjectMocks
	private UnicastRestController unicastRestController;

	@Mock
	private UnicastRestResponseWrapper unicastRestResponseWrapper;
	
	@Mock
	private RabbitTemplate rabbitTemplate;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}	
	
	@Mock
	UnicastRequestValidator validator;
	
	
	/**
	 *  testing getTrigger message 
	 *  when validator.validate(eventUpdate) 
	 *  returns true
	 *  @throws Exception
	 */
	@Test
	public void testGetTrigger() throws Exception 
	{
		EventUpdateRequest eventUpdatRequest=new EventUpdateRequest();
		EventUpdate eventUpdate=new EventUpdate();
		eventUpdatRequest.setEventUpdate(eventUpdate);
		when(unicastRestResponseWrapper.prepareSuccessResponse()).thenReturn(getResponse());
		when(validator.validate(eventUpdate)).thenReturn(true);
		Assert.assertEquals(unicastRestController.getTrigger(eventUpdatRequest).getStatusCode(),org.springframework.http.HttpStatus.OK);
		
	}
	
	/**
	 *  testing getTrigger message 
	 *  when validator.validate(eventUpdate) 
	 *  returns false
	 *  @throws Exception
	 */
	@Test
	public void testGetTriggerWithValidateFalse() throws Exception 
	{
		EventUpdateRequest eventUpdateWrapper=new EventUpdateRequest();
		EventUpdate eventUpdate=new EventUpdate();
		eventUpdateWrapper.setEventUpdate(eventUpdate);
		when(unicastRestResponseWrapper.prepareSuccessResponse()).thenReturn(getResponse());
		when(validator.validate(eventUpdate)).thenReturn(false);
		unicastRestController.getTrigger(eventUpdateWrapper);
		Assert.assertEquals(unicastRestController.getTrigger(eventUpdateWrapper).getStatusCode(), org.springframework.http.HttpStatus.OK);
		
	}
	
	public UnicastRestResponse getResponse()
	{
		UnicastRestResponse unicastRestResponse = new UnicastRestResponse();
		unicastRestResponse.setResultCode(SUCCESS_RESPONSE_CODE);
		unicastRestResponse.setSystemTime(System.currentTimeMillis());
		return unicastRestResponse;
	}
	
}
