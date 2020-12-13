package com.oyster.card.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import com.oyster.card.beans.Station;
import com.oyster.card.exception.IllegalParameterException;
import com.oyster.card.managers.FareManager;
import com.oyster.card.managers.StationManager;
import com.oyster.card.managers.TravelCardManager;
import com.oyster.card.utils.OysterCardUtils;

@RunWith(SpringRunner.class)
public class StationManagerTest {
	
	private static Map<String, Station> stations = null;
	
	@BeforeClass
	public static void setup() {
		stations = new HashMap<String, Station>();
	}
	
	@AfterClass
	public static void cleanup() {
		stations = null;
	}
	
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
	private OysterCardUtils oysterCardUtils;
	
	private static final String STATIONS_ZONES_FILE_PATH = "stationsAndZones.txt";
	private static final String DUMMY_STATIONS_ZONES_FILE_PATH = "dummy_stationsAndZones.txt";
	
	@Test
	public void testInitialize() {
		Map<String, List<Integer>> stationNameWithZoneIds = oysterCardUtils.
					createStationNameWithZoneIds(STATIONS_ZONES_FILE_PATH);
		stationManager.initialize(stationNameWithZoneIds);
		Assert.notNull(stationNameWithZoneIds, "station name picked from file.");
	}
	
	@Test(expected = IllegalParameterException.class)
	public void negative_testInitialize() {
		Map<String, List<Integer>> stationNameWithZoneIds = oysterCardUtils.
					createStationNameWithZoneIds(DUMMY_STATIONS_ZONES_FILE_PATH);
		stationManager.initialize(stationNameWithZoneIds);		
	}

}
