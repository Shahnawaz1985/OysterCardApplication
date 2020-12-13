package com.oyster.card.beans;

import java.math.BigDecimal;

import com.oyster.card.exception.IllegalParameterException;

/**
 * @author Shahnawaz
 */
public class TravelCard {
	/**
	 * 
	 */
	private long id;
	/**
	 * 
	 */
	private BigDecimal balance;
	
	/**
	 * @param i_id i_id
	 * @param i_initialValue i_initialValue
	 * @throws IllegalParameterException IllegalParameterException
	 */
	public TravelCard(long i_id, BigDecimal i_initialValue) throws IllegalParameterException{
		this.id = i_id;
		balance = i_initialValue;
		if (i_initialValue == null || BigDecimal.ZERO.compareTo(i_initialValue) > 0){
			throw new IllegalParameterException("InitialValue: "
			+i_initialValue+" should be a positive value");
		}
	}
	
	/**
	 * @param amount amount
	 * @throws IllegalParameterException IllegalParameterException
	 */
	public void debitAmount(BigDecimal amount) throws IllegalParameterException{
		if(balance.compareTo(amount) < 0){
			throw new IllegalParameterException("Not sufficient funds"
			+ " in the card. Card balance: "+this.balance+", "
			+ "amount seeked: "+amount);
		}
		this.balance = this.balance.subtract(amount);
	}
	
	/**
	 * @param amount amount
	 */
	public void creditAmount(BigDecimal amount){
		this.balance = this.balance.add(amount);
	}

	/**
	 * @return balance
	 */
	public BigDecimal getBalance() {
		return this.balance;
	}

	/**
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TravelCard [id=");
		builder.append(id);
		builder.append(", balance=");
		builder.append(balance);
		builder.append("]");
		return builder.toString();
	}

}
