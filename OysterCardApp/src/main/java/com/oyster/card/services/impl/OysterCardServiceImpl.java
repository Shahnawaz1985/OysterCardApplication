package com.oyster.card.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.oyster.card.beans.TravelCard;
import com.oyster.card.beans.Trip;
import com.oyster.card.exception.GenericException;
import com.oyster.card.exception.IllegalParameterException;
import com.oyster.card.managers.FareManager;
import com.oyster.card.managers.StationManager;
import com.oyster.card.managers.TravelCardManager;
import com.oyster.card.services.OysterCardService;
import com.oyster.card.utils.OysterCardUtils;

/**
 * 
 * @author Shahnawaz
 *
 */
@Service
public class OysterCardServiceImpl implements OysterCardService{
	
	private static Logger logger = Logger.getLogger(OysterCardServiceImpl.class);
	
	@Autowired
	private StationManager stationManager;
	
	@Autowired
	private FareManager fareManager;
	
	@Autowired
	private TravelCardManager cardManager;
	
	@Autowired
	private OysterCardUtils oysterCardUtils;
	
	
	@Override
	public TravelCard purchaseNewCard(BigDecimal initialAmount) throws IllegalParameterException {
		return cardManager.getNew(initialAmount);
	}

	/**
	 *
	 */
	@Override
	public void initializeSystem(String stationsAndZonesFilePath, String zonesAndFaresFilePath)
			throws GenericException {
		String methodName = "initializeSystem";
		logger.info("Entering method:"+methodName);
		initStations(stationsAndZonesFilePath);
		initFares(zonesAndFaresFilePath);
		logger.info("Exit method:"+methodName);
	}

	/**
	 * @throws IllegalParameterException 
	 *
	 */
	@Override
	public void takeTrips(TravelCard card, String travelDataFilePath) throws GenericException, IllegalParameterException {
		String methodName = "takeTrips";
		logger.info("Entering method:"+methodName);
		List<Trip> trips = null;
		try {
			trips = oysterCardUtils.extractTripsFrom(travelDataFilePath);
			if(null != RequestContextHolder.getRequestAttributes() && null != trips) {
				RequestContextHolder.getRequestAttributes().
					setAttribute("trips_details", trips, RequestAttributes.SCOPE_REQUEST);
			}
		} catch (IllegalParameterException e) {
			logger.error("Exception while extracting trip data :"+e.getLocalizedMessage());
			throw new GenericException(e.getLocalizedMessage());
		}
		takeTrips(card, trips);
		logger.info("Exit method :"+methodName);
	}

	/**
	 * @throws IllegalParameterException 
	 *
	 */
	@Override
	public void topUpCard(TravelCard card, BigDecimal topUp) throws IllegalParameterException {
		String methodName = "topUpCard";
		logger.info("Entering method :"+methodName);
		cardManager.topUp(card, topUp);	
		logger.info("Exit method :"+methodName);
		
	}
	
	/**
	 * @throws IllegalParameterException 
	 *
	 */
	private void takeTrips(TravelCard card, List<Trip> trips) throws IllegalParameterException {
		List<Trip> tripsTakenTillNow = new ArrayList<>();
		BigDecimal lastTripCost ;
		BigDecimal tripCost = BigDecimal.valueOf(0.0);
		Map<String, Map<Trip, String>> allTripsTaken = new LinkedHashMap<>();
		Map<Trip, String> tripMap = null;
		logger.info("Taking trips now");
		if(null != card && null != trips && trips.size()>0) {
			for(int i =0; i < trips.size(); i++){
				cardManager.deductForTrip(card, fareManager.getMaxFare());
				Trip trip = trips.get(i);
				logger.info("Deducted max applicable fare for trip: "+trip.getJourneyDetails()+". "+card);
				tripsTakenTillNow.add(trip);
				lastTripCost = tripCost;
				tripCost = fareManager.calculateCost(tripsTakenTillNow);
				logger.info("Total trip cost until now: "+tripCost);
				BigDecimal refund = fareManager.getMaxFare().subtract(tripCost.subtract(lastTripCost));
				cardManager.applyRefund(card,  refund);
				logger.info("Made adjustments after this trip. "+card);
				tripMap = new HashMap<Trip, String>();
				tripMap.put(trip, "Trip Cost :"+tripCost+", updated card details "+ 
						new TravelCard(card.getId(), card.getBalance()));
				allTripsTaken.put("Trip-"+i, tripMap);
			}
		}
		if(null != RequestContextHolder.getRequestAttributes() && !allTripsTaken.isEmpty()) {
			RequestContextHolder.getRequestAttributes().setAttribute("total_trips_taken", 
				allTripsTaken, RequestAttributes.SCOPE_REQUEST);
		}
	}
	
	private void initStations(String stationsAndZonesFilePath) throws GenericException {
		Map<String, List<Integer>> stationNameWithZoneIds = oysterCardUtils.createStationNameWithZoneIds(stationsAndZonesFilePath);
		try {
			stationManager.initialize(stationNameWithZoneIds);
		} catch (IllegalParameterException e) {
			logger.error("Exception while initializing stationManager :"+e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	private void initFares(String zonesAndFaresFilePath) throws GenericException {
		Map<String, String> zonesFaresAndCommutationModes = oysterCardUtils.
				createZonesFaresAndCommutationModes(zonesAndFaresFilePath);
		fareManager.initialize(zonesFaresAndCommutationModes);
	}	
}
