package com.oyster.card.services;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Assert;

import com.oyster.card.beans.TravelCard;
import com.oyster.card.exception.GenericException;
import com.oyster.card.exception.IllegalParameterException;
import com.oyster.card.managers.FareManager;
import com.oyster.card.managers.StationManager;
import com.oyster.card.managers.TravelCardManager;
import com.oyster.card.services.impl.OysterCardServiceImpl;
import com.oyster.card.utils.OysterCardUtils;

@RunWith(SpringRunner.class)
public class OysterCardServiceTest {
	
	private static Logger logger = Logger.getLogger(OysterCardServiceTest.class);
	
	private static final String VALID_INPUT_FILE_FIRST = "travelData1.txt";
	private static final String VALID_INPUT_FILE_SECOND = "travelData2.txt";
	private static final String VALID_INPUT_FILE_THIRD = "travelData3.txt";
	
	private static final String COMPLETE_TRIP_DETAILS = "travelData.txt";
	
	private static final String STATIONS_ZONES_FILE_PATH = "stationsAndZones.txt";
	private static final String ZONES_FARES = "zonesAndFares.txt";
	
	
	@TestConfiguration
    static class OysterCardServiceTestContextConfiguration {
  
        @Bean
        public StationManager stationManager() {
            return new StationManager();
        }
        
        @Bean
        public FareManager fareManager() {
            return new FareManager();
        }
        
        @Bean
        public TravelCardManager travelCardManager() {
            return new TravelCardManager();
        }
        
        @Bean
        public OysterCardUtils oysterCardUtils() {
            return new OysterCardUtils();
        }
        
        @Bean
        public OysterCardService oysterCardService() {
            return new OysterCardServiceImpl();
        }
    }
	
	@Autowired
	private OysterCardService oysterCardService;
	
	private static TravelCard card = new TravelCard(1111, new BigDecimal(10.0));
	
	@Test
	public void testPurchaseNewCard() {
		BigDecimal initialAmount = new BigDecimal(0.0);		
		try {
			 card = oysterCardService.purchaseNewCard(initialAmount);
		} catch (IllegalParameterException e) {
			logger.error(e.getLocalizedMessage());
		}
		Assert.assertTrue(card.getBalance().equals(new BigDecimal(0.0)));
	}
	
	@Test(expected = IllegalParameterException.class)	
	public void negative_testPurchaseNewCard() throws IllegalParameterException {
		BigDecimal initialAmount = new BigDecimal(-1.0);		
		card = oysterCardService.purchaseNewCard(initialAmount);
		Assertions.assertThatExceptionOfType(IllegalParameterException.class);
	}
	
	@Test
	public void testInitializeSystem() {
		try {
			oysterCardService.initializeSystem(STATIONS_ZONES_FILE_PATH, ZONES_FARES);
		} catch (GenericException e) {
			logger.error(e.getLocalizedMessage());
		}
	}
	
	@Test
	public void testTopup() {
		BigDecimal initialAmount = new BigDecimal(15.0);		
		card = oysterCardService.purchaseNewCard(initialAmount);
		oysterCardService.topUpCard(card, new BigDecimal(10.0));
		Assert.assertTrue(card.getBalance().equals(new BigDecimal(25.0)));
	}
	
	@Test(expected = GenericException.class)
	public void negative_testInitializeSystem() throws GenericException {
		oysterCardService.initializeSystem("", "");
		Assertions.assertThatExceptionOfType(GenericException.class);
	}
	
	@Test
	public void testTakeTrips_1() throws GenericException {
		try {
			oysterCardService.takeTrips(card, VALID_INPUT_FILE_FIRST);
			Assertions.assertThatExceptionOfType(null);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
	}
	
	@Test
	public void testTakeTrips_2() throws GenericException {
		try {
			oysterCardService.takeTrips(card, VALID_INPUT_FILE_SECOND);
			Assertions.assertThatExceptionOfType(null);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
	}
	
	@Test
	public void testTakeTrips_3() throws GenericException {
		try {
			oysterCardService.takeTrips(card, VALID_INPUT_FILE_THIRD);
			Assertions.assertThatExceptionOfType(null);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
	}
	
	@Test(expected = GenericException.class)
	public void negative_testTakeTrip() throws Exception {
		oysterCardService.takeTrips(card, "");
		Assertions.assertThatExceptionOfType(GenericException.class);
	}

}
