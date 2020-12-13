package com.oyster.card.services;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.oyster.card.beans.TravelCard;
import com.oyster.card.exception.GenericException;
import com.oyster.card.exception.IllegalParameterException;

/**
 * 
 * @author Shahnawaz
 *
 */
@Service
public interface OysterCardService {
	
	/**
	 * 
	 * @param initialAmount
	 * @return
	 * @throws IllegalParameterException 
	 */
	TravelCard purchaseNewCard(BigDecimal initialAmount) throws IllegalParameterException;
	
	/**
	 * 
	 * @param stationsAndZonesFilePath
	 * @param zonesAndFaresFilePath
	 * @throws IOException
	 */
	void initializeSystem(String stationsAndZonesFilePath, String zonesAndFaresFilePath) throws GenericException;
	
	/**
	 * 
	 * @param card
	 * @param travelDataFilePath
	 * @throws Exception
	 */
	void takeTrips(TravelCard card, String travelDataFilePath) throws Exception;
	
	/**
	 * 
	 * @param card
	 * @param topUp
	 * @throws IllegalParameterException 
	 */
	void topUpCard(TravelCard card, BigDecimal topUp) throws IllegalParameterException;

}
