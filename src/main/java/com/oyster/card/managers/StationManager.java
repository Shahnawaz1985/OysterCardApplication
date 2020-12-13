package com.oyster.card.managers;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.oyster.card.beans.Station;
import com.oyster.card.beans.Zone;
import com.oyster.card.exception.IllegalParameterException;

/**
 * 
 * @author Shahnawaz
 *
 */
@Service
public class StationManager {
	
	private static Logger logger = Logger.getLogger(StationManager.class);

	private Map<String, Station> stations = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	
	@Autowired
	private CacheManager cacheManager;

	public void initialize(Map<String, List<Integer>> stationNameWithZoneIds) throws IllegalParameterException {
		Cache cache = null;
		cache = findStationCache();
		if(null != cache) {
			System.out.println("Checking station details in cache.");
			SimpleValueWrapper temp = (SimpleValueWrapper)cache.get("stations_cache");
			if(null != temp) {
				System.out.println("Station details found in cache.");
				Object cacheValue = temp.get();
				if(cacheValue != null && cacheValue instanceof Map<?, ?>) {
					stations.putAll((Map<String, Station>)cacheValue);
					System.out.println("stations detail populated from cache :"+stations);
				}
				populateRequestAttributes();
			}else {
				System.out.println("Station details not found in cache.");
				initializeStations(stationNameWithZoneIds);
				cache.put("stations_cache", stations);
			}
		}
	}

	public Station findStation(String name) {
		return stations.get(name);
	}
	
	private Cache findStationCache() {
		return cacheManager.getCache("oyster_app_cache");
	}
	
	private void initializeStations(Map<String, List<Integer>> stationNameWithZoneIds) {
		if (stationNameWithZoneIds == null || stationNameWithZoneIds.size() == 0) {
			logger.error("System can't be initialized, there should be some stations to start with");
			throw new IllegalParameterException("System can't be initialized, there should be some stations to start with");
		}else{
			for (Entry<String, List<Integer>> station : stationNameWithZoneIds.entrySet()) {
				Set<Zone> zoneList = new HashSet<>();
				List<Integer> zoneIds = station.getValue();
				if (zoneIds == null || zoneIds.isEmpty()) {
					logger.error("System can't be initialized, each station should come under at least one zone");
					throw new IllegalParameterException(
							"System can't be initialized, each station should come under at least one zone");
				}
				for (Integer zoneId : zoneIds) {
					Zone zone = new Zone(zoneId);
					zoneList.add(zone);
				}
				String name = station.getKey();
				stations.put(name, new Station(name, zoneList));	
				populateRequestAttributes();
			}			
		}
	}
	
	private void populateRequestAttributes() {
		if(null != RequestContextHolder.getRequestAttributes() && !stations.isEmpty()) {
			RequestContextHolder.getRequestAttributes().
				setAttribute("station_details", stations, RequestAttributes.SCOPE_REQUEST);
			}
	}

}
