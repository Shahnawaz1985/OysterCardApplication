package com.oyster.card.managers;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
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

	public void initialize(Map<String, List<Integer>> stationNameWithZoneIds) throws IllegalParameterException {
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
			}
			if(null != RequestContextHolder.getRequestAttributes() && !stations.isEmpty()) {
			RequestContextHolder.getRequestAttributes().
				setAttribute("station_details", stations, RequestAttributes.SCOPE_REQUEST);
			}
		}
		
	}

	public Station findStation(String name) {
		return stations.get(name);
	}

}
