package com.oyster.card.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 
 * @author Shahnawaz
 *
 */
@Component
public class OysterCardResponseWriter {
	
	private static Logger logger = Logger.getLogger(OysterCardResponseWriter.class);
	
	public Map<String, Object> createOysterCardTripResponse() {
		Map<String, Object> data_operation_map = new LinkedHashMap<String, Object>();
		if(null != RequestContextHolder.getRequestAttributes()) {
			Object stationDetails =  RequestContextHolder.getRequestAttributes()
							.getAttribute("station_details", RequestAttributes.SCOPE_REQUEST);	
			logger.info("station details :"+stationDetails);
			if(null != stationDetails) {
				data_operation_map.put("all_station_details", stationDetails);
			}
			Object allFareDetialsMap = RequestContextHolder.getRequestAttributes()
					.getAttribute("all_fare_details", RequestAttributes.SCOPE_REQUEST);
			logger.info("all_fare_details :"+allFareDetialsMap);
			if(null != allFareDetialsMap) {
				data_operation_map.put("all_fare_details", allFareDetialsMap);	
			}
			Object newCardDetails = RequestContextHolder.getRequestAttributes()
					.getAttribute("new_card", RequestAttributes.SCOPE_REQUEST);
			logger.info("new_card :"+newCardDetails);
			if(null != newCardDetails) {
				data_operation_map.put("new_card", newCardDetails);	
			}
			Object topped_up_card =  RequestContextHolder.getRequestAttributes()
					.getAttribute("topped_up_card", RequestAttributes.SCOPE_REQUEST);
			logger.info("topped_up_card :"+topped_up_card);
			if(null != topped_up_card) {
				data_operation_map.put("topped_up_card", topped_up_card);
			}
			Object trip_details = RequestContextHolder.getRequestAttributes()
					.getAttribute("trips_details", RequestAttributes.SCOPE_REQUEST);
			logger.info("trip_details :"+trip_details);
			if(null != trip_details) {
				data_operation_map.put("trip_details", trip_details);
			}
			Object all_trip_details = RequestContextHolder.getRequestAttributes()
					.getAttribute("total_trips_taken", RequestAttributes.SCOPE_REQUEST);
			logger.info("all_trip_details :"+all_trip_details);
			if(null != all_trip_details) {
				data_operation_map.put("all_trip_details", all_trip_details);
			}			
		}
		return data_operation_map;		
	}

}
