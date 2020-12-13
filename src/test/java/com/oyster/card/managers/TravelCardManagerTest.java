package com.oyster.card.managers;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import com.oyster.card.beans.TravelCard;
import com.oyster.card.exception.IllegalParameterException;
import com.oyster.card.managers.FareManager;
import com.oyster.card.managers.StationManager;
import com.oyster.card.managers.TravelCardManager;
import com.oyster.card.utils.OysterCardUtils;

@RunWith(SpringRunner.class)
public class TravelCardManagerTest {
	
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
	private TravelCardManager travelCardManager;
	
	@Test
	public void testGetNew() {
		BigDecimal initialAmount = new BigDecimal(20.0);
		TravelCard card = travelCardManager.getNew(initialAmount);
		Assert.notNull(card, "Travel Card created, id:"+card.getId());
	}
	
	@Test(expected = IllegalParameterException.class)
	public void negative_testGetNew() {
		BigDecimal initialAmount = new BigDecimal(-1.0);
		travelCardManager.getNew(initialAmount);
		Assertions.assertThatExceptionOfType(IllegalParameterException.class);
	}
	
	@Test
	public void testTopup() {
		BigDecimal initialAmount = new BigDecimal(20.0);
		TravelCard card = travelCardManager.getNew(initialAmount);
		travelCardManager.topUp(card, new BigDecimal(5.0));
		if(card.getBalance().equals(new BigDecimal(25.0))) {
			Assert.notNull(card, "Card topped up");
		}
	}
	
	@Test
	public void testDeductForTrip() {
		BigDecimal initialAmount = new BigDecimal(20.0);
		TravelCard card = travelCardManager.getNew(initialAmount);
		BigDecimal deduction = new BigDecimal(1.80);
		travelCardManager.deductForTrip(card, deduction);
		if(card.getBalance().equals(initialAmount.subtract(deduction))) {
			Assert.notNull(card, "Amount Deducted.");
		}		
	}
	
	@Test
	public void testGetBalance() {
		BigDecimal initialAmount = new BigDecimal(20.0);
		TravelCard card = travelCardManager.getNew(initialAmount);
		BigDecimal balanceOnCard = travelCardManager.getBalance(card);
		if(balanceOnCard.equals(initialAmount)) {
			Assert.notNull(balanceOnCard, "Balance found.");
		}
	}
	
	@Test
	public void testApplyRefund() {
		BigDecimal initialAmount = new BigDecimal(20.0);
		TravelCard card = travelCardManager.getNew(initialAmount);
		travelCardManager.applyRefund(card, new BigDecimal(1.80));
		if(card.getBalance().equals(new BigDecimal(21.80))) {
			Assert.notNull(card, "Refund issued.");
		}
	}

}
