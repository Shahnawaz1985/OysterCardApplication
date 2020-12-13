package com.oyster.card.beans;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Shahnawaz
 */
public class Fare {
	/**
	 * 
	 */
	private Set<Zone> zones = new HashSet<>();
	/**
	 * 
	 */
	private BigDecimal amount;
	/**
	 * 
	 */
	private boolean isGenericFare;
	
	
	/**
	 * @param zone_set zone_set
	 * @param fare_amount fare_amount
	 * @param isGeneric isGeneric
	 */
	public Fare(Set<Zone> zone_set, BigDecimal fare_amount, 
			boolean isGeneric) {
		super();
		this.zones = zone_set;
		this.amount = fare_amount;
		this.isGenericFare = isGeneric;
	}

	/**
	 * @return BigDecimal
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @return Set<Zone>
	 */
	public Set<Zone> getZones() {
		return zones;
	}

	/**
	 * @return isGenericFare
	 */
	public boolean isGenericFare() {
		return isGenericFare;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Fare [zones=");
		builder.append(zones);
		builder.append(", amount=");
		builder.append(amount);
		builder.append(", isGenericFare=");
		builder.append(isGenericFare);
		builder.append("]");
		return builder.toString();
	}
	
	

}
