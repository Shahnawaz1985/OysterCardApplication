package com.oyster.card.managers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.oyster.card.beans.CommutationMode;
import com.oyster.card.beans.Fare;
import com.oyster.card.beans.Station;
import com.oyster.card.beans.Trip;
import com.oyster.card.beans.Zone;
import com.oyster.card.exception.GenericException;
import com.oyster.card.exception.IllegalParameterException;

/**
 * 
 * @author Shahnawaz
 *
 */
@Service
public class FareManager {
	
	private static Logger logger = Logger.getLogger(FareManager.class);
	
	private static final String FARE_COMMUTATION_MODE_DELIM = "@";
	private static final String WILD_CARD = "*";
	private List<Fare> fares = new ArrayList<>();
	private BigDecimal maxFare = new BigDecimal(0);
	private Map<CommutationMode, Fare> genericFares = new HashMap<>();
	
	

	public void initialize(Map<String, String> zonesFaresAndCommutationModes){
		if (zonesFaresAndCommutationModes == null || zonesFaresAndCommutationModes.size() == 0){
			 throw new IllegalParameterException("Fare not found.");
		}else {
			for(Entry<String, String> mapEntry: zonesFaresAndCommutationModes.entrySet()){
				String key = mapEntry.getKey();
				boolean isGenericFare = false;
				Set<Zone> zoneList = null;
				if(WILD_CARD.equals(key)){
					isGenericFare = true;
				}
				else {
					zoneList = getZoneList(key);
				}
				StringTokenizer fareAndTransportationModes = new StringTokenizer(mapEntry.getValue(), 
						FARE_COMMUTATION_MODE_DELIM);
				BigDecimal fareAmount = getFareAmount(fareAndTransportationModes);
				String transporationMode = fareAndTransportationModes.nextToken();
				CommutationMode mode = CommutationMode.getValueOf(transporationMode);
				if(mode==null){
					logger.error("Fares are not provided correctly.");
			        throw new IllegalParameterException("Fares are not provided correctly.");
			    }
				Fare fare = new Fare(zoneList, fareAmount, isGenericFare);
				if(isGenericFare){
					genericFares.put(mode, fare);
				}
				fares.add(fare);
				if(maxFare.compareTo(fareAmount) < 0){
					maxFare = fareAmount;
				}
			}
			if(null != RequestContextHolder.getRequestAttributes()) {
				Map<String, Object> allFareDetails = new HashMap<String, Object>();
				allFareDetails.put("fares", fares);
				allFareDetails.put("maxFare", maxFare);
				allFareDetails.put("genericFares", genericFares);	
				RequestContextHolder.getRequestAttributes().
					setAttribute("all_fare_details", allFareDetails, RequestAttributes.SCOPE_REQUEST);
			}
		}
	}

	private BigDecimal getFareAmount(StringTokenizer fareAndTransportationModes) {
		String fare = fareAndTransportationModes.nextToken();
		if(fare==null || fare.isEmpty()){
			logger.error("Fares are not provided correctly. Invalid Fare");
			throw new IllegalArgumentException("Fares are not provided correctly. Invalid Fare");
		}
		return new BigDecimal(fare);
	}

	private Set<Zone> getZoneList(String key) {
		Set<Zone> zoneList = new HashSet<>();
		StringTokenizer zones = new StringTokenizer(key, ",");
		int zoneCount = zones.countTokens();
		for (int i = 0; i < zoneCount; i++) {
			String zoneId = zones.nextToken();
			if(zoneId==null || zoneId.isEmpty()){
				logger.error("Fares are not provided correctly. Invalid Zone list");
				throw new IllegalParameterException("Fares are not provided correctly. Invalid Zone list");
			}
			Zone zone = null;
			try {
				zone = new Zone(Integer.parseInt(zoneId));
			}catch(NumberFormatException ex) {
				throw new GenericException("Not in number format, parsin failed");
			}
			zoneList.add(zone);
		}
		return zoneList;
	}
	
	public BigDecimal getMaxFare(){
		return maxFare;
	}

	public BigDecimal calculateCost(List<Trip> trips) {
		BigDecimal totalCost = BigDecimal.valueOf(0.0);
		boolean applySpecificFare = false;
		Set<Station> uniqueStations = new HashSet<>();
		for(Trip trip: trips){
			CommutationMode transporationMode = trip.getTransporationMode();
			Fare genericFare = genericFares.get(transporationMode);
			if(genericFare!=null){
				totalCost = totalCost.add(genericFare.getAmount());
			}
			else{
				applySpecificFare = true;
				uniqueStations.add(trip.getFrom());
				uniqueStations.add(trip.getTo());
			}
		}
		BigDecimal minFare = BigDecimal.valueOf(0.0);
		if (applySpecificFare) {
			for (Fare fare : fares) {
				if (isFareLessAndCoversAllZones(uniqueStations, minFare, fare)) {
					minFare = fare.getAmount();
				}
			}
			totalCost = totalCost.add(minFare);
		}
		return totalCost;
	}

	private boolean isFareLessAndCoversAllZones(Set<Station> uniqueStations, BigDecimal minFare, Fare fare) {
		if(fare.isGenericFare() || minFare.compareTo(BigDecimal.valueOf(0.0)) != 0 
				&& minFare.compareTo(fare.getAmount()) < 0){
			return false;
		}
		for(Station station: uniqueStations){
			if(Collections.disjoint(station.getZones(), fare.getZones())){
				return false;
			}
		}
		return true;
	}

	public boolean isGenericFareApplicable(CommutationMode transporationMode){
		return genericFares.get(transporationMode)!=null;
	}

}
