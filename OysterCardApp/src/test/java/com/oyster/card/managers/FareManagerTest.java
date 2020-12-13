package com.oyster.card.managers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Assert;

import com.oyster.card.beans.Trip;
import com.oyster.card.exception.IllegalParameterException;
import com.oyster.card.managers.FareManager;
import com.oyster.card.managers.StationManager;
import com.oyster.card.managers.TravelCardManager;
import com.oyster.card.utils.OysterCardUtils;

@RunWith(SpringRunner.class)
public class FareManagerTest {
	
	@TestConfiguration
    static class OysterCardServiceTestContextConfiguration {
  
        @Bean
        public StationManager stationManager() {
            return new StationManager();
        }
        
        @Bean
        public OysterCardUtils oysterCardUtils() {
            return new OysterCardUtils();
        }
        
        @Bean
        public FareManager fareManager() {
            return new FareManager();
        }
        
        @Bean
        public TravelCardManager travelCardManager() {
            return new TravelCardManager();
        }
    }
	
	@Autowired
	private FareManager fareManager;	
	@Autowired
	private OysterCardUtils oysterCardUtils;
	
	private static final String ZONES_FARES = "zonesAndFares.txt";
	private static final String DUMMY_ZONES_FARES = "dummy_zonesAndFares.txt";
	
	@Test
	public void testInitialize() {
		Map<String, String> zonesFaresAndCommutationModes = oysterCardUtils.
				createZonesFaresAndCommutationModes(ZONES_FARES);
		fareManager.initialize(zonesFaresAndCommutationModes);
		Assert.assertFalse(zonesFaresAndCommutationModes.isEmpty());
	}
	
	@Test(expected = IllegalParameterException.class)
	public void negative_testInitialize() {
		Map<String, String> zonesFaresAndCommutationModes = oysterCardUtils.
				createZonesFaresAndCommutationModes(DUMMY_ZONES_FARES);
		fareManager.initialize(zonesFaresAndCommutationModes);
	}
	
	@Test
	public void testCalculateCost() {
		List<Trip> tripDetails = new ArrayList<>();
		BigDecimal costIncurred = fareManager.calculateCost(tripDetails);
		Assert.assertFalse(costIncurred.equals(new BigDecimal(0.0)));
	}

}
