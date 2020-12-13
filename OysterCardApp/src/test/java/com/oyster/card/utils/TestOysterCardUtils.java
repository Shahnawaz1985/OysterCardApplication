package com.oyster.card.utils;

import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.oyster.card.beans.Trip;
import com.oyster.card.exception.IllegalParameterException;
import com.oyster.card.managers.FareManager;
import com.oyster.card.managers.StationManager;
import com.oyster.card.managers.TravelCardManager;

@RunWith(SpringRunner.class)
public class TestOysterCardUtils {
	
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
	private StationManager stationManager;

	@Autowired
	private FareManager fareManager;
	
	@Autowired
	private OysterCardUtils oysterCardUtils;
	
	private static final String STATIONS_ZONES_FILE_PATH = "stationsAndZones.txt";
	private static final String DUMMY_STATIONS_ZONES_FILE_PATH = "dummy_stationsAndZones.txt";
	private static final String ZONES_FARES = "zonesAndFares.txt";
	private static final String DUMMY_ZONES_FARES = "dummy_zonesAndFares.txt";
	private static final String COMPLETE_TRIP_DETAILS = "travelData.txt";
	
	@Test
	public void testCreateStationNameWithZoneIds() {
		Map<String, List<Integer>> stationNameZoneIdMap = oysterCardUtils.
				createStationNameWithZoneIds(STATIONS_ZONES_FILE_PATH);
		Assert.assertFalse(stationNameZoneIdMap.isEmpty());
	}
	
	@Test
	public void negative_testCreateStationNameWithZoneIds() {
		Map<String, List<Integer>> stationNameZoneIdMap = oysterCardUtils.
				createStationNameWithZoneIds(DUMMY_STATIONS_ZONES_FILE_PATH);
		Assert.assertTrue(stationNameZoneIdMap.isEmpty());
	}
	
	@Test
	public void testCreateZonesFaresAndCommutationModes() {
		Map<String, String> zoneFareMap = oysterCardUtils.
				createZonesFaresAndCommutationModes(ZONES_FARES);
		Assert.assertFalse(zoneFareMap.isEmpty());
	}
	
	@Test
	public void negative_testCreateZonesFaresAndCommutationModes() {
		Map<String, String> zoneFareMap = oysterCardUtils.
				createZonesFaresAndCommutationModes(DUMMY_ZONES_FARES);
		Assert.assertTrue(zoneFareMap.isEmpty());
	}
	
	@Test(expected = IllegalParameterException.class)
	public void testExtractTripsFrom() {
		List<Trip> tripDetails = oysterCardUtils.extractTripsFrom(COMPLETE_TRIP_DETAILS);
		Assertions.assertThatExceptionOfType(IllegalParameterException.class);
	}

}
