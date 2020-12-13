package com.oyster.card.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.oyster.card.beans.TravelCard;
import com.oyster.card.constants.OysterCardConstants;
import com.oyster.card.services.OysterCardService;
import com.oyster.card.utils.OysterCardResponseWriter;

/**
 * @author Shahnawaz
 */
@RestController
@RequestMapping("/oyster")
public class OysterWebController {
	/**
	 * 
	 */
	private static Logger logger = Logger.getLogger(OysterWebController.class);
	/**
	 * 
	 */
	@Autowired
	private OysterCardService oysterCardService;
	/**
	 * 
	 */
	@Autowired
	private OysterCardResponseWriter oysterCardResponseWriter;
	/**
	 * 		
	 * @return String
	 * @throws Exception Exception
	 */
	@RequestMapping(value = "/test-oyster-service", method = RequestMethod.GET)
	public Map<String, Object> testOysterCard() throws Exception {
		Map<String, Object> data_operation_map = null;
		TravelCard card = null;
		oysterCardService.initializeSystem(OysterCardConstants.STATIONS_AND_ZONES_TXT, 
					OysterCardConstants.ZONES_AND_FARES_TXT);
		card = oysterCardService.purchaseNewCard(BigDecimal.valueOf(0.0));
		BigDecimal topUp = new BigDecimal(30.0);
		if(null != card){
			oysterCardService.topUpCard(card, topUp);
			oysterCardService.takeTrips(card, OysterCardConstants.TRAVEL_DATA_TXT);
		}		
		data_operation_map = oysterCardResponseWriter.createOysterCardTripResponse();
		logger.info("Sample Data for operation :"+data_operation_map);
		return data_operation_map;
	}
}
