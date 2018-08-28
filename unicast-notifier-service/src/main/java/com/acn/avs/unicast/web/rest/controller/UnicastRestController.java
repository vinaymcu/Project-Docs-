package com.acn.avs.unicast.web.rest.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.acn.avs.unicast.event.json.UnicastRestResponse;
import com.acn.avs.unicast.event.json.EventUpdateRequest;
import com.acn.avs.unicast.event.wrapper.UnicastRestResponseWrapper;
import com.acn.avs.unicast.exception.UnicastRequestValidationException;
import com.acn.avs.unicast.util.UnicastRequestValidator;

/**
 * 
 * @author Anand.Jha
 * 
 * This class is responsible to receive the trigger
 *
 */

@RestController
@RequestMapping(value = "/unicastnotifier", produces = { MediaType.APPLICATION_JSON_VALUE })
public class UnicastRestController {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private UnicastRestResponseWrapper unicastRestResponseWrapper;

	@Autowired
	UnicastRequestValidator validator;

	/**
	 * getTrigger request
	 *
	 * @param eventUpdateRequest
	 *            eventUpdate request
	 * @return the response entity
	 * @throws UnicastRequestValidationException
	 */
	@RequestMapping(value = "/trigger", method = RequestMethod.POST,
			consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<UnicastRestResponse> getTrigger(
			@RequestBody EventUpdateRequest eventUpdateRequest)
					throws UnicastRequestValidationException {
	
		validator.validate(eventUpdateRequest);
		
		rabbitTemplate.convertAndSend(eventUpdateRequest);

		return ResponseEntity.ok(unicastRestResponseWrapper
				.prepareSuccessResponse());
	}

}
