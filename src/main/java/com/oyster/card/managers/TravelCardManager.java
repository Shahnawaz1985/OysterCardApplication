package com.oyster.card.managers;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.oyster.card.beans.TravelCard;
import com.oyster.card.exception.GenericException;
import com.oyster.card.exception.IllegalParameterException;

/**
 * 
 * @author Shahnawaz
 *
 */
@Service
public class TravelCardManager {
	
	private static Logger logger = Logger.getLogger(TravelCardManager.class);
	
	private Map<Long, TravelCard> cards = new HashMap<>();
	private long range = 12345l;
	private Random r = new Random();
	
	public TravelCard getNew(BigDecimal initialAmount) throws IllegalParameterException{
		TravelCard card = null;
		if(null != initialAmount) {
			long id = (long)(r.nextDouble()*range);
			if(initialAmount.compareTo(new BigDecimal(0.0)) < 0)	{
				throw new IllegalParameterException("Negative Amount found");
			}
			card = new TravelCard(id, initialAmount);
			cards.put(id, card);
			logger.info("New card has been purchased from the system."+card);
			TravelCard card1 = new TravelCard(card.getId(), card.getBalance());
			if(null != RequestContextHolder.getRequestAttributes() && null != card1) {
			RequestContextHolder.getRequestAttributes().
				setAttribute("new_card", card1, RequestAttributes.SCOPE_REQUEST);
			}
		}
		return card;
	}

	public void topUp(TravelCard card, BigDecimal topUp) throws IllegalParameterException{
		TravelCard existingCard = lookup(card);
		if(null != existingCard && null != topUp) {
			existingCard.creditAmount(topUp);
			TravelCard topped_up_card = new TravelCard(card.getId(), card.getBalance());
			if(null != RequestContextHolder.getRequestAttributes() && null != topped_up_card) {
				RequestContextHolder.getRequestAttributes().
					setAttribute("topped_up_card", topped_up_card, RequestAttributes.SCOPE_REQUEST);
			}
		}else {
			throw new IllegalParameterException("Card details not found.");
		}
		logger.info("Top up done for amount: "+topUp+". "+card);
	}

	private TravelCard lookup(TravelCard card) {
		TravelCard cardInSystem = cards.get(card.getId());
		if(cardInSystem==null){
			logger.error("Provided card is not known to the system");
			throw new GenericException("Provided card is not known to the system");
		}
		return cardInSystem;
	}
	
	public void deductForTrip(TravelCard card, BigDecimal maxFare) {
		TravelCard existingCard = lookup(card);
		try {
			if(null != existingCard) {
				existingCard.debitAmount(maxFare);
			}else {
				throw new IllegalParameterException("Card details not found.");
			}
		} catch (IllegalParameterException e) {
			logger.error("Exception while debiting card amount after journey:"+e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public BigDecimal getBalance(TravelCard card) {
		return lookup(card).getBalance();
	}

	public void applyRefund(TravelCard card, BigDecimal refund) {
		lookup(card).creditAmount(refund);
	}

}
